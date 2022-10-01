package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Default_collation_clause extends ConfiguredTestFormatter {

    @Test
    public void procedure() throws IOException {
        var input = """
                create
                or
                replace
                procedure
                p
                default
                collation
                using_nls_comp
                is
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create or replace procedure p
                   default collation using_nls_comp
                is
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
