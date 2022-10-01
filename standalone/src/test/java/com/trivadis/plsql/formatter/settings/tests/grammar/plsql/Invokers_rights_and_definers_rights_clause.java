package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Invokers_rights_and_definers_rights_clause extends ConfiguredTestFormatter {

    @Test
    public void invoker() throws IOException {
        var input = """
                create function f
                return integer
                authid
                current_user
                is
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create function f
                   return integer
                   authid current_user
                is
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void definer() throws IOException {
        var input = """
                create function f
                return integer
                authid
                definer
                is
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create function f
                   return integer
                   authid definer
                is
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
