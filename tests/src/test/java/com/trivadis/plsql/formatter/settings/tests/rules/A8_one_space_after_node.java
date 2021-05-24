package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A8_one_space_after_node extends ConfiguredTestFormatter {

    @Test
    public void create_or_replace() throws IOException {
        final String input =
                """
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
        final String expected =
                """
                create or replace package pkg is
                   g_variable integer;
                end pkg;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void from() throws IOException {
        final String input =
                """
                select * from     t;
                """;
        var actual = formatter.format(input);
        final String expected =
                """
                select * from t;
                """;
        assertEquals(expected, actual);
    }
}
