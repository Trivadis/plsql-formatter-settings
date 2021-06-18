package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Comment extends ConfiguredTestFormatter {

    @Test
    @Disabled("SQLDev Bug 20.4.1, only one line break delivered by the Lexer after the single line comment!")
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
