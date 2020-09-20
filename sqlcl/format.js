/*
* Copyright 2020 Philipp Salvisberg <philipp.salvisberg@trivadis.com>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

"use strict";

var getFiles = function (rootPath, extensions) {
    var Arrays = Java.type("java.util.Arrays");
    var Paths = Java.type("java.nio.file.Paths");
    var files;
    if (existsFile(rootPath)) {
        files = Arrays.asList(Paths.get(rootPath));
    } else {
        var Collectors = Java.type("java.util.stream.Collectors");
        var Files = Java.type("java.nio.file.Files");
        files = Files.walk(Paths.get(rootPath))
            .filter(function (f) Files.isRegularFile(f)
                && Arrays.stream(Java.to(extensions, "java.lang.String[]")).anyMatch(function (e) f.toString().toLowerCase().endsWith(e))
            )
            .sorted()
            .collect(Collectors.toList());
    }
    return files;
}

var configure = function (formatter, xmlPath, arboriPath) {
    var File = Java.type("java.io.File");
    var Format = Java.type("oracle.dbtools.app.Format");
    if (!"default".equals(xmlPath) && !"embedded".equals(xmlPath) && xmlPath != null) {
        var Persist2XML = Java.type("oracle.dbtools.app.Persist2XML");
        var url = new File(xmlPath).toURI().toURL();
        var options = Persist2XML.read(url);
        var Collectors = Java.type("java.util.stream.Collectors");
        var keySet = options.keySet().stream().collect(Collectors.toList());
        for (var j in keySet) {
            formatter.options.put(keySet[j], options.get(keySet[j]));
        }
    } else if ("embedded".equals(xmlPath)) {
        // General
        formatter.options.put(formatter.kwCase, Format.Case.UPPER);                                     // default: Format.Case.UPPER
        formatter.options.put(formatter.idCase, Format.Case.NoCaseChange);                              // default: Format.Case.lower
        formatter.options.put(formatter.singleLineComments, Format.InlineComments.CommentsUnchanged);   // default: Format.InlineComments.CommentsUnchanged
        // Alignment
        formatter.options.put(formatter.alignTabColAliases, false);                                     // default: true
        formatter.options.put(formatter.alignTypeDecl, true);                                           // default: true
        formatter.options.put(formatter.alignNamedArgs, true);                                          // default: true
        formatter.options.put(formatter.alignAssignments, true);                                        // default: false
        formatter.options.put(formatter.alignEquality, false);                                          // default: false
        formatter.options.put(formatter.alignRight, true);                                              // default: false
        // Indentation
        formatter.options.put(formatter.identSpaces, 3);                                                // default: 3
        formatter.options.put(formatter.useTab, false);                                                 // default: false
        // Line Breaks
        formatter.options.put(formatter.breaksComma, Format.Breaks.After);                              // default: Format.Breaks.After
        formatter.options.put("commasPerLine", 1);                                                      // default: 5
        formatter.options.put(formatter.breaksConcat, Format.Breaks.Before);                            // default: Format.Breaks.Before
        formatter.options.put(formatter.breaksAroundLogicalConjunctions, Format.Breaks.Before);         // default: Format.Breaks.Before
        formatter.options.put(formatter.breakAnsiiJoin, true);                                          // default: false
        formatter.options.put(formatter.breakParenCondition, true);                                     // default: false
        formatter.options.put(formatter.breakOnSubqueries, true);                                       // default: true
        formatter.options.put(formatter.maxCharLineSize, 120);                                          // default: 128
        formatter.options.put(formatter.forceLinebreaksBeforeComment, false);                           // default: false
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, Format.BreaksX2.X1);      // default: Format.BreaksX2.X2
        formatter.options.put(formatter.breaksAfterSelect, false);                                      // default: true
        formatter.options.put(formatter.flowControl, Format.FlowControl.IndentedActions);               // default: Format.FlowControl.IndentedActions
        // White Space
        formatter.options.put(formatter.spaceAroundOperators, true);                                    // default: true
        formatter.options.put(formatter.spaceAfterCommas, true);                                        // default: true
        formatter.options.put(formatter.spaceAroundBrackets, Format.Space.Default);                     // default: Format.Space.Default
        // Hidden, not configurable in the GUI preferences dialog of SQLDev 20.2
        formatter.options.put(formatter.breaksProcArgs, false);                                         // default: false (overridden in Arbori program based on other settings)
        formatter.options.put(formatter.adjustCaseOnly, false);                                         // default: false (set true to skip formatting)
        formatter.options.put(formatter.formatThreshold, 1);                                            // default: 1 (disables deprecated post-processing logic)
    }
    var arboriFileName = arboriPath;
    if (!"default".equals(arboriPath)) {
        arboriFileName = new File(arboriPath).getAbsolutePath();
    }
    formatter.options.put(formatter.formatProgramURL, arboriFileName);                                  // default: "default" (= provided by SQLDev / SQLcl)
}

var getConfiguredFormatter = function (xmlPath, arboriPath) {
    var Format = Java.type("oracle.dbtools.app.Format")
    var formatter = new Format();
    configure(formatter, xmlPath, arboriPath);
    return formatter;
}

var hasParseErrors = function (content, consoleOutput) {
    var Lexer = Java.type("oracle.dbtools.parser.Lexer");
    var Parsed = Java.type("oracle.dbtools.parser.Parsed");
    var SqlEarley = Java.type("oracle.dbtools.parser.plsql.SqlEarley")
    var newContent = "\n" + content; // ensure correct line number in case of an error
    var tokens = Lexer.parse(newContent);
    var parsed = new Parsed(newContent, tokens, SqlEarley.getInstance(), Java.to(["sql_statements"], "java.lang.String[]"));
    var syntaxError = parsed.getSyntaxError();
    if (syntaxError != null && syntaxError.getMessage() != null) {
        if (consoleOutput) {
            ctx.write(syntaxError.getDetailedMessage());
            ctx.write("... ");
        }
        return true;
    } 
    return false;
}

var readFile = function (file) {
    var Files = Java.type("java.nio.file.Files");
    var String = Java.type("java.lang.String");
    var content = new String(Files.readAllBytes(file));
    return content;
}

var writeFile = function (file, content) {
    var Files = Java.type("java.nio.file.Files");
    Files.write(file, content.getBytes());
}

var existsDirectory = function(dir) {
    var File = Java.type("java.io.File");
    var f = new File(dir);
    return f.isDirectory();
}

var existsFile = function(file) {
    var File = Java.type("java.io.File");
    var f = new File(file);
    return f.isFile();
}

var printUsage = function (asCommand) {
    if (asCommand) {
        ctx.write("usage: tvdformat <rootPath> [options]\n\n");
    } else {
        ctx.write("usage: script format.js <rootPath> [options]\n\n");
    }
    ctx.write("mandatory arguments:\n");
    ctx.write("  <rootPath>      file or path to directory containing files to format (content will be replaced!)\n");
    ctx.write("                  use * to format the SQLcl buffer\n\n");
    ctx.write("options:\n");
    if (!asCommand) {
        ctx.write("  --register, -r  register SQLcl command tvdformat, without processing, no <rootPath> required\n")
    }
    ctx.write("  ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb\n");
    ctx.write("  mext=<ext>      comma separated list of markdown file extensions to process, e.g. ext=md,mdown\n");
    ctx.write("  xml=<file>      path to the file containing the xml file for advanced format settings\n");
    ctx.write("                  xml=default uses default advanced settings included in sqlcl\n");
    ctx.write("                  xml=embedded uses advanced settings defined in format.js\n");
    ctx.write("  arbori=<file>   path to the file containing the Arbori program for custom format settings\n");
    ctx.write("                  arbori=default uses default Arbori program included in sqlcl\n\n");
}

var getJsPath = function() {
    // use original args array at the time when the command was registered
    return args[0].replaceAll("[^\\\\\\/]+(\\.js)?$", "");
}

var getCdPath = function(path) {
    if (path.startsWith("/")) {
        return path; // Unix, fully qualified
    } else if (path.length > 1 && path.substring(1, 2) == ":") {
        return path; // Windows, fully qualified, e.g. C:\mydir
    }
    var currentDir = ctx.getProperty("script.runner.cd_command");
    if (currentDir == null) {
        return path;
    } else {
        var File = Java.type("java.io.File");
        if (path.endsWith(File.separator)) {
            return currentdir + path;
        } else {
            return currentDir + File.separator + path;
        }
    }
}

var processAndValidateArgs = function (args) {
    var rootPath = null;
    var extArgFound = false;
    var extensions = [];
    var mextArgFound = false;
    var markdownExtensions = [];
    var xmlPath = null;
    var arboriPath = null;

    var result = function(valid) {
        var result = {
            rootPath : rootPath,
            extensions : extensions,
            markdownExtensions : markdownExtensions,
            xmlPath : xmlPath,
            arboriPath : arboriPath, 
            valid : valid
        }
        return result;
    }

    if (args.length < 2) {
        ctx.write("missing mandatory <rootPath> argument.\n\n");
        return result(false);
    }
    rootPath = getCdPath(args[1]);
    if (rootPath != "*" && !existsFile(rootPath) && !existsDirectory(rootPath)) {
        ctx.write("file or directory " + rootPath + " does not exist.\n\n");
        return result(false);
    }
    for (var i = 2; i < args.length; i++) {
        if (args[i].toLowerCase().startsWith("ext=")) {
            extArgFound = true;
            if (args[i].length > 4) {
                var values = args[i].substring(4).split(",");
                for (var j in values) {
                    extensions[extensions.length] = "." + values[j].toLowerCase();
                }
            }
            continue;
        }
        if (args[i].toLowerCase().startsWith("mext=")) {
            mextArgFound = true;
            if (args[i].length > 5) {
                var values = args[i].substring(5).split(",");
                for (var j in values) {
                    markdownExtensions[markdownExtensions.length] = "." + values[j].toLowerCase();
                }
            }
            continue;
        }
        if (args[i].toLowerCase().startsWith("xml=")) {
            xmlPath = args[i].substring(4);
            if (!"default".equals(xmlPath) && !"embedded".equals(xmlPath)) {
                xmlPath = getCdPath(xmlPath);
                if (!existsFile(xmlPath)) {
                    ctx.write("file " + xmlPath + " does not exist.\n\n");
                    return result(false);
                }
            }
            continue;
        }
        if (args[i].toLowerCase().startsWith("arbori=")) {
            arboriPath = args[i].substring(7);
            if (!"default".equals(arboriPath)) {
                arboriPath = getCdPath(arboriPath);
                if (!existsFile(getCdPath(arboriPath))) {
                    ctx.write("file " + arboriPath + " does not exist.\n\n");
                    return result(false);
                }
            }
            continue;
        }
        ctx.write("invalid argument " + args[i] + ".\n\n");
        return result(false);
    }
    if (!extArgFound) {
        extensions = [".sql", ".prc", ".fnc", ".pks", ".pkb", ".trg", ".vw", ".tps", ".tpb", ".tbp", ".plb", ".pls", ".rcv", ".spc", ".typ", 
            ".aqt", ".aqp", ".ctx", ".dbl", ".tab", ".dim", ".snp", ".con", ".collt", ".seq", ".syn", ".grt", ".sp", ".spb", ".sps", ".pck"];
    }
    if (!mextArgFound) {
        markdownExtensions = [".markdown", ".mdown", ".mkdn", ".md"];
    }
    for (var j in markdownExtensions) {
        extensions[extensions.length] = markdownExtensions[j];
    }
    if (xmlPath == null) {
        xmlPath = getJsPath() + "../settings/sql_developer/trivadis_advanced_format.xml"
        if (!existsFile(xmlPath)) {
            ctx.write('Warning: ' + xmlPath + ' not found, using "embedded" instead.\n\n');
            xmlPath = "embedded"; 
        }
    }
    if (arboriPath == null) {
        arboriPath = getJsPath() + "../settings/sql_developer/trivadis_custom_format.arbori"
        if (!existsFile(arboriPath)) {
            ctx.write('Warning: ' + arboriPath + ' not found, using "default" instead.\n\n');
            arboriPath = "default"; 
        }
    }
    return result(true);
}

var formatBuffer = function(formatter) {
    ctx.write("Formatting SQLcl buffer... ");
    ctx.getOutputStream().flush();
    var original = ctx.getSQLPlusBuffer().getBufferSafe().getBuffer();
    if (hasParseErrors(original, true)) {
        ctx.write("skipped.\n");
    } else {
        var Arrays = Java.type("java.util.Arrays");
        var formatted = Arrays.asList(formatter.format(original).split("\n"));
        ctx.getSQLPlusBuffer().getBufferSafe().resetBuffer(formatted);
        ctx.write("done.\n");
        ctx.write(ctx.getSQLPlusBuffer().getBufferSafe().list(false));
    }
    ctx.write("\n");
    ctx.getOutputStream().flush();
}

var isMarkdownFile = function(file, markdownExtensions) {
    for (var j in markdownExtensions) {
        if (file.toString().toLowerCase().endsWith(markdownExtensions[j])) {
            return true;
        }
    }
    return false;
} 

var formatMarkdownFile = function(file, formatter) {
    var original = readFile(file)
    var Pattern = Java.type("java.util.regex.Pattern");
    var p = Pattern.compile("(```\\s*sql\\s*\\n)(.+?)(\\n```)", Pattern.DOTALL);
    var m = p.matcher(original);
    var result = "";
    var pos = 0;
    while (m.find()) {
        result += original.substring(pos, m.end(1));
        if (hasParseErrors(m.group(2), false)) {
            result += original.substring(m.start(2), m.end(3));
        } else {
            result += formatter.format(m.group(2));
            result += original.substring(m.end(2), m.end(3));
        }
        pos = m.end(3);
    }
    if (original.length > pos) {
        result += original.substring(pos);
    }
    writeFile(file, result);
    ctx.write("done.\n");
}

var formatFile = function(file, formatter) {
    var original = readFile(file)
    if (hasParseErrors(original, true)) {
        ctx.write("skipped.\n");
    } else {
        writeFile(file, formatter.format(original));
        ctx.write("done.\n");
    }
}

var formatFiles = function(files, formatter, markdownExtensions) {
    for (var i in files) {
        ctx.write("Formatting file " + (i+1) + " of " + files.length + ": " + files[i].toString() + "... ");
        ctx.getOutputStream().flush();
        if (isMarkdownFile(files[i], markdownExtensions)) {
            formatMarkdownFile(files[i], formatter);
        } else {
            formatFile(files[i], formatter);
        }
        ctx.getOutputStream().flush();
    }
}

var run = function(args) { 
    ctx.write("\n");
    var options = processAndValidateArgs(args);
    if (!options.valid) {
        printUsage(args[0].equalsIgnoreCase("tvdformat"));
    } else {
        var formatter = getConfiguredFormatter(options.xmlPath, options.arboriPath);
        if (options.rootPath == "*") {
            formatBuffer(formatter);
        } else {
            var files = getFiles(options.rootPath, options.extensions);
            formatFiles(files, formatter, options.markdownExtensions);
        }
    }
}

var getArgs = function(cmdLine) {
    var Pattern = Java.type("java.util.regex.Pattern");
    var p = Pattern.compile('("[^"]*")|([^ ]+)');
    var m = p.matcher(cmdLine.trim());
    var args = [];
    while (m.find()) {
        args[args.length] = m.group();
    }
    return args;
}

var unregisterTvdFormat = function() {
    var CommandRegistry = Java.type("oracle.dbtools.raptor.newscriptrunner.CommandRegistry");
    var SQLCommand = Java.type("oracle.dbtools.raptor.newscriptrunner.SQLCommand");
    var listeners = CommandRegistry.getListeners(null, ctx).get(SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
    // remove all commands registered with CommandRegistry.addForAllStmtsListener
    CommandRegistry.removeListener(SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
    CommandRegistry.clearCaches(null, ctx);
    var Collectors = Java.type("java.util.stream.Collectors");
    var remainingListeners = CommandRegistry.getListeners(null, ctx).get(SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE)
            .stream().map(function(l) l.getClass()).collect(Collectors.toSet());
    // re-register all commands except for class TvdFormat and remaining (not removed) listener classes
    for (var i in listeners) {
        if (!listeners.get(i).toString().equals("TvdFormat") && !remainingListeners.contains(listeners.get(i).getClass())) {
            CommandRegistry.addForAllStmtsListener(listeners.get(i).getClass());
        }
    }
}

var registerTvdFormat = function() {
    var handleEvent = function(conn, ctx, cmd) {
        var args = getArgs(cmd.getSql());
        if (args != null && typeof args[0] != "undefined" && args[0].equalsIgnoreCase("tvdformat")) {
            run(args);
            return true;
        }
        return false;
    }
    var beginEvent = function(conn, ctx, cmd) {}
    var endEvent = function(conn, ctx, cmd) {}
    var toString = function() {
        // to identify this dynamically created class during unregisterTvdFormat()
        return "TvdFormat";
    }
    var CommandListener =  Java.type("oracle.dbtools.raptor.newscriptrunner.CommandListener")
    var TvdFormat = Java.extend(CommandListener, {
        handleEvent: handleEvent,
        beginEvent: beginEvent,
        endEvent: endEvent,
        toString: toString
    });
    unregisterTvdFormat();
    var CommandRegistry = Java.type("oracle.dbtools.raptor.newscriptrunner.CommandRegistry");
    CommandRegistry.addForAllStmtsListener(TvdFormat.class);
    ctx.write("tvdformat registered as SQLcl command.\n");
}

// main
if (args.length >= 2 && (args[1].equalsIgnoreCase("-r") || args[1].equalsIgnoreCase("--register"))) {
    registerTvdFormat();
} else {
    run(args);
}    
