package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A7_one_space_before_node extends ConfiguredTestFormatter {

    @Test
    public void column_alias() throws IOException {
        var input = """
                select a    as   my_col from t;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select a as my_col from t;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void table_alias() throws IOException {
        var input = """
                select a my_col from t   my_alias;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select a my_col from t my_alias;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void function() throws IOException {
        var input = """
                create package pkg is
                   function f    (    p1 in number   ) return integer;
                end pkg;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create package pkg is
                   function f(p1 in number) return integer;
                end pkg;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void assignment() throws IOException {
        var input = """
                begin
                a
                :=
                1;
                end;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   a :=
                      1;
                end;
                /
                """;
        assertEquals(expected, actual);
    }


}
