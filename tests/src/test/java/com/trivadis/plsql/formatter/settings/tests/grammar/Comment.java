package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Comment extends ConfiguredTestFormatter {

    @Test
    public void one_single_line_with_additional_break() {
        var sql = """
                -- Single line comment
                
                commit;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void two_single_lines_with_additional_break() {
        var sql = """
                --
                -- Single line comment
                
                commit;
                """;
        formatAndAssert(sql);
    }
}
