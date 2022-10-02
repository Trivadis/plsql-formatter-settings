package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_218_indent_column_aliases_with_clause extends ConfiguredTestFormatter {

    @Test
    public void with_clause() {
        var sql = """
                with
                   employees (
                      id,
                      name,
                      salary
                   ) as (
                      select empno,
                             ename,
                             sal
                        from emp
                   )
                select *
                  from employees;
                """;
        formatAndAssert(sql);
    }
}
