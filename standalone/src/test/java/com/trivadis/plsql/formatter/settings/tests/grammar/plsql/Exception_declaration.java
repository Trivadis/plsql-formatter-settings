package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
