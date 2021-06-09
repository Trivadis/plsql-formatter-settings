package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A16_line_break_for_multiline_parents extends ConfiguredTestFormatter {

    @Test
    public void single_line_select() {
        var sql = """
                select deptno, empno, ename from emp where empno > 7000 order by empno;
                """;
        formatAndAssert(sql);
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
    public void single_line_insert() {
        var sql = """
                insert into t (c1, c2, c3) values ('1', '2', '3');
                """;
        formatAndAssert(sql);
    }

    @Test
    public void multi_line_single_table_insert() throws IOException {
        var input = """
                insert into mytable t (a, b, c, d) values ('a', 'b', 'c', 'd') return a, b, c, d
                into l_a, l_b, l_c, l_d log errors into mytable_errors ('bad') reject limit 10;
                """;
        var actual = formatter.format(input);
        var expected = """
                insert into mytable t (a, b, c, d) values ('a', 'b', 'c', 'd')
                return a, b, c, d
                  into l_a, l_b, l_c, l_d
                   log errors into mytable_errors ('bad') reject limit 10;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_multi_table_insert_all() throws IOException {
        var input = """
                insert all into t1 (c1, c2) values (c1, c2) into t2 (c1, c2) values (c1, c2)
                into t3 (c1, c2) values (c1, c2) log errors
                into mytable_errors ('bad') reject limit 10 select c1, c2 from t4;
                """;
        var actual = formatter.format(input);
        var expected = """
                insert all
                  into t1 (c1, c2) values (c1, c2)
                  into t2 (c1, c2) values (c1, c2)
                  into t3 (c1, c2) values (c1, c2)
                   log errors
                  into mytable_errors ('bad') reject limit 10
                select c1, c2 from t4;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_multi_table_insert_conditional() throws IOException {
        var input = """
                insert first when c5 = 1 then into t1 (c1, c2) values (c1, c2) when c3 > 2000 then into t2 (c1, c2)
                values (c1, c2) when c7 < 1 and c3 > 1500 or (c8 > 7 or c5 > 2) then into t3 (c1, c2)
                values (c1, c2) log errors into mytable_errors ('bad') reject limit 10
                select c1, c2 from t4;
                """;
        var actual = formatter.format(input);
        var expected = """
                insert first
                   when c5 = 1 then
                      into t1 (c1, c2) values (c1, c2)
                   when c3 > 2000 then
                      into t2 (c1, c2)
                      values (c1, c2)
                   when c7 < 1
                      and c3 > 1500
                      or (c8 > 7 or c5 > 2)
                   then
                      into t3 (c1, c2)
                      values (c1, c2)
                      log errors into mytable_errors ('bad') reject limit 10
                select c1, c2 from t4;
                """;
        assertEquals(expected, actual);
    }


    @Test
    public void single_line_delete() {
        var sql = """
                delete from t where 1 = 1 return c1 into l_c1 log errors into error_table reject limit 10;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void multi_line_delete() throws IOException {
        var input = """
                delete from t where 1 = 1 return c1 into l_c1 log errors into error_table
                reject limit 10;
                """;
        var actual = formatter.format(input);
        var expected = """
                delete from t
                 where 1 = 1
                return c1 into l_c1
                   log errors into error_table
                reject limit 10;
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
