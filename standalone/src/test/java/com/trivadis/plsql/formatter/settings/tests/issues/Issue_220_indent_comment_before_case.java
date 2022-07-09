package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_220_indent_comment_before_case extends ConfiguredTestFormatter {

    @Test
    public void comment_before_first_when() {
        var sql = """
                select case
                          -- single line comment
                          when dummy = 'X' then
                             'YES'
                          else
                             'NO'
                       end as new_dummy
                  from dummy;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void comment_before_second_when() {
        var sql = """
                select case
                          when dummy = 'X' then
                             'YES'
                             -- single line comment
                          when dummy = 'X' then
                             'YES'
                          else
                             'NO'
                       end as new_dummy
                  from dummy;
                """;
        formatAndAssert(sql);
    }

}
