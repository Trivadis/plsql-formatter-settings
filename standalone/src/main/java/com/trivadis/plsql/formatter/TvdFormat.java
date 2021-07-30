package com.trivadis.plsql.formatter;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import org.graalvm.polyglot.Context;

import javax.script.*;
import java.io.*;
import java.net.URL;
import java.util.function.Predicate;
import java.util.logging.LogManager;

public class TvdFormat {
    private final ScriptEngine scriptEngine;
    private final ScriptRunnerContext ctx;

    TvdFormat() {
        scriptEngine = GraalJSScriptEngine.create(null,
                Context.newBuilder("js")
                        .option("js.nashorn-compat", "true")
                        .allowAllAccess(true));
        ctx = new ScriptRunnerContext();
        ctx.setOutputStream(System.out);
        scriptEngine.getContext().setAttribute("ctx", ctx, ScriptContext.ENGINE_SCOPE);
    }

    public void run(String[] arguments) throws IOException, ScriptException {
        URL script = Thread.currentThread().getContextClassLoader().getResource("format.js");
        String[] args = new String[arguments.length + 1];
        args[0] = "format.js";
        System.arraycopy(arguments, 0, args, 1, arguments.length);
        scriptEngine.getContext().setAttribute("args", args, ScriptContext.ENGINE_SCOPE);
        assert script != null;
        scriptEngine.eval(new InputStreamReader(script.openStream()), scriptEngine.getContext());
        ctx.getOutputStream().flush();
    }

    public static void main(String[] args) throws IOException, ScriptException {
        LogManager.getLogManager().reset();
        System.setProperty("tvdformat.standalone", "true");
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        TvdFormat formatter = new TvdFormat();
        formatter.run(args);
    }
}
