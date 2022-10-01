package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Close_statement extends ConfiguredTestFormatter {

    @Test
    public void close() throws IOException {
        var input = """
                begin
                close
                c_cursor
                ;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   close c_cursor;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
