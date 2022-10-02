package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class O7_align_equality_predicate extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class True {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().alignEquality, true);
        }

        @Test
        public void where() throws IOException {
            var input = """
                    select *
                      from emp
                     where job = 'CLERK'
                       and deptno = 10;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select *
                      from emp
                     where job    = 'CLERK'
                       and deptno = 10;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void case_expression() throws IOException {
            var input = """
                    select ename,
                    case
                    when
                    job = 'CLERK'
                    and deptno = 10
                    then
                    'yes'
                    else
                    'no'
                    end filter_critera
                    from emp;
                    """;
            var actual = getFormatter().format(input);
            var expected = """
                    select ename,
                           case
                              when job      = 'CLERK'
                                 and deptno = 10
                              then
                                 'yes'
                              else
                                 'no'
                           end filter_critera
                      from emp;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class False {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().alignTabColAliases, false);
        }

        @Test
        public void where() {
            var sql = """
                    select *
                      from emp
                     where job = 'CLERK'
                       and deptno = 10;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void case_expression() {
            var sql = """
                    select ename,
                           case
                              when job = 'CLERK'
                                 and deptno = 10
                              then
                                 'yes'
                              else
                                 'no'
                           end filter_critera
                      from emp;
                    """;
            formatAndAssert(sql);
        }
    }
}
