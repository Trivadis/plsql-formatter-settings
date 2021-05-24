package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A5_no_space_before_node extends ConfiguredTestFormatter {

    @Test
    public void concat() throws IOException {
        final String input =
            """
            select 'a'  |   |   'b' from t;
            """;
        var actual = formatter.format(input);
        final String expected =
            """
            select 'a' || 'b' from t;
            """;
        assertEquals(expected, actual);
    }

    @Test
    public void dot_single_line() throws IOException {
        final String input =
                """
                select t   .   column from t;
                """;
        var actual = formatter.format(input);
        final String expected =
                """
                select t.column from t;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void dot_multi_line() throws IOException {
        final String input =
                """
                select t
                       .   column from t;
                """;
        var actual = formatter.format(input);
        final String expected =
                """
                select t
                       .column from t;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void plsql_slash() throws IOException {
        final String input =
                """
                begin null; end; /
                """;
        var actual = formatter.format(input);
        final String expected =
                """
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void expr_slash() throws IOException {
        final String input =
                """
                select 5   /   2
                  from dual;
                """;
        var actual = formatter.format(input);
        final String expected =
                """
                select 5 / 2
                  from dual;
                """;
        assertEquals(expected, actual);
    }
}
