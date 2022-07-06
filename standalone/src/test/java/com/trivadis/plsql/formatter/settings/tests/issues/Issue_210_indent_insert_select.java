package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_210_indent_insert_select extends ConfiguredTestFormatter {

    @Test
    public void single_table_insert() {
        var sql = """
                insert into deptsal (dept_no, dept_name, salary)
                select d.deptno, d.dname, sum(e.sal + nvl(e.comm, 0)) as sal
                  from dept d
                  left join (
                          select *
                            from emp
                           where hiredate > date '1980-01-01'
                       ) e
                    on e.deptno = d.deptno
                 group by d.deptno, d.dname;
                """;
        formatAndAssert(sql);
    }
}
