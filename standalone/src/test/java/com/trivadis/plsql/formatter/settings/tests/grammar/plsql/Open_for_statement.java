package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Open_for_statement extends ConfiguredTestFormatter {

    @Test
    public void tokenized_for_select_statement() throws IOException {
        var input = """
                begin
                open
                c1
                for
                select
                *
                from
                emp
                ;
                end
                ;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   open c1 for
                      select *
                        from emp;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_for_dynamic_select_statement_using() throws IOException {
        var input = """
                begin
                open
                c1
                for
                'select * from emp where deptno =:deptno and sal > :sal'
                using
                in
                p1
                ,
                p2
                ;
                end
                ;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   open c1 for
                      'select * from emp where deptno =:deptno and sal > :sal'
                      using in p1, p2;
                end;
                """;
        assertEquals(expected, actual);
    }
}
