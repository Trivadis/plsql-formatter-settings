package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.*;

public class Cursor_for_loop_statement extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Select {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breakOnSubqueries, false);
        }

        @Test
        public void no_break_after_left_paren() {
            // in this case the left margin has to be extended for the select statement
            // important are lines 3, 4, 5 (select_list) and 7 (from_clause)
            var sql = """
                    begin
                       for r in (select d.deptno,
                                        d.dname,
                                        e.empno,
                                        e.ename
                                   from emp e,
                                        dept d
                                  where d.deptno = e.deptno)
                       loop
                          null;
                       end loop;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void break_after_left_paren() {
            // default calculation of left margin (3 spaces per indentation)
            var sql = """
                    begin
                       for r in (
                          select d.deptno,
                                 d.dname,
                                 e.empno,
                                 e.ename
                            from emp e,
                                 dept d
                           where d.deptno = e.deptno)
                       loop
                          null;
                       end loop;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void break_after_left_paren_and_before_right_paren() {
            // default calculation of left margin (3 spaces per indentation)
            var sql = """
                    begin
                       for r in (
                          select d.deptno,
                                 d.dname,
                                 e.empno,
                                 e.ename
                            from emp e,
                                 dept d
                           where d.deptno = e.deptno
                       )
                       loop
                          null;
                       end loop;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }
    }
}
