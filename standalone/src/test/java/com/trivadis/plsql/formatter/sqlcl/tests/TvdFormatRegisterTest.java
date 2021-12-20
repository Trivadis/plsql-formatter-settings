package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TvdFormatRegisterTest extends AbstractSqlclTest {

    @Test
    public void not_registered_feeback() {
        reset();
        var expected = """
                Error starting at line : 1 in command -
                tvdformat
                Error report -
                Unknown Command
                """.trim();
        var actual = runCommand("tvdformat").trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void register_feedback() {
        var expected = """
                tvdformat registered as SQLcl command.
                """;
        var actual = runScript("--register");
        Assertions.assertEquals(expected, actual);
        byteArrayOutputStream.reset();
        var actual2 = runScript("-r");
        Assertions.assertEquals(expected, actual2);
    }
}
