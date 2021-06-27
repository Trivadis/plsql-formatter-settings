package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Continue_Statement extends ConfiguredTestFormatter {

    @Test
    public void for_loop_example() throws IOException {
        var input = """
                declare
                   v_employees employees%rowtype;
                   cursor c1 is select * from employees;
                begin
                   open c1;
                
                   -- Fetch entire row into v_employees record:
                   <<outer_loop>>
                   for i in 1..10
                   loop
                      -- Process data here
                      for j in 1..10
                      loop
                         fetch c1 into v_employees;
                         continue outer_loop when c1%notfound;
                         -- Process data here
                         null;
                      end loop;
                   end loop outer_loop;
                             
                   close c1;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   v_employees employees%rowtype;
                   cursor c1 is select * from employees;
                begin
                   open c1;
                
                   -- Fetch entire row into v_employees record:
                   <<outer_loop>>
                   for i in 1..10
                   loop
                      -- Process data here
                      for j in 1..10
                      loop
                         fetch c1 into v_employees;
                         continue outer_loop when c1%notfound;
                         -- Process data here
                         null;
                      end loop;
                   end loop outer_loop;
                             
                   close c1;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void long_condition() throws IOException {
        var input = """
                begin
                   <<outer_loop>>
                   for i in 1..10
                   loop
                      for j in 1..10
                      loop
                         fetch c1 into v_employees;
                         continue outer_loop when c1%notfound
                         and v_employees.name = '123456789.123456789.123456789.';
                      end loop;
                   end loop outer_loop;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   <<outer_loop>>
                   for i in 1..10
                   loop
                      for j in 1..10
                      loop
                         fetch c1 into v_employees;
                         continue outer_loop when c1%notfound
                            and v_employees.name = '123456789.123456789.123456789.';
                      end loop;
                   end loop outer_loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

}
