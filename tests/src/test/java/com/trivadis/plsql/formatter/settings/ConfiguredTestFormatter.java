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
    protected final Format formatter;

    public ConfiguredTestFormatter() {
        super();
        System.setProperty("polyglot.engine.WarnInterpreterOnly","false");
        setArboriHome();
        loadLoggingConf();
        formatter = new Format();
        configureFormatter();
    }

    private void setArboriHome() {
        var arboriDir = new File(getArboriFileName()).getParentFile().getAbsolutePath();
        System.setProperty("dbtools.arbori.home", arboriDir);
    }

    private void loadLoggingConf() {
        var manager = LogManager.getLogManager();
        try {
            manager.readConfiguration(Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.conf"));
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getOptions() {
        Map<String, Object> map;
        try {
            URL advancedFormat = Thread.currentThread().getContextClassLoader().getResource("trivadis_advanced_format.xml"); // symbolic link
            assert advancedFormat != null;
            map = Persist2XML.read(advancedFormat);
        } catch (IOException e) {
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
            formatter.options.put(key, map.get(key));
        }
        formatter.options.put(formatter.formatProgramURL, getArboriFileName());
    }

    public Format getFormatter() {
        return formatter;
    }

    public void resetOptions() {
        configureFormatter();
    }

    public void formatAndAssert(CharSequence expected) {
        formatAndAssert(expected, false);
    }

    public void formatAndAssert(CharSequence expected, boolean resetOptions) {
        try {
            var expectedTrimmed = expected.toString().trim();
            var actual = formatter.format(expectedTrimmed);
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
