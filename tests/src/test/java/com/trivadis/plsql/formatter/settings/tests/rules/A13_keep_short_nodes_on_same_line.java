package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A13_keep_short_nodes_on_same_line extends ConfiguredTestFormatter {

    @Test
    public void simple_comparison() throws IOException {
        var input = """
                select *
                  from dual
                 where (1, 2, 3) = (
                           select 1, 2, 3 from dual
                       );
                """;
        var expected = """
                select *
                  from dual
                 where (1, 2, 3) = (select 1, 2, 3 from dual);
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void group_comparison() throws IOException {
        var input = """
                select *
                  from dual
                 where dummy = any (
                           'A', 'B', 'C', 'X', 'Y', 'Z'
                       );
                """;
        var expected = """
                select *
                  from dual
                 where dummy = any ('A', 'B', 'C', 'X', 'Y', 'Z');
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void procedure_call() throws IOException {
        var input = """
                begin
                   sys.dbms_output
                      .put_line(
                         'Amount: '
                         || '100 CHF'
                   );
                end;
                /
                """;
        var expected = """
                begin
                   sys.dbms_output.put_line('Amount: ' || '100 CHF');
                end;
                /
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void function_expression() throws IOException {
        var input = """
                select sum(
                          sal
                       ) as emp_sal
                  from emp;
                """;
        var expected = """
                select sum(sal) as emp_sal
                  from emp;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void ignore_concat_expression() {
        var sql = """
                select '4'
                       || '2'
                       || '!' as result
                  from dual;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void null_expression() throws IOException {
        var input = """
                select
                *
                from
                dual
                where
                dummy
                is
                not
                null
                ;
                """;
        var expected = """
                select *
                  from dual
                 where dummy is not null;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
