package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O11_line_break_on_subqueries extends ConfiguredTestFormatter {

    @Nested
    class True {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breakOnSubqueries, true);
        }

        @Test
        public void correlated_subquery_single_line() throws IOException {
            var input = """
                    select d.dname,
                           (select count(*) from emp e where e.deptno = d.deptno) as emp_count
                      from dept d;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname,
                           (select count(*) from emp e where e.deptno = d.deptno) as emp_count
                      from dept d;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void correlated_subquery_multiline() throws IOException {
            var input = """
                    select d.dname,
                           (select count(*)
                              from emp e
                             where e.deptno = d.deptno) as emp_count
                      from dept d;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname,
                           (
                              select count(*)
                                from emp e
                               where e.deptno = d.deptno
                           ) as emp_count
                      from dept d;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class False {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breakOnSubqueries, false);
        }

        @Test
        public void join() {
            var sql = """
                    select d.dname,
                           (select count(*)
                              from emp e
                             where e.deptno = d.deptno) as emp_count
                      from dept d;
                    """;
            formatAndAssert(sql);
        }
    }
}
