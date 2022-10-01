package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_186_with_clause extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup_non_trivadis_default_settings() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void keep_single_line() {
        var sql = """
                with e as (select * from emp where deptno = 10) select * from e;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void keep_with_on_single_line() {
        var sql = """
                with e as (select * from emp where deptno = 10)
                select *
                  from e;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void keep_line_break_after_with() {
        var sql = """
                with
                   e as (
                      select *
                        from emp
                       where deptno = 10
                   )
                select *
                  from e;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void keep_single_line_named_queries() {
        var sql = """
                with
                   e as (select * from emp where deptno = 10),
                   d as (select * from dept)
                select e.empno, e.name, d.deptno, d.dname
                  from e
                  join d
                    on d.deptno = e.deptno;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void keep_indent_of_plsql_declaration() {
        var sql = """
                with
                   function f(
                      in_value in number
                   ) return number is
                   begin
                      return in_value;
                   end;
                   e as (
                      select f(empno) as empno,
                             ename,
                             deptno
                        from emp
                       where deptno = 10
                   )
                select *
                  from e;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void keep_indent_of_plsql_declaration_only() {
        var sql = """
                with
                   function f(
                      in_value in number
                   ) return number is
                   begin
                      return in_value;
                   end;
                select f(empno) as empno,
                       ename,
                       deptno
                  from e;
                """;
        formatAndAssert(sql);
    }
}
