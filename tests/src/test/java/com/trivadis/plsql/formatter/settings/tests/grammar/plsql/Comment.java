package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Comment extends ConfiguredTestFormatter {

    @Test
    public void single_line() throws IOException {
        var input = """
                begin
                -- singe line comment
                null;  -- another single line comment
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   -- singe line comment
                   null;  -- another single line comment
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void mutli_line() throws IOException {
        var input = """
                begin
                /* multi
                   line
                   comment */
                null  /* another multi line comment */;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   /* multi
                      line
                      comment */
                   null  /* another multi line comment */;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
