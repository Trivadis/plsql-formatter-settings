package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Forall_statement extends ConfiguredTestFormatter {

    @Test
    public void example_12_8() throws IOException {
        var input = """
                begin
                  forall i in depts.first..depts.last
                    delete from employees_temp
                    where department_id = depts(i);
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   forall i in depts.first..depts.last
                      delete from employees_temp
                       where department_id = depts(i);
                end;
                """;
        assertEquals(expected, actual);
    }
}
