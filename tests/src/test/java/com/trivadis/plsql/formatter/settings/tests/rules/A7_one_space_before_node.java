package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A7_one_space_before_node extends ConfiguredTestFormatter {

    @Test
    public void column_alias() throws IOException {
        final String input =
            """
            select a    as   my_col from t;
            """;
        var actual = formatter.format(input);
        final String expected =
            """
            select a as my_col from t;
            """;
        assertEquals(expected, actual);
    }

    @Test
    public void table_alias() throws IOException {
        final String input =
                """
                select a my_col from t   my_alias;
                """;
        var actual = formatter.format(input);
        final String expected =
                """
                select a my_col from t my_alias;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void function() throws IOException {
        final String input =
                """
                create package pkg is
                   function f    (    p1 in number   ) return integer;
                end pkg;
                /
                """;
        var actual = formatter.format(input);
        final String expected =
                """
                create package pkg is
                   function f (p1 in number) return integer;
                end pkg;
                /
                """;
        assertEquals(expected, actual);
    }
}
