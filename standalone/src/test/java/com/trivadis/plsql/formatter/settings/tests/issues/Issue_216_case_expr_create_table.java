package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_216_case_expr_create_table extends ConfiguredTestFormatter {

    @Test
    public void create_table() {
        var sql = """
                create table letters (
                   letter       varchar2(1 char) not null constraint letters_pk primary key,
                   occurrences  integer          not null,
                   is_vowel     integer          generated always as (
                                                    case
                                                       when letter in ('a', 'e', 'i', 'o', 'u') then
                                                          1
                                                       else
                                                          0
                                                     end
                                                 ) virtual
                );
                """;
        formatAndAssert(sql);
    }
}
