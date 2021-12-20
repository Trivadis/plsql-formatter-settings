package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Expression extends ConfiguredTestFormatter {

    @Test
    public void example_2_32_improving_readablity_with_parentheses() throws IOException {
        var input = """
                declare
                  a integer := 2**2*3**2;
                  b integer := (2**2)*(3**2);
                begin
                  dbms_output.put_line('a = ' || to_char(a));
                  dbms_output.put_line('b = ' || to_char(b));
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   a integer := 2 ** 2 * 3 ** 2;
                   b integer := (2 ** 2) * (3 ** 2);
                begin
                   dbms_output.put_line('a = ' || to_char(a));
                   dbms_output.put_line('b = ' || to_char(b));
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
