package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Execute_immediate_statement extends ConfiguredTestFormatter {

    @Test
    public void dynamic_returning_clause() throws IOException {
        var input = """
                begin
                execute immediate '...'
                returning into r1;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   execute immediate '...'
                      returning into r1;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void using_clause() throws IOException {
        var input = """
                begin
                execute immediate '...'
                using in d, e, f;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   execute immediate '...'
                      using in d, e, f;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void into_clause() throws IOException {
        var input = """
                begin
                execute immediate '...'
                into a, b, c
                using in d, e, f;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   execute immediate '...'
                      into a, b, c
                      using in d, e, f;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void bulk_collect_into_clause() throws IOException {
        var input = """
                begin
                   execute immediate '...'
                   bulk collect into a, b, c;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   execute immediate '...'
                      bulk collect into a, b, c;
                end;
                """;
        assertEquals(expected, actual);
    }
}
