package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TvdFormatVersionTest extends AbstractSqlclTest {
    final String expected = """
                            
            Trivadis PL/SQL & SQL Formatter (tvdformat), version XYZ
                            
            """;

    @BeforeEach
    public void registerCommandBeforeTest() {
        runScript("--register");
        byteArrayOutputStream.reset();
    }

    @Test
    public void longArgument() {
        var actual = runCommand("tvdformat --version");
        Assertions.assertEquals(expected, actual.replaceAll(", version [^\\s]+", ", version XYZ"));
    }

    @Test
    public void shortArgument() {
        var actual = runCommand("tvdformat -v");
        Assertions.assertEquals(expected, actual.replaceAll(", version [^\\s]+", ", version XYZ"));
    }
}
