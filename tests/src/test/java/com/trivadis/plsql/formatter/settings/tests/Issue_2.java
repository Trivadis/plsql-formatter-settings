package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class Issue_2 extends ConfiguredTestFormatter {
    private final String input =
        """
        select * from dual;
        
        -- test comment
        select * from dual;
        """;

    private final String expected =
        """
        select *
          from dual;
        
        -- test comment
        select *
          from dual;
        """.trim();

    public String windows(final CharSequence input) {
        return macos(input.toString()).replace("\n", "\r\n");
    }

    public String macos(final CharSequence input) {
        return input.toString().replace("\r", "");
    }

    @Test
    public void macos() throws IOException {
        //formatter.options.put(formatter.extraLinesAfterSignificantStatements, Format.BreaksX2.Keep)
        final String actual = getFormatter().format(macos(input));
        Assert.assertEquals(macos(expected), macos(actual));
        final String actual2 = getFormatter().format(macos(actual));
        Assert.assertEquals(macos(expected), macos(actual2));
    }

    @Test
    public void windows() throws IOException {
        //formatter.options.put(formatter.extraLinesAfterSignificantStatements, Format.BreaksX2.Keep)
        final String actual = getFormatter().format(windows(input));
        Assert.assertEquals(windows(expected), windows(actual));
        final String actual2 = getFormatter().format(windows(actual));
        Assert.assertEquals(windows(expected), windows(actual2));
    }

}
