package com.trivadis.plsql.formatter.sqlcl.tests;

import oracle.dbtools.raptor.newscriptrunner.*;
import org.junit.jupiter.api.BeforeEach;

import javax.script.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.logging.LogManager;

public abstract class AbstractSqlclTest {
    static {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
    }

    protected final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    protected final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
    protected final ScriptContext scriptContext = new SimpleScriptContext();
    protected final ScriptRunnerContext ctx = new ScriptRunnerContext();
    protected final ScriptExecutor sqlcl = new ScriptExecutor(null);
    protected final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Path tempDir;

    AbstractSqlclTest() {
        reset();
        loadLoggingConf();
    }

    private void loadLoggingConf() {
        var disableLogging = System.getProperty("disable.logging");
        var manager = LogManager.getLogManager();
        manager.reset();
        if (disableLogging != null && !disableLogging.trim().equalsIgnoreCase("true")) {
            try {
                manager.readConfiguration(Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.conf"));
            } catch (SecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        CommandRegistry.removeListener(SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        CommandRegistry.clearCaches(null, ctx);
        setup();
    }

    @SuppressWarnings("resource")
    @BeforeEach
    public void setup() {
        byteArrayOutputStream.reset();
        var bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        var wrapListenBufferOutputStream = new WrapListenBufferOutputStream(bufferedOutputStream);
        var bindings = new SimpleBindings();
        bindings.put("polyglot.js.allowHostAccess", true);
        bindings.put("polyglot.js.allowHostClassLookup", (Predicate<String>) s -> true);
        scriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        sqlcl.setOut(bufferedOutputStream);
        ctx.setOutputStreamWrapper(wrapListenBufferOutputStream);
        sqlcl.setScriptRunnerContext(ctx);
        scriptContext.setAttribute("ctx", ctx, ScriptContext.ENGINE_SCOPE);
        scriptContext.setAttribute("sqlcl", sqlcl, ScriptContext.ENGINE_SCOPE);
        try {
            tempDir = Files.createTempDirectory("plsql-formatter-test-");
            var url = Thread.currentThread().getContextClassLoader().getResource("unformatted");
            assert url != null;
            var unformattedDir = new File(url.getFile()).toPath();
            var sources = Files.walk(unformattedDir).filter(Files::isRegularFile).toList();
            for (Path source : sources) {
                Path target = Paths.get(tempDir.toString() + File.separator + source.getFileName());
                Files.copy(source, target);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String run(RunType runType, String... arguments) {
        if (runType == RunType.FormatJS) {
            return runScript(arguments);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("tvdformat");
            for (String argument : arguments) {
                sb.append(" ");
                sb.append(argument);
            }
            return runCommand(sb.toString());
        }
    }

    public String runScript(String... arguments) {
        var script = Thread.currentThread().getContextClassLoader().getResource("format.js");
        var args = new String[arguments.length + 1];
        args[0] = "format.js";
        System.arraycopy(arguments, 0, args, 1, arguments.length);
        scriptContext.setAttribute("args", args, ScriptContext.ENGINE_SCOPE);
        try {
            assert script != null;
            scriptEngine.eval(new InputStreamReader(script.openStream()), scriptContext);
        } catch (ScriptException | IOException e) {
            throw new RuntimeException(e);
        }
        return getConsoleOutput();
    }

    @SuppressWarnings("CallToThreadRun")
    public String runCommand(String cmdLine) {
        var executor = new ScriptExecutor(cmdLine, null);
        executor.setScriptRunnerContext(ctx);
        // synchronous execution, that's what we want here
        executor.run();
        return getConsoleOutput();
    }

    private String getConsoleOutput() {
        try {
            ctx.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toString();
    }

    public String getOriginalContent(String fileName) {
        var url = Thread.currentThread().getContextClassLoader().getResource("unformatted/" + fileName);
        assert url != null;
        var file = new File(url.getFile()).toPath();
        return getFileContent(file);
    }

    private String getFileContent(Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFormattedContent(String fileName) {
        var file = Paths.get(tempDir.toString() + File.separator + fileName);
        return getFileContent(file);
    }
}
