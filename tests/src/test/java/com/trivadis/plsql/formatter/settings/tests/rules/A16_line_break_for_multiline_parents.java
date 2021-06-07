package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A16_line_break_for_multiline_parents extends ConfiguredTestFormatter {

    @Test
    public void single_line_select() throws IOException {
        var input = """
                 select deptno, empno, ename from emp where empno > 7000 order by empno;
                """;
        var actual = formatter.format(input);
        var expected = """
                select deptno, empno, ename from emp where empno > 7000 order by empno;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_select() throws IOException {
        var input = """
                select deptno, empno, ename from emp
                where empno > 7000 order by empno;
                """;
        var actual = formatter.format(input);
        var expected = """
                select deptno, empno, ename
                  from emp
                 where empno > 7000
                 order by empno;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_select_list() throws IOException {
        var input = """
                select deptno, empno, ename,
                hiredate from emp;
                """;
        var actual = formatter.format(input);
        var expected = """
                select deptno,
                       empno,
                       ename,
                       hiredate
                  from emp;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_order_by_list() throws IOException {
        var input = """
                select * from emp order by deptno, empno, ename,
                hiredate;
                """;
        var actual = formatter.format(input);
        var expected = """
                select *
                  from emp
                 order by deptno,
                       empno,
                       ename,
                       hiredate;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_group_by_list() throws IOException {
        var input = """
                select count(*) from emp group by deptno, empno, ename,
                hiredate;
                """;
        var actual = formatter.format(input);
        var expected = """
                select count(*)
                  from emp
                 group by deptno,
                       empno,
                       ename,
                       hiredate;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_select_into() throws IOException {
        var input = """
                begin
                select empno, ename, job, mgr, hiredate, sal, comm, deptno
                into l_empno, l_ename, l_job, l_mgr, l_hiredate, l_sal, l_comm,
                l_deptno
                from emp;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   select empno, ename, job, mgr, hiredate, sal, comm, deptno
                     into l_empno,
                          l_ename,
                          l_job,
                          l_mgr,
                          l_hiredate,
                          l_sal,
                          l_comm,
                          l_deptno
                     from emp;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
