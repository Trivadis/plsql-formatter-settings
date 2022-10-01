package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A8_one_space_after_node extends ConfiguredTestFormatter {

    @Test
    public void create_or_replace() throws IOException {
        var input = """
                create
                or
                replace
                package
                pkg is
                   g_variable integer;
                end pkg;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create or replace package pkg is
                   g_variable integer;
                end pkg;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void from() throws IOException {
        var input = """
                select * from     t;
                """;
        var actual = formatter.format(input);
        var expected = """
                select * from t;
                """;
        assertEquals(expected, actual);
    }
}
