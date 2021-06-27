package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Deterministic_clause extends ConfiguredTestFormatter {

    @Test
    public void example_14_16() throws IOException {
        var input = """
                create or replace function text_length(a clob)
                return number deterministic is
                begin
                   return dbms_lob.getlength(a);
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                create or replace function text_length(a clob)
                return number
                   deterministic
                is
                begin
                   return dbms_lob.getlength(a);
                end;
                """;
        assertEquals(expected, actual);
    }
}
