package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_204_lost_line_break_after_label extends ConfiguredTestFormatter {

    @Test
    public void line_break_after_label() {
        var sql = """
                create or replace package body pkg is
                   procedure p is
                      co_errno constant simple_integer := -20501;
                   begin
                      <<check_other_things>>
                      null;
                   end;
                end;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void space_after_label() {
        var sql = """
                create or replace package body pkg is
                   procedure p is
                      co_errno constant simple_integer := -20501;
                   begin
                      <<check_other_things>> null;
                   end;
                end;
                """;
        formatAndAssert(sql);
    }
}
