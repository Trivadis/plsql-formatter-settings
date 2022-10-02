package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class O6_line_breaks_after_select extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class True {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksAfterSelect, true);
        }

        @Test
        public void select() throws IOException {
            var input = """
                    select d.deptno, d.dname, count(*) as emp_count
                    from emp e
                    join dept d
                    on d.deptno = e.deptno
                    where e.sal > 2000
                    group by d.deptno, d.dname
                    having count(*) > 2
                    order by emp_count desc;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select
                       d.deptno, d.dname, count(*) as emp_count
                    from
                       emp e
                       join dept d
                       on d.deptno = e.deptno
                    where
                       e.sal > 2000
                    group by
                       d.deptno, d.dname
                    having
                       count(*) > 2
                    order by
                       emp_count desc;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class False {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksAfterSelect, false);
        }

        @Test
        public void select() throws IOException {
            var input = """
                    select d.deptno, d.dname, count(*) as emp_count
                    from emp e
                    join dept d
                    on d.deptno = e.deptno
                    where e.sal > 2000
                    group by d.deptno, d.dname
                    having count(*) > 2
                    order by emp_count desc;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select d.deptno, d.dname, count(*) as emp_count
                      from emp e
                      join dept d
                        on d.deptno = e.deptno
                     where e.sal > 2000
                     group by d.deptno, d.dname
                    having count(*) > 2
                     order by emp_count desc;
                    """;
            assertEquals(expected, actual);
        }
    }
}
