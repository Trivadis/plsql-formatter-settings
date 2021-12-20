package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Exception_handler extends ConfiguredTestFormatter {

    @Test
    public void combined() throws IOException {
        var input = """
                begin
                p(1);
                exception
                when
                no_data_found
                or
                too_many_rows
                then
                null;
                when
                others
                then
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   p(1);
                exception
                   when no_data_found or too_many_rows then
                      null;
                   when others then
                      null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
