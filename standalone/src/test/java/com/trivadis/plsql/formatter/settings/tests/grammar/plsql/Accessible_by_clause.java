package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Accessible_by_clause extends ConfiguredTestFormatter {

    @Test
    public void one() throws IOException {
        var input = """
                create package pkg as
                procedure p1 accessible
                by (procedure p2);
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                create package pkg as
                   procedure p1
                      accessible by (procedure p2);
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multiple_single_line() {
        var sql = """
                create package pkg as
                   procedure p1
                      accessible by (function f1, procedure p2, package pkg2, trigger trg1, type type);
                end;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void multiple_multi_line() {
        var sql = """
                create package pkg as
                   procedure p1
                      accessible by (
                         function f1,
                         procedure p2,
                         package pkg2,
                         trigger trg1,
                         type type
                      );
                end;
                """;
        formatAndAssert(sql);
    }
}
