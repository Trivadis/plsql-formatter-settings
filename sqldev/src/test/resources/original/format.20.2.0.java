package oracle.dbtools.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptEngine;
import oracle.dbtools.arbori.GlobalMap;
import oracle.dbtools.arbori.MaterializedPredicate;
import oracle.dbtools.arbori.Program;
import oracle.dbtools.arbori.SqlProgram;
import oracle.dbtools.parser.Lexer;
import oracle.dbtools.parser.LexerToken;
import oracle.dbtools.parser.ParseNode;
import oracle.dbtools.parser.Parsed;
import oracle.dbtools.parser.Token;
import oracle.dbtools.parser.plsql.SqlEarley;
import oracle.dbtools.parser.plsql.SyntaxError;
import oracle.dbtools.raptor.utils.MessageLogging;
import oracle.dbtools.util.Service;

public class Format {
    public final String singleLineComments = "singleLineComments";
    public final String kwCase = "kwCase";
    public final String idCase = "idCase";
    public final String adjustCaseOnly = "adjustCaseOnly";
    public final String formatThreshold = "formatThreshold";
    public final String alignTabColAliases = "alignTabColAliases";
    public final String alignTypeDecl = "alignTypeDecl";
    public final String alignNamedArgs = "alignNamedArgs";
    public final String alignAssignments = "alignAssignments";
    public final String alignEquality = "alignEquality";
    public final String alignRight = "alignRight";
    public final String identSpaces = "identSpaces";
    public final String useTab = "useTab";
    public final String breaksComma = "breaksComma";
    public final String breaksProcArgs = "breaksProcArgs";
    public final String breaksConcat = "breaksConcat";
    public final String breaksAroundLogicalConjunctions = "breaksAroundLogicalConjunctions";
    public final String breaksAfterSelect = "breaksAfterSelect";
    public final String commasPerLine = "commasPerLine";
    public final String breakOnSubqueries = "breakOnSubqueries";
    public final String breakAnsiiJoin = "breakAnsiiJoin";
    public final String breakParenCondition = "breakParenCondition";
    public final String maxCharLineSize = "maxCharLineSize";
    public final String forceLinebreaksBeforeComment = "forceLinebreaksBeforeComment";
    public final String extraLinesAfterSignificantStatements = "extraLinesAfterSignificantStatements";
    public final String flowControl = "flowControl";
    public final String spaceAroundOperators = "spaceAroundOperators";
    public final String spaceAfterCommas = "spaceAfterCommas";
    public final String spaceAroundBrackets = "spaceAroundBrackets";
    public final String formatProgramURL = "formatProgramURL";
    public final Map<String, Object> options = new HashMap<String, Object>() {
        public Object put(String key, Object value) {
            Format.this.programInstance = null;
            return super.put(key, value);
        }
    };
    public int inputPos = -1;
    public int outputPos = -1;
    final String path = "/oracle/dbtools/app/";
    SqlProgram programInstance = null;
    final int unary_add_op = SqlEarley.getInstance().getSymbol("unary_add_op");
    final int compound_expression822 = SqlEarley.getInstance().getSymbol("compound_expression[8,22)");
    final int sql_statement = SqlEarley.getInstance().getSymbol("sql_statement");
    final int numeric_literal = SqlEarley.getInstance().getSymbol("numeric_literal");
    final int exp = SqlEarley.getInstance().getSymbol("\".exp.\"");
    public boolean rethrowSyntaxError = false;
    Map<Integer, Integer> posDepths = new HashMap();
    Map<Integer, String> newlinePositions = new HashMap();
    public Map<String, String> casedIds = new HashMap();
    public Map<String, Integer> maxIdLengthInScope = new HashMap();
    public Map<Integer, String> ids2scope = new HashMap();
    public Map<Integer, Integer> ids2interval = new HashMap();
    public Map<Integer, Integer> ids2adjustments = new HashMap();
    public Set<Integer> skipWSPositions = new HashSet();
    public Set<Integer> unformattedPositions = new HashSet();

    public static void main(String[] args) throws Exception {
        String input = Service.readFile(SqlEarley.class, "test.sql");
        input = Service.readFile(Format.class, "format_example.sql");
        input = Service.readFile(SqlEarley.class, "MSC_CL_PRE_PROCESS.pkgbdy");
        long t0 = System.currentTimeMillis();
        Program.timing = 10000 < input.length();
        SqlEarley.visualize = false;
        SqlEarley.main(new String[]{input});
        Format format = new Format() {
        };
        String output = format.format(input);
        if (input.length() < 100000) {
            System.out.println("----------------- output: ------------------");
            System.out.println(output);
        }

    }

    private static void checkContentMatch(String input, String output) {
        List<LexerToken> srcI = Lexer.parse(input, true);
        List<LexerToken> srcO = Lexer.parse(output, true);
        int i = 0;
        int j = 0;

        while(i < srcI.size() && j < srcO.size()) {
            LexerToken tI = (LexerToken)srcI.get(i);
            if (tI.type == Token.WS) {
                ++i;
            } else {
                LexerToken tO = (LexerToken)srcO.get(j);
                if (tO.type == Token.WS) {
                    ++j;
                } else {
                    if (!tI.content.toLowerCase().equals(tO.content.toLowerCase())) {
                        System.err.println("i=" + i + "   " + tI.content + "!=" + tO.content);
                        return;
                    }

                    ++i;
                    ++j;
                }
            }
        }

        System.out.println("*** content matches ***");
    }

    public Format() {
        this.setDefaultOptions();
    }

    public void setDefaultOptions() {
        this.options.put("singleLineComments", Format.InlineComments.CommentsUnchanged);
        this.options.put("kwCase", Format.Case.UPPER);
        this.options.put("idCase", Format.Case.lower);
        this.options.put("adjustCaseOnly", false);
        this.options.put("formatThreshold", 1);
        this.options.put("alignTabColAliases", true);
        this.options.put("alignTypeDecl", true);
        this.options.put("alignNamedArgs", true);
        this.options.put("alignEquality", false);
        this.options.put("alignAssignments", false);
        this.options.put("alignRight", false);
        this.options.put("identSpaces", 4);
        this.options.put("useTab", false);
        this.options.put("breaksComma", Format.Breaks.After);
        this.options.put("breaksProcArgs", false);
        this.options.put("breaksConcat", Format.Breaks.Before);
        this.options.put("breaksAroundLogicalConjunctions", Format.Breaks.Before);
        this.options.put("breaksAfterSelect", true);
        this.options.put("commasPerLine", 5);
        this.options.put("breakOnSubqueries", true);
        this.options.put("breakAnsiiJoin", false);
        this.options.put("breakParenCondition", false);
        this.options.put("maxCharLineSize", 128);
        this.options.put("forceLinebreaksBeforeComment", false);
        this.options.put("extraLinesAfterSignificantStatements", Format.BreaksX2.X2);
        this.options.put("flowControl", Format.FlowControl.IndentedActions);
        this.options.put("spaceAroundOperators", true);
        this.options.put("spaceAfterCommas", true);
        this.options.put("spaceAroundBrackets", Format.Space.Default);
        this.options.put("formatProgramURL", "default");
    }

    public void setOptions(Map<String, Object> changed) {
        Iterator var2 = changed.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            this.options.put(key, changed.get(key));
        }

    }

    public int getIntOption(String name) {
        return (Integer)this.options.get(name);
    }

    public int getCommasPerLine() {
        return this.getIntOption("commasPerLine");
    }

    public int indentLength() {
        return this.getIntOption("identSpaces");
    }

    public Boolean getBoolBind(String name) {
        return this.programInstance.getBoolBindVar(name);
    }

    public boolean breaksAfterComma() {
        return this.options.get("breaksComma") == Format.Breaks.After;
    }

    public boolean breaksBeforeComma() {
        return this.options.get("breaksComma") == Format.Breaks.Before;
    }

    public boolean breaksAfterLogicalConjunction() {
        return this.options.get("breaksAroundLogicalConjunctions") == Format.Breaks.After || this.options.get("breaksAroundLogicalConjunctions") == Format.Breaks.BeforeAndAfter;
    }

    public boolean breaksBeforeLogicalConjunction() {
        return this.options.get("breaksAroundLogicalConjunctions") == Format.Breaks.Before || this.options.get("breaksAroundLogicalConjunctions") == Format.Breaks.BeforeAndAfter;
    }

    public boolean breaksAfterConcat() {
        return this.options.get("breaksConcat") == Format.Breaks.After || this.options.get("breaksConcat") == Format.Breaks.BeforeAndAfter;
    }

    public boolean breaksBeforeConcat() {
        return this.options.get("breaksConcat") == Format.Breaks.Before || this.options.get("breaksConcat") == Format.Breaks.BeforeAndAfter;
    }

    public boolean breaksAfterSelectFromWhere() {
        return (Boolean)this.options.get("breaksAfterSelect");
    }

    public boolean spaceParenDefault() {
        return this.options.get("spaceAroundBrackets") == Format.Space.Default;
    }

    public boolean noSpaceBeforeOpenParen() {
        return this.options.get("spaceAroundBrackets") == Format.Space.Inside || this.options.get("spaceAroundBrackets") == Format.Space.NoSpace;
    }

    public boolean noSpaceBeforeCloseParen() {
        return this.options.get("spaceAroundBrackets") == Format.Space.Outside || this.options.get("spaceAroundBrackets") == Format.Space.NoSpace;
    }

    public boolean noSpaceAfterOpenParen() {
        return this.options.get("spaceAroundBrackets") == Format.Space.Outside || this.options.get("spaceAroundBrackets") == Format.Space.NoSpace;
    }

    public boolean noSpaceAfterCloseParen() {
        return this.options.get("spaceAroundBrackets") == Format.Space.Inside || this.options.get("spaceAroundBrackets") == Format.Space.NoSpace;
    }

    public boolean indentConditions() {
        return this.options.get("flowControl") == Format.FlowControl.IndentedConditionsActions;
    }

    public boolean indentActions() {
        return this.options.get("flowControl") == Format.FlowControl.IndentedConditionsActions || this.options.get("flowControl") == Format.FlowControl.IndentedActions;
    }

    public boolean breakAfterConditions() {
        return this.options.get("flowControl") == Format.FlowControl.SeparateConditionsActions;
    }

    public boolean breakAfterThen() {
        return this.options.get("flowControl") == Format.FlowControl.IndentedActions;
    }

    public void resetProgramInstance(String prg) {
        this.options.put("formatProgramURL", prg);
        this.programInstance = null;
    }

    public void resetProgramInstance() {
        this.programInstance = null;
    }

    public String getActiveFormatProgram() throws IOException {
        String ret = Service.readFile(Format.class, "/oracle/dbtools/app/format.prg");
        Object tmp = this.options.get("formatProgramURL");
        if (tmp != null) {
            try {
                String customURL = (String)tmp;
                if (!"default".equalsIgnoreCase(customURL)) {
                    ret = Service.readFile(customURL);
                }
            } catch (Exception var4) {
                System.out.println("Failed to read custom formatting program " + tmp.toString());
                System.out.println(var4.getMessage());
            }
        }

        return ret;
    }

    public synchronized String format(String input) throws IOException {
        this.checkValidity();
        this.initCallbackScaffolds();
        StringBuilder output = new StringBuilder();
        this.outputPos = -1;
        Parsed target = null;
        if (this.programInstance == null) {
            String formaterArboriCode = this.getActiveFormatProgram();
            if (Thread.currentThread().getStackTrace()[2].getMethodName().equals("doPost")) {
                this.programInstance = new SqlProgram(formaterArboriCode, this) {
                    public Boolean getBoolBindVar(String name) {
                        Object value = Format.this.options.get(name);
                        return value != null && value instanceof Boolean ? (Boolean)value : super.getBoolBindVar(name);
                    }

                    protected ScriptEngine getEngine() {
                        return null;
                    }

                    protected GlobalMap getGlobals() {
                        return null;
                    }
                };
            } else {
                this.programInstance = new SqlProgram(formaterArboriCode, this) {
                    public Boolean getBoolBindVar(String name) {
                        Object value = Format.this.options.get(name);
                        return value != null && value instanceof Boolean ? (Boolean)value : super.getBoolBindVar(name);
                    }
                };
            }
        } else {
            this.programInstance.setStruct(this);
        }

        List fullCode;
        try {
            fullCode = Lexer.parse(input);
            String rootPayload = "sql_statements";
            if (!this.rethrowSyntaxError) {
                rootPayload = "parse with errors";
            }

            target = new Parsed(input, fullCode, SqlEarley.getInstance(), new String[]{rootPayload});
            if (this.rethrowSyntaxError && target.getSyntaxError() != null) {
                throw target.getSyntaxError();
            }

            this.programInstance.eval(target, this);
            if (this.inputPos < 1) {
                this.outputPos = output.length();
            }
        } catch (SyntaxError var22) {
            this.outputPos = var22.end;
            if (this.rethrowSyntaxError) {
                throw var22;
            }

            return input;
        }

        fullCode = Lexer.parse(target.getInput(), true);
        int pos = -1;
        String priorIdent = null;
        LexerToken prior = null;
        int cumulativeChars = 0;
        int lastNewLine = -1;
        ParseNode root = target.getRoot();
        if (root.children().size() == 0) {
            return input;
        } else {
            Iterator var11 = fullCode.iterator();

            while(true) {
                while(var11.hasNext()) {
                    LexerToken t = (LexerToken)var11.next();
                    String prior_end2t_begin;
                    ParseNode node;
                    if (this.options.get("adjustCaseOnly").equals(Boolean.TRUE)) {
                        if (t.type != Token.COMMENT && t.type != Token.LINE_COMMENT && t.type != Token.WS && t.type != Token.INCOMPLETE) {
                            ++pos;
                        }

                        prior_end2t_begin = t.content;
                        if (t.type == Token.IDENTIFIER) {
                            prior_end2t_begin = this.adjustCase(prior_end2t_begin, this.options.get("kwCase"));
                            node = target.getRoot().leafAtPos(pos);
                            if (node != null && this.casedIds.containsKey(node.interval())) {
                                prior_end2t_begin = (String)this.casedIds.get(node.interval());
                            }
                        }

                        output.append(prior_end2t_begin);
                    } else {
                        if ("inner".equalsIgnoreCase(t.content)) {
                            t = t;
                        }

                        if (this.outputPos < 0 && this.inputPos <= t.begin) {
                            this.outputPos = t.begin;
                        }

                        if ((!this.unformattedPositions.contains(pos) || !this.unformattedPositions.contains(pos + 1)) && (root.topLevel == null || root.coveredByOnTopLevel(pos + 1) != null)) {
                            if (t.type == Token.WS) {
                                if ("\n".equals(t.content)) {
                                    lastNewLine = t.end;
                                }
                            } else {
                                prior_end2t_begin = "";
                                if (prior != null) {
                                    prior_end2t_begin = target.getInput().substring(prior.end, t.begin);
                                }

                                if (t.type != Token.COMMENT && t.type != Token.LINE_COMMENT && t.type != Token.MACRO_SKIP && t.type != Token.SQLPLUSLINECONTINUE_SKIP && t.type != Token.INCOMPLETE) {
                                    ++pos;
                                    node = target.getRoot().leafAtPos(pos);
                                    String ident = (String)this.newlinePositions.get(pos);
                                    String word;
                                    if (ident != null) {
                                        if (prior != null && (this.options.get("extraLinesAfterSignificantStatements") == Format.BreaksX2.Keep && ident.startsWith("\n\n") || prior.type == Token.LINE_COMMENT || prior.type == Token.COMMENT)) {
                                            if (prior != null) {
                                                output.append(prior_end2t_begin);
                                            }

                                            if (ident.charAt(0) == '\n' && prior_end2t_begin.contains("\n")) {
                                                ident = ident.substring(1);
                                            }

                                            if (0 < ident.length() && ident.charAt(0) == '\n' && prior_end2t_begin.contains("\n")) {
                                                ident = ident.substring(1);
                                            }
                                        }

                                        output.append(ident);
                                        priorIdent = ident;
                                        if (ident.contains("\n")) {
                                            cumulativeChars = ident.length() - 1;
                                        } else {
                                            cumulativeChars += ident.length() - 1;
                                        }
                                    } else {
                                        if (prior != null && prior.type == Token.LINE_COMMENT) {
                                            if (priorIdent != null) {
                                                output.append(priorIdent);
                                            } else {
                                                output.append("\n");
                                            }
                                        }

                                        word = (String)this.ids2scope.get(pos);
                                        if (null != word) {
                                            word = this.nullifySingletonIds(word);
                                        }

                                        if (null != word) {
                                            int maxLen = (Integer)this.maxIdLengthInScope.get(word);
                                            LexerToken startToken = (LexerToken)target.getSrc().get((Integer)this.ids2interval.get(pos));
                                            Integer adj = (Integer)this.ids2adjustments.get(pos);
                                            int idLength = prior.end - startToken.begin;
                                            int pad = maxLen + 1 - idLength;
                                            if (adj != null) {
                                                pad -= adj;
                                            }

                                            if (pad < 1) {
                                                pad = 1;
                                            }

                                            output.append(Service.padln("", pad));
                                        } else {
                                            String separator = this.decideSpace(target, pos);
                                            if ("/".equals(t.content) && node.parent() != null && node.parent().contains(this.sql_statement)) {
                                                separator = "\n";
                                                cumulativeChars = 0;
                                            }

                                            if ((Integer)this.options.get("maxCharLineSize") < cumulativeChars && t.type != Token.OPERATION) {
                                                if (priorIdent != null) {
                                                    separator = priorIdent;
                                                }

                                                if (separator.startsWith("\n\n")) {
                                                    separator = separator.substring(1);
                                                }

                                                if (separator.startsWith("\n\r\n")) {
                                                    separator = separator.substring(2);
                                                }

                                                if (!separator.contains("\n")) {
                                                    separator = "\n" + separator;
                                                }

                                                cumulativeChars = separator.length() - 1;
                                            } else {
                                                cumulativeChars += separator.length();
                                            }

                                            output.append(separator);
                                        }
                                    }

                                    word = t.content;
                                    if (t.type == Token.IDENTIFIER) {
                                        word = this.adjustCase(word, this.options.get("kwCase"));
                                    }

                                    if (node != null && this.casedIds.containsKey(node.interval())) {
                                        word = (String)this.casedIds.get(node.interval());
                                    }

                                    output.append(word);
                                    cumulativeChars += word.length();
                                    prior = t;
                                } else {
                                    if ((Boolean)this.options.get("forceLinebreaksBeforeComment") && (t.type == Token.COMMENT || t.type == Token.LINE_COMMENT) && prior != null && lastNewLine < prior.end) {
                                        output.append("\n    ");
                                    }

                                    output.append(prior_end2t_begin);
                                    if (t.content.endsWith("\r")) {
                                        t.content = t.content.substring(0, t.content.length() - 1) + '\n';
                                    }

                                    String pureContent = t.content;
                                    boolean endsWNL = false;
                                    if (pureContent.endsWith("\n")) {
                                        pureContent = pureContent.substring(0, pureContent.length() - 1);
                                        endsWNL = true;
                                    }

                                    if (pureContent.startsWith("--")) {
                                        pureContent = pureContent.substring(2);
                                    } else if (pureContent.startsWith("/*") && pureContent.endsWith("*/")) {
                                        pureContent = pureContent.substring(2, pureContent.length() - 2);
                                    }

                                    if (!pureContent.contains("\n")) {
                                        if (this.options.get("singleLineComments") == Format.InlineComments.MultiLine) {
                                            output.append("/*" + pureContent + "*/");
                                            if (endsWNL) {
                                                output.append("\n");
                                                cumulativeChars = 0;
                                            }
                                        } else if (this.options.get("singleLineComments") == Format.InlineComments.SingleLine) {
                                            output.append("--" + pureContent);
                                        } else {
                                            output.append(t.content);
                                        }
                                    } else {
                                        output.append(t.content);
                                    }

                                    prior = t;
                                }
                            }
                        } else {
                            output.append(t.content);
                            if (t.type != Token.WS && t.type != Token.INCOMPLETE && t.type != Token.COMMENT && t.type != Token.LINE_COMMENT && t.type != Token.MACRO_SKIP && t.type != Token.SQLPLUSLINECONTINUE_SKIP) {
                                ++pos;
                            }

                            prior = t;
                        }
                    }
                }

                if (pos < (Integer)this.options.get("formatThreshold")) {
                    return input;
                }

                String ret = output.toString();
                if (ret.startsWith("\n")) {
                    ret = ret.substring(1);
                }

                if (this.options.get("extraLinesAfterSignificantStatements") != Format.BreaksX2.Keep) {
                    ret = ret.replace("\n\n\n", "\n\n");
                }

                int index = -1;

                while(true) {
                    index = ret.indexOf(";/", index + 1);
                    if (index < 0) {
                        this.inputPos = -1;
                        return ret;
                    }

                    if (ret.indexOf("*", index + 1) != index + 2) {
                        ret = ret.substring(0, index) + ";\n/" + ret.substring(index + 2);
                    }
                }
            }
        }
    }

    private void checkValidity() throws IOException {
        String formaterArboriCode = this.getActiveFormatProgram();
        boolean oK = this.checkIfContains(formaterArboriCode, "skipWhiteSpaceBeforeNode:");
        if (oK) {
            this.checkIfContains(formaterArboriCode, ":indentConditions");
        }

    }

    private boolean checkIfContains(String formaterArboriCode, String substr) {
        boolean oK = true;
        if (formaterArboriCode.indexOf(substr) < 0) {
            oK = false;
        }

        if (!oK) {
            MessageLogging.getInstance().log("Mising " + substr + "; had to reset format program");
            this.resetProgramInstance();
            this.options.put("formatProgramURL", "default");
        }

        return oK;
    }

    private String nullifySingletonIds(String scope) {
        int cnt = 0;
        Iterator var3 = this.ids2scope.values().iterator();

        while(var3.hasNext()) {
            String sc = (String)var3.next();
            if (sc.equals(scope)) {
                ++cnt;
            }
        }

        return cnt > 1 ? scope : null;
    }

    private String decideSpace(Parsed target, int pos) {
        if (pos == 0) {
            return "";
        } else {
            return this.skipWSPositions.contains(pos) ? "" : " ";
        }
    }

    public String adjustCase(String word, Object changeCase) {
        if (!word.startsWith("\"") && changeCase == Format.Case.lower) {
            word = word.toLowerCase();
        }

        if (!word.startsWith("\"") && changeCase == Format.Case.UPPER) {
            word = word.toUpperCase();
        }

        if (!word.startsWith("\"") && changeCase == Format.Case.InitCap) {
            char[] converted = new char[word.length()];
            char prior1 = '%';

            for(int i = 0; i < converted.length; ++i) {
                char current = word.charAt(i);
                if (i != 0 && prior1 != '_') {
                    converted[i] = Character.toLowerCase(current);
                } else {
                    converted[i] = Character.toUpperCase(current);
                }

                prior1 = current;
            }

            word = new String(converted);
        }

        return word;
    }

    String spaceSequence(int level) {
        String output = "";
        int i;
        if ((Boolean)this.options.get("useTab")) {
            for(i = 0; i < level; ++i) {
                output = output + "\t";
            }
        } else {
            for(i = 0; i < level * (Integer)this.options.get("identSpaces"); ++i) {
                output = output + " ";
            }
        }

        return output;
    }

    public void putNewline(int pos, String newline) {
        this.newlinePositions.put(pos, newline);
    }

    public String getNewline(int pos) {
        return (String)this.newlinePositions.get(pos);
    }

    private void initCallbackScaffolds() {
        this.posDepths = new HashMap();
        this.newlinePositions = new HashMap();
        this.casedIds = new HashMap();
        this.maxIdLengthInScope = new HashMap();
        this.ids2scope = new HashMap();
        this.ids2interval = new HashMap();
        this.ids2adjustments = new HashMap();
        this.skipWSPositions = new HashSet();
        this.unformattedPositions = new HashSet();
    }

    public void closestPrecursorDescendant(Parsed target, Map<String, ParseNode> tuple) {
        String m = (new Object() {
        }).getClass().getEnclosingMethod().getName();
        System.out.println(m + ".  " + MaterializedPredicate.tupleMnemonics(tuple, target.getSrc()));
    }

    public void ancestorDescendant(Parsed target, Map<String, ParseNode> tuple) {
        String m = (new Object() {
        }).getClass().getEnclosingMethod().getName();
        System.out.println(m + ".  " + MaterializedPredicate.tupleMnemonics(tuple, target.getSrc()));
    }

    public void indentedNodes1(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        if (node == null) {
            throw new AssertionError("node == null");
        } else {
            for(int i = node.from; i < node.to; ++i) {
                Integer posDepth = (Integer)this.posDepths.get(i);
                if (posDepth == null) {
                    posDepth = 0;
                }

                posDepth = posDepth + 1;
                this.posDepths.put(i, posDepth);
            }

        }
    }

    public void indentedNodes2(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        int pos = node.from;
        Integer depth = this.depth(pos);
        int priorDepth = -1;
        if (0 < pos) {
            priorDepth = this.depth(pos - 1);
        }

        if (priorDepth != depth) {
            this.newlinePositions.put(pos, "\n" + this.spaceSequence(depth));
        }

        if (target.getSrc().size() > node.to) {
            pos = node.to;
            this.newlinePositions.put(pos, "\n" + this.spaceSequence(this.depth(pos)));
        }
    }

    private int depth(int pos) {
        Integer ret = (Integer)this.posDepths.get(pos);
        return ret == null ? 0 : ret;
    }

    public void identifiers(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("identifier");
        String id = ((LexerToken)target.getSrc().get(node.from)).content;
        id = this.adjustCase(id, this.options.get("idCase"));
        this.casedIds.put(node.interval(), id);
    }

    public void paddedIdsInScope(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode scope = (ParseNode)tuple.get("scope");
        Integer maxLen = (Integer)this.maxIdLengthInScope.get(scope.interval());
        if (maxLen == null) {
            maxLen = 0;
        }

        ParseNode id = (ParseNode)tuple.get("predecessor");
        ParseNode id1 = (ParseNode)tuple.get("follower");
        int from = id.from;
        int indentOffset = 0;
        String indent0 = (String)this.newlinePositions.get(id.from);

        int idLen;
        for(idLen = id.to - 1; id.from <= idLen; --idLen) {
            String indent = (String)this.newlinePositions.get(idLen);
            if (indent != null) {
                from = idLen;
                if (indent0 != null && indent0.length() < indent.length()) {
                    indentOffset += indent.length() - indent0.length();
                }
                break;
            }
        }

        idLen = 0;

        for(int i = from; i < id1.from; ++i) {
            idLen += ((LexerToken)target.getSrc().get(i)).content.length();
            ++idLen;
        }

        idLen += indentOffset;
        if (maxLen < idLen) {
            maxLen = idLen;
        }

        this.maxIdLengthInScope.put(scope.interval(), maxLen);
        this.ids2scope.put(id1.from, scope.interval());
        this.ids2interval.put(id1.from, from);
        this.ids2adjustments.put(id1.from, indentOffset);
    }

    public void incrementalAlignments(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        String ident = this.offset(node.from, target);

        for(int i = node.from + 1; i < node.to; ++i) {
            String oldOffset = (String)this.newlinePositions.get(i);
            if (oldOffset != null) {
                this.newlinePositions.put(i, oldOffset + Service.padln("", ident.length() - this.indent(node.from, target)));
            }
        }

    }

    private int indent(int pos, Parsed target) {
        for(int i = pos; pos - 50 < i & 0 <= i; --i) {
            String tmp = (String)this.newlinePositions.get(i);
            if (tmp != null) {
                return tmp.length();
            }
        }

        return 1;
    }

    public void pairwiseAlignments(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        ParseNode predecessor = (ParseNode)tuple.get("predecessor");
        String nodeIndent = (String)this.newlinePositions.get(node.from);
        if (nodeIndent != null) {
            String ident = this.offset(predecessor.from, target);
            int delta = ident.length() - nodeIndent.length();
            this.newlinePositions.put(node.from, ident);
            if (0 < delta) {
                for(int i = node.from + 1; i < node.to; ++i) {
                    String oldOffset = (String)this.newlinePositions.get(i);
                    if (oldOffset != null) {
                        String newOffset = oldOffset + Service.padln("", delta);
                        this.newlinePositions.put(i, newOffset);
                    }
                }
            }

        }
    }

    private String offset(int pos, Parsed target) {
        String ret = "\n";

        for(int i = pos; pos - 50 < i & 0 <= i; --i) {
            String tmp = (String)this.newlinePositions.get(i);
            if (tmp != null && tmp.startsWith("\n\n")) {
                tmp = tmp.substring(1);
            }

            if (i == 0) {
                tmp = "\n";
            }

            if (tmp != null) {
                ret = tmp;

                for(int j = i; j < pos; ++j) {
                    for(int k = 0; k < ((LexerToken)target.getSrc().get(j)).content.length(); ++k) {
                        ret = ret + ' ';
                    }

                    ret = ret + this.decideSpace(target, j + 1);
                    if ((Integer)this.options.get("maxCharLineSize") < ret.length()) {
                        return ret;
                    }
                }

                return ret;
            }
        }

        return ret;
    }

    public void rightAlignments(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        String kw = ((LexerToken)target.getSrc().get(node.from)).content;
        int delta = "select".length() - kw.length();
        String nodeIndent = (String)this.newlinePositions.get(node.from);
        if (nodeIndent != null) {
            this.newlinePositions.put(node.from, nodeIndent + Service.padln("", delta));
        }
    }

    public void extraBrkBefore(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        String padding = (String)this.newlinePositions.get(node.from);
        if (padding == null) {
            int depth = this.depth(node.from);
            this.newlinePositions.put(node.from, "\n" + this.spaceSequence(depth));
        }

    }

    public void extraBrkAfter(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        String padding = (String)this.newlinePositions.get(node.to);
        if (padding == null) {
            int depth = this.depth(node.to);
            this.newlinePositions.put(node.to, "\n" + this.spaceSequence(depth));
        }

    }

    public void brkX2(Parsed target, Map<String, ParseNode> tuple) {
        if (this.options.get("extraLinesAfterSignificantStatements") != Format.BreaksX2.X1) {
            ParseNode node = (ParseNode)tuple.get("node");
            String padding = (String)this.newlinePositions.get(node.to);
            if (padding == null) {
                this.newlinePositions.put(node.to, "\n\n");
            } else {
                this.newlinePositions.put(node.to, "\n" + padding);
            }

        }
    }

    public void ignoreLineBreaksBeforeNode(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        this.newlinePositions.remove(node.from);
    }

    public void ignoreLineBreaksAfterNode(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        this.newlinePositions.remove(node.to);
    }

    public void dontFormatNode(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");

        for(int i = node.from; i < node.to; ++i) {
            this.unformattedPositions.add(i);
        }

    }

    public void skipWhiteSpaceBeforeNode(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        this.skipWSPositions.add(node.from);
    }

    public void skipWhiteSpaceAfterNode(Parsed target, Map<String, ParseNode> tuple) {
        ParseNode node = (ParseNode)tuple.get("node");
        this.skipWSPositions.add(node.to);
    }

    public static enum FlowControl {
        IndentedActions,
        Terse,
        SeparateConditionsActions,
        IndentedConditionsActions;

        private FlowControl() {
        }
    }

    public static enum InlineComments {
        CommentsUnchanged,
        SingleLine,
        MultiLine;

        private InlineComments() {
        }
    }

    public static enum BreaksX2 {
        X1,
        X2,
        Keep;

        private BreaksX2() {
        }
    }

    public static enum Breaks {
        Before,
        After,
        None,
        BeforeAndAfter;

        private Breaks() {
        }
    }

    public static enum Space {
        Default,
        Inside,
        Outside,
        NoSpace;

        private Space() {
        }
    }

    public static enum Case {
        UPPER,
        lower,
        InitCap,
        NoCaseChange;

        private Case() {
        }
    }
}
