package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_21_line_open_cursor_for extends ConfiguredTestFormatter {

    @Test
    public void cursor_for() {
        var sql = """
                begin
                   open c1 for
                      select *
                        from same_tab;
                end;
                /
                """;
        formatAndAssert(sql);
    }
}
