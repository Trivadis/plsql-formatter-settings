package com.trivadis.plsql.formatter;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.LogManager;

public class TvdFormat {
    private final ScriptEngine scriptEngine;
    private final ScriptRunnerContext ctx;

    TvdFormat() {
        scriptEngine = GraalJSScriptEngine.create(null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.ALL)
                        .allowHostClassLookup(s -> true));
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
        // disable logging by default as in SQLcl
        LogManager.getLogManager().reset();
        // amend usage help in format.js for standalone tvdformat
        System.setProperty("tvdformat.standalone", "true");
        // format.js is compiled at runtime with a GraalVM JDK but interpreted with other JDKs
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        // run formatter with command line parameters
        TvdFormat formatter = new TvdFormat();
        formatter.run(args);
    }
}
