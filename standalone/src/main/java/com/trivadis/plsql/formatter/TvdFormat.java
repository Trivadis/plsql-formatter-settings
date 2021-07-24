package com.trivadis.plsql.formatter;

import oracle.dbtools.raptor.newscriptrunner.*;

import javax.script.*;
import java.io.*;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.LogManager;

public class TvdFormat {
    protected final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    protected final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
    protected final ScriptContext scriptContext = new SimpleScriptContext();
    protected final ScriptRunnerContext ctx = new ScriptRunnerContext();
    protected final ScriptExecutor sqlcl = new ScriptExecutor(null);
    Path tempDir;

    TvdFormat() {
        CommandRegistry.removeListener(SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        CommandRegistry.clearCaches(null, ctx);
        var bufferedOutputStream = new BufferedOutputStream(System.out);
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
    }

    public void run(String[] arguments) throws IOException, ScriptException {
        LogManager.getLogManager().reset();
        var script = Thread.currentThread().getContextClassLoader().getResource("format.js");
        var args = new String[arguments.length + 1];
        args[0] = "format.js";
        System.arraycopy(arguments, 0, args, 1, arguments.length);
        scriptContext.setAttribute("args", args, ScriptContext.ENGINE_SCOPE);
        assert script != null;
        scriptEngine.eval(new InputStreamReader(script.openStream()), scriptContext);
        ctx.getOutputStream().flush();
    }

    public static void main(String[] args) throws IOException, ScriptException {
        var formatter = new TvdFormat();
        formatter.run(args);
    }
}
