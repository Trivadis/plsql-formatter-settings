package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A2_trailing_spaces extends ConfiguredTestFormatter {

    @Test
    public void remove_trailing_spaces() throws IOException {
        var input = """
                begin\s\s\s
                null;\s\s\s\t
                end;\s\s\s
                /\s\s\s
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
