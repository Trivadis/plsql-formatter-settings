package com.trivadis.plsql.formatter.settings.examples;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Trivadis extends ConfiguredTestFormatter {

    @Test
    public void set_salary() {
        final String sql =
            """
            create or replace procedure set_salary (
               in_employee_id in employees.employee_id%type
            ) is
               cursor c_employees (
                  p_employee_id in employees.employee_id%type
               ) is
                  select last_name,
                         first_name,
                         salary
                    from employees
                   where employee_id = p_employee_id
                   order by last_name,
                            first_name;
               r_employee    c_employees%rowtype;
               l_new_salary  employees.salary%type;
            begin
               open c_employees(p_employee_id => in_employee_id);
               fetch c_employees into r_employee;
               close c_employees;
               new_salary(
                  in_employee_id  => in_employee_id,
                  out_salary      => l_new_salary
               );
               -- check whether salary has changed
               if r_employee.salary <> l_new_salary then
                  update employees
                     set salary = l_new_salary
                   where employee_id = in_employee_id;
               end if;
            end set_salary;
            /
            """;
        formatAndAssert(sql);
    }

}
