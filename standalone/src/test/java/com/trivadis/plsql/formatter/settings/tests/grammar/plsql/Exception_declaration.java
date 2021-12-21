package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Exception_declaration extends ConfiguredTestFormatter {

    @Test
    public void simple() throws IOException {
        var input = """
                declare
                e_invalid_operation
                exception;
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   e_invalid_operation exception;
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
