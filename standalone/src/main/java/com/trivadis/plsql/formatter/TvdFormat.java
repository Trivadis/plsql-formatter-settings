package com.trivadis.plsql.formatter;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import oracle.dbtools.arbori.Program;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

import javax.script.*;
import java.io.*;
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
        // configure logging
        LogManager.getLogManager().reset();
        String loggingConfFile = System.getenv("TVDFORMAT_LOGGING_CONF_FILE");
        if (loggingConfFile != null) {
            // enable logging according java.util.logging configuration file
            try {
                LogManager.getLogManager().readConfiguration(new FileInputStream(loggingConfFile));
            } catch (FileNotFoundException e) {
                System.out.println("\nWarning: The file '" + loggingConfFile +
                        "' does not exist. Please update the environment variable TVDFORMAT_LOGGING_CONF_FILE.\n");
            }
        }
        // enable Arbori program debug
        String debug = System.getenv("TVDFORMAT_DEBUG");
        if (debug != null && debug.trim().equalsIgnoreCase("true")) {
            Program.debug = true;
        }
        // enable Arbori program timing
        String timing = System.getenv("TVDFORMAT_TIMING");
        if (timing != null && timing.trim().equalsIgnoreCase("true")) {
            Program.timing = true;
        }
        // amend usage help in format.js for standalone tvdformat
        System.setProperty("tvdformat.standalone", "true");
        // format.js is compiled at runtime with a GraalVM JDK but interpreted with other JDKs
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        // run formatter with command line parameters
        TvdFormat formatter = new TvdFormat();
        formatter.run(args);
    }
}
