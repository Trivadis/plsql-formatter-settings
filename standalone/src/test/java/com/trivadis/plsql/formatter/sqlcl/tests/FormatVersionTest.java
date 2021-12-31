package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormatVersionTest extends AbstractSqlclTest {
    final String expected = """
                            
            Trivadis PL/SQL & SQL Formatter (format.js), version XYZ
                            
            """;
    @Test
    public void longArgument() {
        var actual = runScript("--version");
        Assertions.assertEquals(expected, actual.replaceAll(", version [^\\s]+", ", version XYZ"));
    }

    @Test
    public void shortArgument() {
        var actual = runScript("-v");
        Assertions.assertEquals(expected, actual.replaceAll(", version [^\\s]+", ", version XYZ"));
    }
}
