package com.trivadis.plsql.formatter.sqlcl.tests;

import oracle.dbtools.raptor.newscriptrunner.CommandRegistry;
import oracle.dbtools.raptor.newscriptrunner.SQLCommand;
import oracle.dbtools.raptor.newscriptrunner.ScriptExecutor;
import oracle.dbtools.raptor.newscriptrunner.ScriptRunnerContext;
import oracle.dbtools.raptor.newscriptrunner.WrapListenBufferOutputStream;
import org.junit.jupiter.api.BeforeEach;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractSqlclTest {
    protected final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    protected final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
    protected final ScriptContext scriptContext = new SimpleScriptContext();
    protected final ScriptRunnerContext ctx = new ScriptRunnerContext();
    protected final ScriptExecutor sqlcl = new ScriptExecutor(null);
    protected final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Path tempDir;
    
    AbstractSqlclTest() {
        reset();
    }

    public void reset() {
        CommandRegistry.removeListener(SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        CommandRegistry.clearCaches(null, ctx);
        setup();
    }

    @BeforeEach
    public void setup() {
        byteArrayOutputStream.reset();
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        final WrapListenBufferOutputStream wrapListenBufferOutputStream = new WrapListenBufferOutputStream(bufferedOutputStream);
        final Bindings bindings = new SimpleBindings();
        bindings.put("polyglot.js.nashorn-compat", true);
        bindings.put("polyglot.js.allowHostAccess", Boolean.TRUE);
        bindings.put("polyglot.js.allowNativeAccess", Boolean.TRUE);
        bindings.put("polyglot.js.allowCreateThread", Boolean.TRUE);
        bindings.put("polyglot.js.allowIO", Boolean.TRUE);
        bindings.put("polyglot.js.allowHostClassLoading", Boolean.TRUE);
        bindings.put("polyglot.js.allowHostClassLookup",  (Predicate<String>) s -> true);
        bindings.put("polyglot.js.allowAllAccess", Boolean.TRUE);
        scriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        sqlcl.setOut(bufferedOutputStream);
        ctx.setOutputStreamWrapper(wrapListenBufferOutputStream);
        sqlcl.setScriptRunnerContext(ctx);
        scriptContext.setAttribute("ctx", ctx, ScriptContext.ENGINE_SCOPE);
        scriptContext.setAttribute("sqlcl", sqlcl, ScriptContext.ENGINE_SCOPE);
        try {
            tempDir = Files.createTempDirectory("plsql-formatter-test-");
            final Path unformattedDir = Paths.get(Thread.currentThread().getContextClassLoader().getResource("unformatted").getPath());
            final List<Path> sources = Files.walk(unformattedDir).filter(f -> Files.isRegularFile(f))
                    .collect(Collectors.toList());
            for (Path source : sources) {
                Path target = Paths.get(tempDir.toString() + File.separator + source.getFileName());
                Files.copy(source, target);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    public String run(RunType runType, String...arguments) {
        if (runType == RunType.FormatJS) {
            return runScript(arguments);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("tvdformat");
            for (int i = 0; i < arguments.length; i++) {
                sb.append(" ");
                sb.append(arguments[i]);
            }
            return runCommand (sb.toString());
        }
    }
    
    public String runScript(String... arguments) {
        final URL script = Thread.currentThread().getContextClassLoader().getResource("format.js");
        final String[] args = new String[arguments.length + 1];
        args[0] = "format.js";
        for (int i=0; i < arguments.length; i++) {
            args[i+1] = arguments[i];
        }
        scriptContext.setAttribute("args", args, ScriptContext.ENGINE_SCOPE);
        try {
            scriptEngine.eval(new InputStreamReader(script.openStream()), scriptContext);
        } catch (ScriptException | IOException e) {
            throw new RuntimeException(e);
        }
        return getConsoleOutput();
    }
    
    public String runCommand(String cmdLine) {
        ScriptExecutor executor = new ScriptExecutor(cmdLine, null);
        executor.setScriptRunnerContext(ctx);
        executor.run();
        return getConsoleOutput();
    }
    
    private String getConsoleOutput(){
        try {
            ctx.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String result = new String(byteArrayOutputStream.toByteArray());
        return result;
    }
    
    public String getOriginalContent(String fileName) {
        Path file = Paths.get(Thread.currentThread().getContextClassLoader().getResource("unformatted/" + fileName).getPath());
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
        Path file = Paths.get(tempDir.toString() + File.separator + fileName);
        return getFileContent(file);
    }

}
