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

}
