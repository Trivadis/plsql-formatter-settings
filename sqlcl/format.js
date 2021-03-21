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

var javaString = Java.type("java.lang.String");
var javaArrays = Java.type("java.util.Arrays");
var javaPaths = Java.type("java.nio.file.Paths");
var javaFile = Java.type("java.io.File");
var javaFiles = Java.type("java.nio.file.Files");
var javaCollectors = Java.type("java.util.stream.Collectors");
var javaPersist2XML = Java.type("oracle.dbtools.app.Persist2XML");
var javaPattern = Java.type("java.util.regex.Pattern");
var odbtSQLCommand = Java.type("oracle.dbtools.raptor.newscriptrunner.SQLCommand");
var odbtFormat = Java.type("oracle.dbtools.app.Format");
var odbtLexer = Java.type("oracle.dbtools.parser.Lexer");
var odbtParsed = Java.type("oracle.dbtools.parser.Parsed");
var odbtSqlEarley = Java.type("oracle.dbtools.parser.plsql.SqlEarley");
var odbtCommandRegistry = Java.type("oracle.dbtools.raptor.newscriptrunner.CommandRegistry");
var odbtCommandListener =  Java.type("oracle.dbtools.raptor.newscriptrunner.CommandListener");

var getFiles = function (rootPath, extensions) {
    var files;
    if (existsFile(rootPath)) {
        files = javaArrays.asList(javaPaths.get(rootPath));
    } else {
        files = javaFiles.walk(javaPaths.get(rootPath))
            .filter(function (f) javaFiles.isRegularFile(f)
                && javaArrays.stream(Java.to(extensions, "java.lang.String[]")).anyMatch(function (e) f.toString().toLowerCase().endsWith(e))
            )
            .sorted()
            .collect(javaCollectors.toList());
    }
    return files;
}

var configure = function (formatter, xmlPath, arboriPath) {
    if (!"default".equals(xmlPath) && !"embedded".equals(xmlPath) && xmlPath != null) {
        var url = new javaFile(xmlPath).toURI().toURL();
        var options = javaPersist2XML.read(url);
        var keySet = options.keySet().stream().collect(javaCollectors.toList());
        for (var j in keySet) {
            formatter.options.put(keySet[j], options.get(keySet[j]));
        }
    } else if ("embedded".equals(xmlPath)) {
        // General
        formatter.options.put(formatter.kwCase, odbtFormat.Case.UPPER);                                     // default: odbtFormat.Case.UPPER
        formatter.options.put(formatter.idCase, odbtFormat.Case.NoCaseChange);                              // default: odbtFormat.Case.lower
        formatter.options.put(formatter.singleLineComments, odbtFormat.InlineComments.CommentsUnchanged);   // default: odbtFormat.InlineComments.CommentsUnchanged
        // Alignment
        formatter.options.put(formatter.alignTabColAliases, false);                                         // default: true
        formatter.options.put(formatter.alignTypeDecl, true);                                               // default: true
        formatter.options.put(formatter.alignNamedArgs, true);                                              // default: true
        formatter.options.put(formatter.alignAssignments, true);                                            // default: false
        formatter.options.put(formatter.alignEquality, false);                                              // default: false
        formatter.options.put(formatter.alignRight, true);                                                  // default: false
        // Indentation
        formatter.options.put(formatter.identSpaces, 3);                                                    // default: 3
        formatter.options.put(formatter.useTab, false);                                                     // default: false
        // Line Breaks
        formatter.options.put(formatter.breaksComma, odbtFormat.Breaks.After);                              // default: odbtFormat.Breaks.After
        formatter.options.put("commasPerLine", 1);                                                          // default: 5
        formatter.options.put(formatter.breaksConcat, odbtFormat.Breaks.Before);                            // default: odbtFormat.Breaks.Before
        formatter.options.put(formatter.breaksAroundLogicalConjunctions, odbtFormat.Breaks.Before);         // default: odbtFormat.Breaks.Before
        formatter.options.put(formatter.breakAnsiiJoin, true);                                              // default: false
        formatter.options.put(formatter.breakParenCondition, true);                                         // default: false
        formatter.options.put(formatter.breakOnSubqueries, true);                                           // default: true
        formatter.options.put(formatter.maxCharLineSize, 120);                                              // default: 128
        formatter.options.put(formatter.forceLinebreaksBeforeComment, false);                               // default: false
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, odbtFormat.BreaksX2.Keep);    // default: odbtFormat.BreaksX2.X2
        formatter.options.put(formatter.breaksAfterSelect, false);                                          // default: true
        formatter.options.put(formatter.flowControl, odbtFormat.FlowControl.IndentedActions);               // default: odbtFormat.FlowControl.IndentedActions
        // White Space
        formatter.options.put(formatter.spaceAroundOperators, true);                                        // default: true
        formatter.options.put(formatter.spaceAfterCommas, true);                                            // default: true
        formatter.options.put(formatter.spaceAroundBrackets, odbtFormat.Space.Default);                     // default: odbtFormat.Space.Default
        // Hidden, not configurable in the GUI preferences dialog of SQLDev 20.2
        formatter.options.put(formatter.breaksProcArgs, false);                                             // default: false (overridden in Arbori program based on other settings)
        formatter.options.put(formatter.adjustCaseOnly, false);                                             // default: false (set true to skip formatting)
        formatter.options.put(formatter.formatThreshold, 1);                                                // default: 1 (disables deprecated post-processing logic)
    }
    var arboriFileName = arboriPath;
    if (!"default".equals(arboriPath)) {
        arboriFileName = new javaFile(arboriPath).getAbsolutePath();
    }
    formatter.options.put(formatter.formatProgramURL, arboriFileName);                                  // default: "default" (= provided by SQLDev / SQLcl)
}

var getConfiguredFormatter = function (xmlPath, arboriPath) {
    var formatter = new odbtFormat();
    configure(formatter, xmlPath, arboriPath);
    return formatter;
}

var hasParseErrors = function (content, consoleOutput) {
    var newContent = "\n" + content; // ensure correct line number in case of an error
    var tokens = odbtLexer.parse(newContent);
    var parsed = new odbtParsed(newContent, tokens, odbtSqlEarley.getInstance(), Java.to(["sql_statements"], "java.lang.String[]"));
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
    var content = new javaString(javaFiles.readAllBytes(file));
    return content;
}

var writeFile = function (file, content) {
    javaFiles.write(file, content.getBytes());
}

var existsDirectory = function(dir) {
    var f = new javaFile(dir);
    return f.isDirectory();
}

var existsFile = function(file) {
    var f = new javaFile(file);
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
        if (path.endsWith(javaFile.separator)) {
            return currentdir + path;
        } else {
            return currentDir + javaFile.separator + path;
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
    var files = [];
    var filexIdx;
    var result = function(valid) {
        var result = {
            rootPath : rootPath,
            files: files,
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

    // If the rootPath ends with '.json', then the file is assumed to be a 
    // <config.json> instead. 
    if (rootPath.endsWith('.json')) {
        var configJson = readFile(javaPaths.get(rootPath));
        //ctx.write(JSON.stringify(configJson) + '\n');
        try {
            configJson = JSON.parse(configJson);
        } catch (err) {
            ctx.write("file " + rootPath + " is not valid JSON.\n\n");
            return result(false);
        }

        // If the data type of the <config.json> is an array, then the file is
        // assumed to be an array of file paths. Otherwise, it should be a
        // config object with could have multiple properties.
        if (Array.isArray(configJson)) {
            files = configJson;
        } else {
            extensions = configJson.extensions;
            markdownExtensions = configJson.markdownExtensions;
            xmlPath = configJson.xmlPath;
            arboriPath = configJson.arboriPath;
            files = configJson.files;
        }

        // The file paths passed in need to be converted to Java Paths
        // to work with 'readFile' correctly.
        for (filexIdx = 0; filexIdx < files.length; filexIdx++) {
            files[filexIdx] = javaPaths.get(files[filexIdx]);
        }
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
        var formatted = javaArrays.asList(formatter.format(original).split("\n"));
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
    var p = javaPattern.compile("(```\\s*sql\\s*\\n)(.+?)(\\n```)", javaPattern.DOTALL);
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
    var fileIdx;
    for (fileIdx = 0; fileIdx < files.length; fileIdx++) {
        ctx.write("Formatting file " + (fileIdx+1) + " of " + files.length + ": " + files[fileIdx].toString() + "... ");
        ctx.getOutputStream().flush();
        if (isMarkdownFile(files[fileIdx], markdownExtensions)) {
            formatMarkdownFile(files[fileIdx], formatter);
        } else {
            formatFile(files[fileIdx], formatter);
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
            if (options.files) {
                formatFiles(options.files, formatter, options.markdownExtensions);
            } else {
                var files = getFiles(options.rootPath, options.extensions);
                formatFiles(files, formatter, options.markdownExtensions);
            }
        }
    }
}

var getArgs = function(cmdLine) {
    var p = javaPattern.compile('("[^"]*")|([^ ]+)');
    var m = p.matcher(cmdLine.trim());
    var args = [];
    while (m.find()) {
        args[args.length] = m.group();
    }
    return args;
}

var unregisterTvdFormat = function() {
    var listeners = odbtCommandRegistry.getListeners(null, ctx).get(odbtSQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
    // remove all commands registered with odbtCommandRegistry.addForAllStmtsListener
    odbtCommandRegistry.removeListener(odbtSQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
    odbtCommandRegistry.clearCaches(null, ctx);
    var remainingListeners = odbtCommandRegistry.getListeners(null, ctx).get(odbtSQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE)
            .stream().map(function(l) l.getClass()).collect(javaCollectors.toSet());
    // re-register all commands except for class TvdFormat and remaining (not removed) listener classes
    for (var i in listeners) {
        if (!listeners.get(i).toString().equals("TvdFormat") && !remainingListeners.contains(listeners.get(i).getClass())) {
            odbtCommandRegistry.addForAllStmtsListener(listeners.get(i).getClass());
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
    var TvdFormat = Java.extend(odbtCommandListener, {
        handleEvent: handleEvent,
        beginEvent: beginEvent,
        endEvent: endEvent,
        toString: toString
    });
    unregisterTvdFormat();
    odbtCommandRegistry.addForAllStmtsListener(TvdFormat.class);
    ctx.write("tvdformat registered as SQLcl command.\n");
}

// main
if (args.length >= 2 && (args[1].equalsIgnoreCase("-r") || args[1].equalsIgnoreCase("--register"))) {
    registerTvdFormat();
} else {
    run(args);
}    
