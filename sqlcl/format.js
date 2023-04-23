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

// SQLcl uses the Nashorn JS engine of the JDK 8/11 by default.
// As a result, this JS file must comply with ECMAScript 5.1.

// java.lang
var javaString = Java.type("java.lang.String");
var javaSystem = Java.type("java.lang.System");
// java.nio
var javaByteBuffer = Java.type("java.nio.ByteBuffer");
var javaCharset = Java.type("java.nio.charset.Charset");
var javaFiles = Java.type("java.nio.file.Files");
var javaFileSystems = Java.type("java.nio.file.FileSystems");
var javaPaths = Java.type("java.nio.file.Paths");
// java.io
var javaFile = Java.type("java.io.File");
var javaFileInputStream = Java.type("java.io.FileInputStream");
// java.util
var javaArrays = Java.type("java.util.Arrays");
var javaLogManager = Java.type("java.util.logging.LogManager");
var javaPattern = Java.type("java.util.regex.Pattern");
var javaCollectors = Java.type("java.util.stream.Collectors");
// oracle.dbtools
var javaFormat = Java.type("oracle.dbtools.app.Format");
var javaFormat$Breaks = Java.type("oracle.dbtools.app.Format$Breaks");
var javaFormat$BreaksX2 = Java.type("oracle.dbtools.app.Format$BreaksX2");
var javaFormat$Case = Java.type("oracle.dbtools.app.Format$Case");
var javaFormat$FlowControl = Java.type("oracle.dbtools.app.Format$FlowControl");
var javaFormat$InlineComments = Java.type("oracle.dbtools.app.Format$InlineComments");
var javaFormat$Space = Java.type("oracle.dbtools.app.Format$Space");
var javaPersist2XML = Java.type("oracle.dbtools.app.Persist2XML");
var javaLexer = Java.type("oracle.dbtools.parser.Lexer");
var javaParsed = Java.type("oracle.dbtools.parser.Parsed");
var javaSqlEarley = Java.type("oracle.dbtools.parser.plsql.SqlEarley");
var javaProgram = Java.type("oracle.dbtools.arbori.Program");

var getVersion = function() {
    return "23.1.0";
}

var getFiles = function (rootPath, extensions, ignoreMatcher) {
    var files;
    if (existsFile(rootPath)) {
        if (isRelevantFile(javaPaths.get(rootPath.toString()), extensions, ignoreMatcher)) {
            files = javaArrays.asList(javaPaths.get(rootPath.toString()));
        } else {
            files = [];
        }
    } else {
        files = javaFiles.walk(javaPaths.get(rootPath.toString()))
            .filter(function (f) {return javaFiles.isRegularFile(f) && isRelevantFile(f, extensions, ignoreMatcher)})
            .sorted()
            .collect(javaCollectors.toList());
    }
    return files;
}

var isRelevantFile = function (file, extensions, ignoreMatcher) {
    if (ignoreMatcher != null) {
        if (ignoreMatcher.matches(file)) {
            return false;
        }
    }
    for (var i in extensions) {
        var fileName = file.toString().toLowerCase();
        if (fileName.lastIndexOf(extensions[i]) + extensions[i].length === fileName.length) {
            return true;
        }
    }
    return false;
}

var getRelevantFiles = function (files, extensions, ignoreMatcher) {
    var relevantFiles = [];
    for (var i in files) {
        if (existsDirectory(files[i])) {
            relevantFiles.push.apply(relevantFiles, getFiles(files[i], extensions, ignoreMatcher));
        } else if (isRelevantFile(files[i], extensions, ignoreMatcher)) {
            relevantFiles.push(files[i]);
        }
    }
    return relevantFiles;
}

var configure = function (formatter, xmlPath, arboriPath) {
    if ("default" !==  xmlPath && "embedded" !== xmlPath && xmlPath != null) {
        var url = new javaFile(xmlPath).toURI().toURL();
        var options = javaPersist2XML.read(url);
        var keySet = options.keySet().stream().collect(javaCollectors.toList());
        for (var j in keySet) {
            formatter.options.put(keySet[j], options.get(keySet[j]));
        }
    } else if ("embedded" === xmlPath) {
        // Code Editor: Format
        formatter.options.put(formatter.adjustCaseOnly, false);                                             // default: false (set true to skip formatting)
        // Advanced Format: General
        formatter.options.put(formatter.kwCase, javaFormat$Case.lower);                                     // default: javaFormat.Case.UPPER
        formatter.options.put(formatter.idCase, javaFormat$Case.NoCaseChange);                              // default: javaFormat.Case.lower
        formatter.options.put(formatter.singleLineComments, javaFormat$InlineComments.CommentsUnchanged);   // default: javaFormat.InlineComments.CommentsUnchanged
        // Advanced Format: Alignment
        formatter.options.put(formatter.alignTabColAliases, false);                                         // default: true
        formatter.options.put(formatter.alignTypeDecl, true);                                               // default: true
        formatter.options.put(formatter.alignNamedArgs, true);                                              // default: true
        formatter.options.put(formatter.alignAssignments, true);                                            // default: false
        formatter.options.put(formatter.alignEquality, false);                                              // default: false
        formatter.options.put(formatter.alignRight, true);                                                  // default: false
        // Advanced Format: Indentation
        formatter.options.put(formatter.identSpaces, 3);                                                    // default: 3
        formatter.options.put(formatter.useTab, false);                                                     // default: false
        // Advanced Format: Line Breaks
        formatter.options.put(formatter.breaksComma, javaFormat$Breaks.After);                              // default: javaFormat.Breaks.After
        formatter.options.put("commasPerLine", 1);                                                          // default: 5
        formatter.options.put(formatter.breaksConcat, javaFormat$Breaks.Before);                            // default: javaFormat.Breaks.Before
        formatter.options.put(formatter.breaksAroundLogicalConjunctions, javaFormat$Breaks.Before);         // default: javaFormat.Breaks.Before
        formatter.options.put(formatter.breakAnsiiJoin, true);                                              // default: false
        formatter.options.put(formatter.breakParenCondition, true);                                         // default: false
        formatter.options.put(formatter.breakOnSubqueries, true);                                           // default: true
        formatter.options.put(formatter.maxCharLineSize, 120);                                              // default: 128
        formatter.options.put(formatter.forceLinebreaksBeforeComment, false);                               // default: false
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, javaFormat$BreaksX2.Keep);    // default: javaFormat.BreaksX2.X2
        formatter.options.put(formatter.breaksAfterSelect, false);                                          // default: true
        formatter.options.put(formatter.flowControl, javaFormat$FlowControl.IndentedActions);               // default: javaFormat.FlowControl.IndentedActions
        // Advanced Format: White Space
        formatter.options.put(formatter.spaceAroundOperators, true);                                        // default: true
        formatter.options.put(formatter.spaceAfterCommas, true);                                            // default: true
        formatter.options.put(formatter.spaceAroundBrackets, javaFormat$Space.Default);                     // default: javaFormat.Space.Default
        // Advanced Format: Hidden, not configurable in the GUI preferences dialog of SQLDev 20.4.1
        formatter.options.put(formatter.breaksProcArgs, false);                                             // default: false (overridden in Arbori program based on other settings)
        formatter.options.put(formatter.formatThreshold, 1);                                                // default: 1 (disables deprecated post-processing logic)
    }
    var arboriFileName = arboriPath;
    if ("default" !== arboriPath) {
        arboriFileName = new javaFile(arboriPath).getAbsolutePath();
    }
    formatter.options.put(formatter.formatProgramURL, arboriFileName);                                      // default: "default" (= provided by SQLDev / SQLcl)
}

var getConfiguredFormatter = function (xmlPath, arboriPath) {
    // set relative path for include directive in Arbori program to the directory of the main Arbori program
    if (arboriPath !== "default") {
        javaSystem.setProperty("dbtools.arbori.home", new javaFile(arboriPath).getParentFile().getAbsolutePath());
    }
    // now instantiate and configure the formatter
    var formatter = new javaFormat();
    configure(formatter, xmlPath, arboriPath);
    return formatter;
}

var formatInSandbox = function(formatter, original, timeout) {
    if (timeout === 0) {
        // run formatter without timeout
        return formatter.format(original);
    } else {
        // run formatter in a thread with a given timeout in seconds
        // requires SandboxedFormatter which is not available when running within SQLcl
        var javaSandboxedFormatter = Java.type("com.trivadis.plsql.formatter.sandbox.SandboxedFormatter");
        return javaSandboxedFormatter.format(formatter, original, timeout);
    }
}

var hasParseErrors = function (content, consoleOutput) {
    var newContent = "\n" + content; // ensure correct line number in case of an error
    var tokens = javaLexer.parse(newContent);
    try {
        var parsed = new javaParsed(newContent, tokens, javaSqlEarley.getInstance(), Java.to(["sql_statements"], "java.lang.String[]"));
        var syntaxError = parsed.getSyntaxError();
        if (syntaxError != null && syntaxError.getMessage() != null) {
            if (consoleOutput) {
                ctx.write(syntaxError.getDetailedMessage());
                ctx.write("... ");
            }
            return true;
        }
        return false;
    } catch (e) {
        if (consoleOutput) {
            ctx.write("Internal during parse: " + e.getMessage());
            ctx.write("... ");
        }
        return true;
    }
}

var readFile = function (file) {
    return new javaString(javaFiles.readAllBytes(file));
}

var writeFile = function (file, content, charset) {
    var writer = javaFiles.newBufferedWriter(file, charset);
    writer.write(content);
    writer.close();
}

var existsDirectory = function (dir) {
    var f = new javaFile(dir.toString());
    return f.isDirectory();
}

var existsFile = function (file) {
    var f = new javaFile(file.toString());
    return f.isFile();
}

var printVersion = function(asCommand, standalone) {
    ctx.write("Trivadis PL/SQL & SQL Formatter ");
    if (asCommand || standalone) {
        ctx.write("(tvdformat)");
    } else {
        ctx.write("(format.js)");
    }
    ctx.write(", version " + getVersion() + "\n\n");
}

var printUsage = function (asCommand, standalone) {
    printVersion(asCommand, standalone);
    if (asCommand || standalone) {
        ctx.write("usage: tvdformat <rootPath> [options]\n\n");
    } else {
        ctx.write("usage: script format.js <rootPath> [options]\n\n");
    }
    ctx.write("mandatory argument: (one of the following)\n");
    ctx.write("  <rootPath>      file or path to directory containing files to format (content will be replaced!)\n");
    ctx.write("  <config.json>   configuration file in JSON format (must end with .json)\n");
    if (!standalone) {
        ctx.write("  *               use * to format the SQLcl buffer\n");
    }
    ctx.write("\noptions:\n");
    ctx.write("  ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb\n");
    ctx.write("  mext=<ext>      comma separated list of markdown file extensions to process, e.g. ext=md,mdown\n");
    ctx.write("  xml=<file>      path to the file containing the xml file for advanced format settings\n");
    ctx.write("                  xml=default uses default advanced settings included in sqlcl\n");
    ctx.write("                  xml=embedded uses advanced settings defined in format.js\n");
    ctx.write("  arbori=<file>   path to the file containing the Arbori program for custom format settings\n");
    ctx.write("                  arbori=default uses default Arbori program included in sqlcl\n");
    ctx.write("  ignore=<file>   path to the file containing file patterns to ignore. Patterns are defined\n");
    ctx.write("                  per line. Each line represent a glob pattern. Empty lines and lines starting\n");
    ctx.write("                  with a hash sign (#) are ignored.\n");
    ctx.write("  serr=<scope>    scope of syntax errors to be reported. By default all errors are reported.\n");
    ctx.write("                  serr=none reports no syntax errors\n");
    ctx.write("                  serr=all reports all syntax errors\n");
    ctx.write("                  serr=ext reports syntax errors for files defined with ext option\n");
    ctx.write("                  serr=mext reports syntax errors for files defined with mext option\n");
    if (standalone) {
        ctx.write("  timeout=<sec>   time in seconds to wait for the completion of the formatting for a file.\n");
        ctx.write("                  the default value is 0 seconds, which means no timeout.\n");
    }
    ctx.write("  --help, -h,     print this help screen and exit\n")
    ctx.write("  --version, -v   print version and exit\n")
    if (!asCommand && !standalone) {
        ctx.write("  --register, -r  register SQLcl command tvdformat and exit\n")
    }
    ctx.write("\n");
}

var getJsPath = function () {
    // use original args array at the time when the command was registered
    return args[0].substring(0, args[0].lastIndexOf(javaFile.separator) + 1);
}

var getCdPath = function (path) {
    if (path.indexOf("/") === 0) {
        return path; // Unix, fully qualified
    } else if (path.length > 1 && path.substring(1, 2) === ":") {
        return path; // Windows, fully qualified, e.g. C:\mydir
    }
    var currentDir = ctx.getProperty("script.runner.cd_command");
    if (currentDir == null) {
        return path;
    } else {
        if (path.lastIndexOf(javaFile.separator) + javaFile.separator.length === path.length) {
            return currentDir + path;
        } else {
            return currentDir + javaFile.separator + path;
        }
    }
}

var replaceAll = function(input, pattern, replacement) {
    var p = javaPattern.compile(pattern);
    var m = p.matcher(input);
    var result = "";
    var pos = 0;
    while (m.find()) {
        result += input.substring(pos, m.start());
        result += replacement;
        pos = m.end();
    }
    if (input.length > pos) {
        result += input.substring(pos);
    }
    return result;
}

var createIgnoreMatcher = function (ignorePath) {
    var globPattern = "glob:{"
    var lines = javaFiles.readAllLines(javaPaths.get(ignorePath));
    for (var i=0; i < lines.size(); i++) {
        var line = replaceAll(lines[i].trim(), "(\\\\)", "/");
        if (line.length > 0 && line.indexOf('#') === -1) {
            if (globPattern.length > 6) {
                globPattern += ",";
            }
            globPattern += line
        }
    }
    globPattern += "}";
    return javaFileSystems.getDefault().getPathMatcher(globPattern);
}

var processAndValidateArgs = function (args) {
    var rootPath = null;
    var extArgFound = false;
    var extensions = [];
    var mextArgFound = false;
    var markdownExtensions = [];
    var xmlPath = null;
    var arboriPath = null;
    var ignorePath = null;
    var ignoreMatcher = null;
    var serr = null;
    var timeout = null;
    var files = [];
    var result = function (valid) {
        return {
            rootPath: rootPath,
            files: files,
            extensions: extensions,
            markdownExtensions: markdownExtensions,
            xmlPath: xmlPath,
            arboriPath: arboriPath,
            ignoreMatcher: ignoreMatcher,
            serr: serr,
            timeout: timeout,
            valid: valid
        };
    }

    if (args.length < 2) {
        ctx.write("missing mandatory <rootPath> argument.\n\n");
        return result(false);
    }
    rootPath = getCdPath(args[1]);
    if (rootPath !== "*" && !existsFile(rootPath) && !existsDirectory(rootPath)) {
        ctx.write("file or directory " + rootPath + " does not exist.\n\n");
        return result(false);
    }

    // If the rootPath ends with '.json', then the file is assumed to be a 
    // <config.json> instead.
    if ((rootPath.lastIndexOf('.json') + 5) === rootPath.length) {
        var configJson = readFile(javaPaths.get(rootPath));
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
            // All parameters are optional, but they need to be named as in printUsage
            // Subsequent processing relies on correctly initialized variables.
            // Wrong types in configJson may cause runtime exceptions.
            // TODO: check all parameters in configJson as for args
            if (typeof configJson.ext !== 'undefined') {
                // only applicable if rootPath is provided
                if (!Array.isArray(configJson.ext)) {
                    ctx.write("ext in " + rootPath + " is not an array.\n\n");
                }
                extensions = configJson.ext;
                extArgFound = true;
            }
            if (typeof configJson.mext !== 'undefined') {
                if (!Array.isArray(configJson.mext)) {
                    ctx.write("mext in " + rootPath + " is not an array.\n\n");
                }
                markdownExtensions = configJson.mext;
                mextArgFound = true;
            }
            if (typeof configJson.xml !== 'undefined') {
                xmlPath = configJson.xml;
            }
            if (typeof configJson.arbori !== 'undefined') {
                arboriPath = configJson.arbori;
            }
            if (typeof configJson.ignore !== 'undefined') {
                ignorePath = configJson.ignore;
            }
            if (typeof configJson.serr !== 'undefined') {
                serr = configJson.serr.toLowerCase();
            }
            if (typeof configJson.timeout !== 'undefined') {
                timeout = configJson.timeout;
            }
            if (typeof configJson.files !== 'undefined') {
                if (!Array.isArray(configJson.files)) {
                    ctx.write("files in " + rootPath + " is not an array.\n\n");
                }
                files = configJson.files;
            }
            if (typeof configJson.rootPath !== 'undefined') {
                // only applicable if files is not provided
                rootPath = getCdPath(configJson.rootPath);
                if (!existsFile(rootPath) && !existsDirectory(rootPath)) {
                    ctx.write("file or directory " + rootPath + " does not exist.\n\n");
                    return result(false);
                }
            }
            if (typeof configJson.rootPath == 'undefined' && typeof configJson.files == 'undefined') {
                ctx.write("rootPath " + " does not contain files nor rootPath.\n\n");
                return result(false);
            }
        }

        // The file paths passed in need to be converted to Java Paths
        // to work with 'readFile' correctly.
        for (var i = 0; i < files.length; i++) {
            files[i] = javaPaths.get(files[i]);
        }
    }

    for (var i = 2; i < args.length; i++) {
        if (args[i].toLowerCase().indexOf("ext=") === 0) {
            extArgFound = true;
            if (args[i].length > 4) {
                var values = args[i].substring(4).split(",");
                for (var j in values) {
                    extensions.push("." + values[j].toLowerCase());
                }
            }
            continue;
        }
        if (args[i].toLowerCase().indexOf("mext=") === 0) {
            mextArgFound = true;
            if (args[i].length > 5) {
                var values = args[i].substring(5).split(",");
                for (var j in values) {
                    markdownExtensions.push("." + values[j].toLowerCase());
                }
            }
            continue;
        }
        if (args[i].toLowerCase().indexOf("xml=") === 0) {
            xmlPath = args[i].substring(4);
            continue;
        }
        if (args[i].toLowerCase().indexOf("arbori=") === 0) {
            arboriPath = args[i].substring(7);
            continue;
        }
        if (args[i].toLowerCase().indexOf("ignore=") === 0) {
            ignorePath = args[i].substring(7);
            continue;
        }
        if (args[i].toLowerCase().indexOf("serr=") === 0) {
            serr = args[i].substring(5).toLowerCase();
            continue;
        }
        if (args[i].toLowerCase().indexOf("timeout=") === 0) {
            timeout = args[i].substring(8);
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
        extensions.push(markdownExtensions[j]);
    }
    if (xmlPath == null) {
        xmlPath = getJsPath() + "../settings/sql_developer/trivadis_advanced_format.xml"
        if (!existsFile(xmlPath)) {
            ctx.write('Warning: ' + xmlPath + ' not found, using "embedded" instead.\n\n');
            xmlPath = "embedded";
        }
    } else {
        if ("default" !== xmlPath && "embedded" !== xmlPath) {
            xmlPath = getCdPath(xmlPath);
            if (!existsFile(xmlPath)) {
                ctx.write("XML file " + xmlPath + " does not exist.\n\n");
                return result(false);
            }
        }
    }
    if (arboriPath == null) {
        arboriPath = getJsPath() + "../settings/sql_developer/trivadis_custom_format.arbori"
        if (!existsFile(arboriPath)) {
            ctx.write('Warning: ' + arboriPath + ' not found, using "default" instead.\n\n');
            arboriPath = "default";
        }
    } else {
        if ("default" !== arboriPath) {
            arboriPath = getCdPath(arboriPath);
            if (!existsFile(arboriPath)) {
                ctx.write("Arbori file " + arboriPath + " does not exist.\n\n");
                return result(false);
            }
        }
    }
    if (ignorePath != null) {
        ignorePath = getCdPath(ignorePath);
        if (!existsFile(ignorePath)) {
            ctx.write("Ignore file " + ignorePath + " does not exist.\n\n");
            return result(false);
        }
        ignoreMatcher = createIgnoreMatcher(ignorePath);
    }
    if (serr == null) {
        serr = "all";
    } else {
        if (serr !== "all" && serr !== "none" && serr !== "ext" && serr != "mext") {
            ctx.write("invalid scope '" + serr + "' for serr. Valid are: all, none, ext, mext.\n\n");
            return result(false);
        }
    }
    if (timeout == null) {
        timeout = 0;
    } else {
        if (isNaN(timeout)) {
            ctx.write("timeout must be a number.\n\n");
            return result(false);
        }
        timeout = Number(timeout);
        if (timeout < 0) {
            ctx.write("timeout cannot be less than zero.\n\n");
            return result(false);
        }
        var standalone = javaSystem.getProperty('tvdformat.standalone') != null;
        if (!standalone) {
            ctx.write("timeout is not supported in SQLcl, use the standalone formatter tvdformat.\n\n")
            return result(false);
        }
    }
    return result(true);
}

var formatBuffer = function (formatter, timeout) {
    ctx.write("Formatting SQLcl buffer... ");
    ctx.getOutputStream().flush();
    var original = ctx.getSQLPlusBuffer().getBufferSafe().getBuffer();
    if (hasParseErrors(original, true)) {
        ctx.write("skipped.\n");
    } else {
        var formatted = javaArrays.asList(formatInSandbox(formatter, original, timeout).split("\n"));
        ctx.getSQLPlusBuffer().getBufferSafe().resetBuffer(formatted);
        ctx.write("done.\n");
        ctx.write(ctx.getSQLPlusBuffer().getBufferSafe().list(false));
    }
    ctx.write("\n");
    ctx.getOutputStream().flush();
}

var isMarkdownFile = function (file, markdownExtensions) {
    for (var j in markdownExtensions) {
        var fileName = file.toString().toLowerCase();
        if (fileName.lastIndexOf(markdownExtensions[j]) + markdownExtensions[j].length === fileName.length) {
            return true;
        }
    }
    return false;
}

var detectCharset = function(content) {
    // rudimentary solution since Apache Tika cannot be used in SQLcl
    // try default character set of the OS (can be overridden via -Dfile.encoding), then UTF-8, then windows-1252
    var defaultCharsetName = javaCharset.defaultCharset().name();
    var charsetNames = ["UTF-8", defaultCharsetName, "windows-1252"];
    for (var i = 0; i < charsetNames.length; i++) {
        var cs = javaCharset.forName(charsetNames[i]);
        try {
            cs.newDecoder().decode(javaByteBuffer.wrap(content));
            return cs;
        } catch(e) {
            // ignore exception
        }
    }
    return null;
}

var formatMarkdownFile = function (file, formatter, serr, timeout) {
    var bytes = javaFiles.readAllBytes(file);
    var charset = detectCharset(bytes);
    if (charset == null) {
        ctx.write("skipped due to unknown character set.\n");
    } else {
        var original = new javaString(bytes, charset);
        var p = javaPattern.compile("(\\n```[ \\t]*sql[ \\t]*[^\\n]*\\n)(.+?)(\\n```)", javaPattern.DOTALL);
        var m = p.matcher(original);
        var result = "";
        var pos = 0;
        var consoleOutput = false;
        if (serr == "all" || serr == "mext") {
            consoleOutput = true;
        }
        var sqlBlock = 0;
        while (m.find()) {
            sqlBlock++;
            ctx.write("#" + sqlBlock + "... ");
            result += original.substring(pos, m.end(1));
            if (hasParseErrors(m.group(2), consoleOutput)) {
                ctx.write("skipped... ")
                result += original.substring(m.start(2), m.end(3));
            } else {
                ctx.write("done... ")
                result += formatInSandbox(formatter, m.group(2), timeout);
                result += original.substring(m.end(2), m.end(3));
            }
            pos = m.end(3);
        }
        if (original.length > pos) {
            result += original.substring(pos);
        }
        writeFile(file, result, charset);
        ctx.write("done.\n");
    }
}

var getLineSeparator = function (input) {
    var lineSep;
    if (input.indexOf("\r\n") !== -1) {
        lineSep = "\r\n";
    } else if (input.indexOf("\n") !== -1) {
        lineSep = "\n";
    } else {
        lineSep = javaSystem.lineSeparator();
    }
    return lineSep;
}

var formatFile = function (file, formatter, serr, timeout) {
    var bytes = javaFiles.readAllBytes(file);
    var charset = detectCharset(bytes);
    if (charset == null) {
         ctx.write("skipped due to unknown character set.\n");
    } else {
        var original = new javaString(bytes, charset);
        var consoleOutput = false;
        if (serr == "all" || serr == "ext") {
            consoleOutput = true;
        }
        if (hasParseErrors(original, consoleOutput)) {
            ctx.write("skipped.\n");
        } else {
            writeFile(file, formatInSandbox(formatter, original, timeout) + getLineSeparator(original), charset);
            ctx.write("done.\n");
        }
    }
}

var formatFiles = function (files, formatter, markdownExtensions, serr, timeout) {
    for (var i = 0; i < files.length; i++) {
        ctx.write("Formatting file " + (i + 1) + " of " + files.length + ": " + files[i].toString() + "... ");
        ctx.getOutputStream().flush();
        if (isMarkdownFile(files[i], markdownExtensions)) {
            formatMarkdownFile(files[i], formatter, serr, timeout);
        } else {
            formatFile(files[i], formatter, serr, timeout);
        }
        ctx.getOutputStream().flush();
    }
}

var enableLogging = function() {
    // enable Logging, it's disabled by default in SQLcl and standalone executable
    var loggingConfFile = javaSystem.getenv("TVDFORMAT_LOGGING_CONF_FILE");
    if (loggingConfFile != null) {
        // enable logging according java.util.logging configuration file
        if ((new javaFile(loggingConfFile)).exists()) {
            javaLogManager.getLogManager().readConfiguration(new javaFileInputStream(loggingConfFile));
        } else {
            ctx.write("\nWarning: The file '" + loggingConfFile +
                "' does not exist. Please update the environment variable TVDFORMAT_LOGGING_CONF_FILE.\n\n");
        }
    }
    // enable Arbori program debug
    var debug = javaSystem.getenv("TVDFORMAT_DEBUG");
    if (debug != null && debug.trim() === "true") {
        javaProgram.debug = true;
    }
    // enable Arbori program timing
    var timing = javaSystem.getenv("TVDFORMAT_TIMING");
    if (timing != null && timing.trim() === "true") {
        javaProgram.timing = true;
    }
}

var run = function (args) {
    enableLogging();
    var asCommand = args[0].toLowerCase() === "tvdformat";
    var standalone = javaSystem.getProperty('tvdformat.standalone') != null;
    ctx.write("\n");
    if (args.length === 2 && (args[1].toLowerCase() === '-h' || args[1].toLowerCase() === '--help')) {
        printUsage(asCommand, standalone);
    } else if (args.length == 2 && (args[1].toLowerCase() === '-v' || args[1].toLowerCase() === "--version")) {
        printVersion(asCommand, standalone);
    } else {
        var options = processAndValidateArgs(args);
        if (!options.valid) {
            printUsage(asCommand, standalone);
        } else {
            var formatter = getConfiguredFormatter(options.xmlPath, options.arboriPath);
            if (options.rootPath === "*") {
                formatBuffer(formatter, options.timeout);
            } else {
                var files;
                if (options.files.length > 0) {
                    files = getRelevantFiles(options.files, options.extensions, options.ignoreMatcher);
                } else {
                    files = getFiles(options.rootPath, options.extensions, options.ignoreMatcher);
                }
                formatFiles(files, formatter, options.markdownExtensions, options.serr, options.timeout);
            }
        }
    }
}

var getArgs = function (cmdLine) {
    var p = javaPattern.compile('("([^"]*)")|([^ ]+)');
    var m = p.matcher(cmdLine.trim());
    var args = [];
    while (m.find()) {
        args.push(m.group(3) != null ? m.group(3) : m.group(2));
    }
    return args;
}

var unregisterTvdFormat = function () {
    var listeners = javaCommandRegistry.getListeners(ctx.getBaseConnection(), ctx).get(javaSQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
    // remove all commands registered with javaCommandRegistry.addForAllStmtsListener
    javaCommandRegistry.removeListener(javaSQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
    javaCommandRegistry.clearCaches(ctx.getBaseConnection(), ctx);
    var remainingListeners = javaCommandRegistry.getListeners(ctx.getBaseConnection(), ctx).get(javaSQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE)
        .stream().map(function(l) {return l.getClass()}).collect(javaCollectors.toSet());
    // re-register all commands except for class TvdFormat and remaining (not removed) listener classes
    for (var i in listeners) {
        if (listeners.get(i).toString() !== "TvdFormat" && !remainingListeners.contains(listeners.get(i).getClass())) {
            javaCommandRegistry.addForAllStmtsListener(listeners.get(i).getClass());
        }
    }
}

var registerTvdFormat = function () {
    var handleEvent = function (conn, ctx, cmd) {
        var args = getArgs(cmd.getSql());
        if (args != null && typeof args[0] != "undefined" && args[0].toLowerCase() === "tvdformat") {
            run(args);
            return true;
        }
        return false;
    }
    var beginEvent = function (conn, ctx, cmd) {
    }
    var endEvent = function (conn, ctx, cmd) {
    }
    var toString = function () {
        // to identify this dynamically created class during unregisterTvdFormat()
        return "TvdFormat";
    }
    var TvdFormat = Java.extend(javaCommandListener, {
        handleEvent: handleEvent,
        beginEvent: beginEvent,
        endEvent: endEvent,
        toString: toString
    });
    unregisterTvdFormat();
    javaCommandRegistry.addForAllStmtsListener(TvdFormat.class);
    ctx.write("tvdformat registered as SQLcl command.\n");
}

// main
if (args.length >= 2 && (args[1].toLowerCase() === "-r" || args[1].toLowerCase() === "--register")) {
    var javaSQLCommand = Java.type("oracle.dbtools.raptor.newscriptrunner.SQLCommand");
    var javaCommandRegistry = Java.type("oracle.dbtools.raptor.newscriptrunner.CommandRegistry");
    var javaCommandListener = Java.type("oracle.dbtools.raptor.newscriptrunner.CommandListener");
    registerTvdFormat();
} else {
    run(args);
}    
