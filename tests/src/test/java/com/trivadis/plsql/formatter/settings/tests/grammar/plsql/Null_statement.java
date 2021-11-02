package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Null_statement extends ConfiguredTestFormatter {

    @Test
    public void tokenized_upper_null() throws IOException {
        var input = """
                begin
                NULL
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   null;
                end;
                """;
        assertEquals(expected, actual);
    }
}
