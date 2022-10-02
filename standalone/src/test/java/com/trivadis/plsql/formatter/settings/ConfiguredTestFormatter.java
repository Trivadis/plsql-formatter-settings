package com.trivadis.plsql.formatter.settings;

import oracle.dbtools.app.Format;
import oracle.dbtools.app.Persist2XML;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.LogManager;

public abstract class ConfiguredTestFormatter {
    private static Format formatter;

    public ConfiguredTestFormatter() {
        super();
        if (getFormatter() == null) {
            System.setProperty("polyglot.engine.WarnInterpreterOnly","false");
            setArboriHome();
            loadLoggingConf();
            formatter = new Format();
            configureFormatter();
        } else {
            // Format (first tab)
            setOption(getFormatter().adjustCaseOnly, false);
            // General - Advanced Format
            setOption(getFormatter().kwCase, Format.Case.lower);
            setOption(getFormatter().idCase, Format.Case.NoCaseChange);
            setOption(getFormatter().singleLineComments, Format.InlineComments.CommentsUnchanged);
            // Alignment - Advanced Format
            setOption(getFormatter().alignTabColAliases, false);
            setOption(getFormatter().alignTypeDecl, true);
            setOption(getFormatter().alignNamedArgs, true);
            setOption(getFormatter().alignAssignments, true);
            setOption(getFormatter().alignEquality, false);
            setOption(getFormatter().alignRight, true);
            // Indentation - Advanced Format
            setOption(getFormatter().identSpaces, 3);
            setOption(getFormatter().useTab, false);
            // Line Breaks - Advanced Format
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().commasPerLine, 1); // irrelevant
            setOption(getFormatter().breaksConcat, Format.Breaks.Before);
            setOption(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.Before);
            setOption(getFormatter().breakAnsiiJoin, true);
            setOption(getFormatter().breakParenCondition, true);
            setOption(getFormatter().breakOnSubqueries, true);
            setOption(getFormatter().maxCharLineSize, 120);
            setOption(getFormatter().forceLinebreaksBeforeComment, false);
            setOption(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X1);
            setOption(getFormatter().breaksAfterSelect, false);
            setOption(getFormatter().flowControl, Format.FlowControl.IndentedActions);
            // White Space - Advanced Format
            setOption(getFormatter().spaceAroundOperators, true);
            setOption(getFormatter().spaceAfterCommas, true);
            setOption(getFormatter().spaceAroundBrackets, Format.Space.Default);
            // Undocumented
            setOption(getFormatter().formatThreshold, 1); // don't format input with less than threshold tokens
            setOption(getFormatter().formatWhenSyntaxError, false);
            // Custom
            setOption("keepQuotedIdentifiers", false);
        }
    }

    private void setArboriHome() {
        var arboriDir = new File(getArboriFileName()).getParentFile().getAbsolutePath();
        System.setProperty("dbtools.arbori.home", arboriDir);
    }

    private void loadLoggingConf() {
        var disableLogging = System.getProperty("disable.logging");
        var manager = LogManager.getLogManager();
        manager.reset();
        if (disableLogging != null && !disableLogging.trim().equalsIgnoreCase("true")) {
            try {
                manager.readConfiguration(Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.conf"));
            } catch (SecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Object> getOptions() {
        Map<String, Object> map;
        try {
            URL advancedFormat = Thread.currentThread().getContextClassLoader().getResource("trivadis_advanced_format.xml"); // symbolic link
            assert advancedFormat != null;
            map = Persist2XML.read(advancedFormat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private String getArboriFileName() {
        var customFormat = Thread.currentThread().getContextClassLoader().getResource("trivadis_custom_format.arbori"); // symbolic link
        assert customFormat != null;
        return customFormat.getFile();
    }

    private void configureFormatter() {
        var map = getOptions();
        for (String key : map.keySet()) {
            setOption(key, map.get(key));
        }
        setOption(getFormatter().formatProgramURL, getArboriFileName());
    }

    public void setOption(String key, Object value) {
        // do not use put method to keep Format.programInstance
        getFormatter().options.remove(key);
        getFormatter().options.putIfAbsent(key, value);
    }

    public Format getFormatter() {
        return formatter;
    }

    public void resetOptions() {
        formatter = new Format();
        configureFormatter();
    }

    public void formatAndAssert(CharSequence expected) {
        formatAndAssert(expected, false);
    }

    public void formatAndAssert(CharSequence expected, boolean resetOptions) {
        try {
            var expectedTrimmed = expected.toString().trim();
            var actual = getFormatter().format(expectedTrimmed);
            Assertions.assertEquals(expectedTrimmed, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (resetOptions) {
                resetOptions();
            }
        }
    }

    public void assertEquals(CharSequence expected, CharSequence actual) {
        Assertions.assertEquals(expected.toString().trim().replaceAll("\r", ""), actual.toString().replaceAll("\r", ""));
    }
}
