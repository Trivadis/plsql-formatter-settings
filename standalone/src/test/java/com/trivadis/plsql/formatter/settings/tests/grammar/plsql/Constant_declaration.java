package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Constant_declaration extends ConfiguredTestFormatter {

    @Test
    public void tokenized() throws IOException {
        var input = """
                declare
                co_true
                integer
                not
                null
                :
                =
                1
                ;
                begin
                null
                ;
                end
                ;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   co_true integer not null :=
                      1;
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
