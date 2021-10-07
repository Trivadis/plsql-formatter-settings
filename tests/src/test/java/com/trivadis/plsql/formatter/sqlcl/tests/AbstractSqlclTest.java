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
import java.util.stream.Collectors;

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
        var manager = LogManager.getLogManager();
        try {
            manager.readConfiguration(Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.conf"));
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        CommandRegistry.removeListener(SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        CommandRegistry.clearCaches(null, ctx);
        setup();
    }

    @BeforeEach
    public void setup() {
        byteArrayOutputStream.reset();
        var bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        var wrapListenBufferOutputStream = new WrapListenBufferOutputStream(bufferedOutputStream);
        var bindings = new SimpleBindings();
        bindings.put("polyglot.js.nashorn-compat", true);
        bindings.put("polyglot.js.allowHostAccess", Boolean.TRUE);
        bindings.put("polyglot.js.allowNativeAccess", Boolean.TRUE);
        bindings.put("polyglot.js.allowCreateThread", Boolean.TRUE);
        bindings.put("polyglot.js.allowIO", Boolean.TRUE);
        bindings.put("polyglot.js.allowHostClassLoading", Boolean.TRUE);
        bindings.put("polyglot.js.allowHostClassLookup", (Predicate<String>) s -> true);
        bindings.put("polyglot.js.allowAllAccess", Boolean.TRUE);
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
            var unformattedDir = Paths.get(url.getPath());
            var sources = Files.walk(unformattedDir).filter(Files::isRegularFile)
                    .collect(Collectors.toList());
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
        var file = Paths.get(url.getPath());
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
