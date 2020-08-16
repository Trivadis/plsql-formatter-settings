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

var getFiles = function (rootPath, extensions) {
    var Collectors = Java.type("java.util.stream.Collectors");
    var Files = Java.type("java.nio.file.Files");
    var Paths = Java.type("java.nio.file.Paths");
    var files = Files.walk(Paths.get(rootPath))
        .filter(function (f) Files.isRegularFile(f)
            && extensions.stream().anyMatch(function (e) f.toString().endsWith(e))
        )
        .sorted()
        .collect(Collectors.toList());
    return files;
}

var configure = function (formatter, arboriPath) {
    var File = Java.type("java.io.File");
    var Format = Java.type("oracle.dbtools.app.Format");
    var arboriFileName = arboriPath;
    if (!"default".equals(arboriPath)) {
        arboriFileName = new File(arboriPath).getAbsolutePath();
    }
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
    // Custom Format
    formatter.options.put(formatter.formatProgramURL, arboriFileName);                              // default: "default" (= provided by SQLDev / SQLcl)
}

var getConfiguredFormatter = function (arboriPath) {
    var Format = Java.type("oracle.dbtools.app.Format")
    var formatter = new Format();
    configure(formatter, arboriPath);
    return formatter;
}

var hasParseErrors = function (content) {
    var Lexer = Java.type('oracle.dbtools.parser.Lexer');
    var Parsed = Java.type('oracle.dbtools.parser.Parsed');
    var SqlEarley = Java.type('oracle.dbtools.parser.plsql.SqlEarley')
    var tokens = Lexer.parse(content);
    var parsed = new Parsed(content, tokens, SqlEarley.getInstance(), Java.to(["sql_statements"], "java.lang.String[]"));
    var syntaxError = parsed.getSyntaxError();
    if (syntaxError != null && syntaxError.getMessage() != null) {
        ctx.write(syntaxError.getDetailedMessage());
        ctx.write(". ");
        return true;
    } 
    return false;
}

var readFile = function (file) {
    var Files = Java.type("java.nio.file.Files");
    var content = Files.readString(file);
    return content;
}

var writeFile = function (file, content) {
    var Files = Java.type("java.nio.file.Files");
    Files.writeString(file, content);
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

var printHeader = function () {
    ctx.write("\nformat.js for SQLcl 20.2\n");
    ctx.write("Copyright 2020 by Philipp Salvisberg (philipp.salvisberg@trivadis.com)\n\n");
}

var printUsage = function () {
    ctx.write("usage: script format.js <rootPath> [options]\n\n");
    ctx.write("mandatory arguments:\n");
    ctx.write("  <rootPath>     path to directory containing files to format (content will be replaced!)\n\n");
    ctx.write("options:\n");
    ctx.write("  ext=<ext>      comma separated list of file extensions to process, e.g. ext=sql,pks,pkb\n");
    ctx.write("  arbori=<file>  path to the file containing the Arbori program for custom format settings\n\n");
}

var processAndValidateArgs = function () {
    if (args.length < 2) {
        ctx.write("missing mandatory <rootPath> argument.\n\n");
        printUsage();
        return false;
    }
    rootPath = args[1];
    if (!existsDirectory(rootPath)) {
        ctx.write("directory " + rootPath + " does not exist.\n\n");
        printUsage();
        return false;
    }
    for (var i = 2; i < args.length; i++) {
        if (args[i].startsWith("ext=")) {
            var values = args[i].substring(4).split(",");
            for (var j in values) {
                extensions.add("." + values[j]);
            }
            continue;
        }
        if (args[i].startsWith("arbori=")) {
            arboriPath = args[i].substring(7);
            if (!existsFile(arboriPath)) {
                ctx.write("file " + arboriPath + " does not exist.\n\n");
                printUsage();
                return false;
            }
            continue;
        }
        ctx.write("invalid argument " + args[i] + ".\n\n");
        printUsage();
        return false;
    }
    if (extensions.size() == 0) {
        var defaults = Java.to(["sql", "prc", "fnc", "pks", "pkb", "trg", "vw", "tps", "tpb", "tbp", "plb", "pls", "rcv", "spc", "typ", "aqt", "aqp", "ctx",
        "dbl", "tab", "dim", "snp", "con", "collt", "seq", "syn", "grt", "sp", "spb", "sps", "pck", "java.lang.String[]"]);
        for (var i in defaults) {
            extensions.add("." + defaults[i]);
        }
    }
    if (arboriPath == null) {
        var suffix = "format";
        if (args[0].endsWith(".js")) {
            suffix += ".js";
        }
        arboriPath = args[0].replace(suffix, "") + "../settings/sql_developer/trivadis_custom_format.arbori"
        if (!existsFile(arboriPath)) {
            ctx.write('Warning: ' + arboriPath + ' not found, using "default" instead.\n\n');
            arboriPath = "default"; 
        }
    }
    return true;
}

// main
var ArrayList = Java.type("java.util.ArrayList");
printHeader();
var rootPath = null;
var extensions = new ArrayList();
var arboriPath = null;
if (processAndValidateArgs()) {
    var files = getFiles(rootPath, extensions);
    var formatter = getConfiguredFormatter(arboriPath);
    for (var i in files) {
        ctx.write("Formatting file " + (i+1) + " of " + files.length + ": " + files[i].toString() + "... ");
        ctx.getOutputStream().flush();
        var original = readFile(files[i])
        if (hasParseErrors(original)) {
            ctx.write("skipped.\n");
        } else {
            writeFile(files[i], formatter.format(original));
            ctx.write("done.\n");
        }
        ctx.getOutputStream().flush();
    }
}
