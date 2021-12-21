package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Delete_statement_extension extends ConfiguredTestFormatter {

    @Test
    public void delete() throws IOException {
        var input = """
                declare
                   my_emp_id number(6);
                   my_job_id varchar2(10);
                   my_sal    number(8, 2);
                   cursor c1 is select employee_id, job_id, salary from employees for update;
                begin
                   open c1;
                   loop
                      fetch c1 into my_emp_id, my_job_id, my_sal;
                      if my_job_id = 'SA_REP' then
                         delete
                         employees
                         where
                         current
                         of
                         c1;
                      end if;
                      exit when c1%notfound;
                   end loop;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   my_emp_id number(6);
                   my_job_id varchar2(10);
                   my_sal    number(8, 2);
                   cursor c1 is select employee_id, job_id, salary from employees for update;
                begin
                   open c1;
                   loop
                      fetch c1 into my_emp_id, my_job_id, my_sal;
                      if my_job_id = 'SA_REP' then
                         delete employees
                          where current of c1;
                      end if;
                      exit when c1%notfound;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void delete_from() throws IOException {
        var input = """
                begin
                delete
                from
                employees
                where
                current
                of
                c1
                ;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   delete
                     from employees
                    where current of c1;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
