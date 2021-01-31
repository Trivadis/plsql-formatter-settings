package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.Assert;
import org.junit.Test;

public class TvdFormatRegisterTest extends AbstractSqlclTest {

    @Test
    public void not_registered_feeback() {
        reset();
        final String expected =
            """
            Error starting at line : 1 in command -
            tvdformat
            Error report -
            Unknown Command
            """.trim();
        final String actual = runCommand("tvdformat").trim();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void register_feedback() {
        final String expected =
            """
            tvdformat registered as SQLcl command.
            """;
        final String actual = runScript("--register");
        Assert.assertEquals(expected, actual);
        byteArrayOutputStream.reset();
        final String actual2 = runScript("-r");
        Assert.assertEquals(expected, actual2);
    }

}
