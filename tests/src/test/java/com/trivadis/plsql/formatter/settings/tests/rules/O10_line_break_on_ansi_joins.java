package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O10_line_break_on_ansi_joins extends ConfiguredTestFormatter {

    @Nested
    class True {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breakAnsiiJoin, true);
        }

        @Test
        public void join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e join dept d on d.deptno = e.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                      join dept d
                        on d.deptno = e.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void join_using() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e join dept d using (deptno);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                      join dept d
                     using (deptno);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void inner_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e inner join dept d on d.deptno = e.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                     inner join dept d
                        on d.deptno = e.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void left_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e left join dept d on d.deptno = e.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                      left join dept d
                        on d.deptno = e.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void left_outer_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e left outer join dept d on d.deptno = e.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                      left outer join dept d
                        on d.deptno = e.deptno;
                    """;
            assertEquals(expected, actual);
        }


        @Test
        public void right_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e right join dept d on d.deptno = e.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                     right join dept d
                        on d.deptno = e.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void full_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e full join dept d on d.deptno = e.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                      full join dept d
                        on d.deptno = e.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void full_outer_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e full outer join dept d on d.deptno = e.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                      full outer join dept d
                        on d.deptno = e.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void cross_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e cross join dept d;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                     cross join dept d;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void natural_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e natural join dept d;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                    natural join dept d;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void natural_inner_join() throws IOException {
            var input = """
                    select d.dname, e.ename
                      from emp e natural inner join dept d;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                    natural inner join dept d;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void partition_by_right_outer_join() throws IOException {
            var input = """
                    select d.dname, e.job, e.ename
                      from emp e partition by (e.job) right outer join dept d on e.deptno = d.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.job, e.ename
                      from emp e partition by (e.job)
                     right outer join dept d
                        on e.deptno = d.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void partition_by_right_join() throws IOException {
            var input = """
                    select d.dname, e.job, e.ename
                      from emp e partition by (e.job) right join dept d on e.deptno = d.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.job, e.ename
                      from emp e partition by (e.job)
                     right join dept d
                        on e.deptno = d.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void partition_by_left_outer_join() throws IOException {
            var input = """
                    select d.dname, e.job, e.ename
                     from dept d partition by (e.job) left outer join emp e on e.deptno = d.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.job, e.ename
                      from dept d partition by (e.job)
                      left outer join emp e
                        on e.deptno = d.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void partition_by_left_join() throws IOException {
            var input = """
                    select d.dname, e.job, e.ename
                     from dept d partition by (e.job) left join emp e on e.deptno = d.deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.job, e.ename
                      from dept d partition by (e.job)
                      left join emp e
                        on e.deptno = d.deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void cross_apply() throws IOException {
            var input = """
                    select d.dname, e.ename
                     from emp e cross apply (select * from dept d
                                             where d.deptno = e.empno);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from emp e
                     cross apply (select *
                                    from dept d
                                   where d.deptno = e.empno);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void outer_apply() throws IOException {
            var input = """
                    select d.dname, e.ename
                     from dept d outer apply (select * from emp e
                                              where e.deptno = d.empno);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.dname, e.ename
                      from dept d
                     outer apply (select *
                                    from emp e
                                   where e.deptno = d.empno);
                    """;
            assertEquals(expected, actual);
        }


    }

    @Nested
    class False {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breakAnsiiJoin, false);
        }

        @Test
        public void join() {
            var sql = """
                    select d.dname, e.ename
                      from emp e join dept d on d.deptno = e.deptno;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void left_join() {
            var sql = """
                    select d.dname, e.ename
                      from emp e left join dept d on d.deptno = e.deptno;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void right_join() {
            var sql = """
                    select d.dname, e.ename
                      from emp e right join dept d on d.deptno = e.deptno;
                    """;
            formatAndAssert(sql);
        }
    }
}
