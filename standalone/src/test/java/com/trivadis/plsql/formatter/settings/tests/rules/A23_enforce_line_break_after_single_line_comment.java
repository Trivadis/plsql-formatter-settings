package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class A23_enforce_line_break_after_single_line_comment extends ConfiguredTestFormatter {

    @Nested
    class case_expression {

        @Test
        public void comment_before_then() {
            var sql = """
                    select empno,
                           ename,
                           case
                              when 'JOB' = 'SALESMAN' -- make gender-neutral
                              then
                                 'SALESPERSON'
                              else
                                 job
                           end as job,
                           sal
                      from emp;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void comment_after_every_token() {
            var sql = """
                    select empno,
                           ename,
                           case
                              when              -- comment 1
                                 'JOB'          -- comment 2
                                 =              -- comment 3
                                 'SALESMAN'     -- comment 4
                              then              -- comment 5
                                 'SALESPERSON'  -- comment 6
                              else              -- comment 7
                                 job            -- comment 8
                           end                  -- comment 9
                           as                   -- comment 10
                           job,
                           sal
                      from emp;
                    """;
            formatAndAssert(sql);
        }
    }
}
