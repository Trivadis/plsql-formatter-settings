package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A1_tabs extends ConfiguredTestFormatter {

    @Test
    public void replace_tabs_with_3_spaces() throws IOException {
        var input = """
                begin
                \tnull;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
