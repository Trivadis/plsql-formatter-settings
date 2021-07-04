package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Explicit_Cursor extends ConfiguredTestFormatter {

    @Test
    public void example_6_5_explicit_cusor_declaration_and_definition() throws IOException {
        var input = """
                declare
                  cursor c1 return departments%rowtype;    -- Declare c1
                
                  cursor c2 is                             -- Declare and define c2
                    select employee_id, job_id, salary from employees
                    where salary > 2000;
                
                  cursor c1 return departments%rowtype is  -- Define c1,
                    select * from departments              -- repeating return type
                    where department_id = 110;
                
                  cursor c3 return locations%rowtype;      -- Declare c3
                
                  cursor c3 is                             -- Define c3,
                    select * from locations                -- omitting return type
                    where country_id = 'JP';
                begin
                  null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   cursor c1 return departments%rowtype;    -- Declare c1
                                
                   cursor c2 is                             -- Declare and define c2
                      select employee_id, job_id, salary
                        from employees
                       where salary > 2000;
                                
                   cursor c1 return departments%rowtype is  -- Define c1,
                      select *
                        from departments              -- repeating return type
                       where department_id = 110;
                                
                   cursor c3 return locations%rowtype;      -- Declare c3
                                
                   cursor c3 is                             -- Define c3,
                      select *
                        from locations                -- omitting return type
                       where country_id = 'JP';
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_6_11_explict_cursor_that_accepts_parameters() throws IOException {
        var input = """
                declare
                  cursor c (job varchar2, max_sal number) is
                    select last_name, first_name, (salary - max_sal) overpayment
                    from employees
                    where job_id = job
                    and salary > max_sal
                    order by salary;
                
                  procedure print_overpaid is
                    last_name_   employees.last_name%type;
                    first_name_  employees.first_name%type;
                    overpayment_      employees.salary%type;
                  begin
                    loop
                      fetch c into last_name_, first_name_, overpayment_;
                      exit when c%notfound;
                      dbms_output.put_line(last_name_ || ', ' || first_name_ ||
                        ' (by ' || overpayment_ || ')');
                    end loop;
                  end print_overpaid;
                
                begin
                  dbms_output.put_line('----------------------');
                  dbms_output.put_line('Overpaid Stock Clerks:');
                  dbms_output.put_line('----------------------');
                  open c('ST_CLERK', 5000);
                  print_overpaid;
                  close c;
                
                  dbms_output.put_line('-------------------------------');
                  dbms_output.put_line('Overpaid Sales Representatives:');
                  dbms_output.put_line('-------------------------------');
                  open c('SA_REP', 10000);
                  print_overpaid;
                  close c;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   cursor c(job     varchar2,
                            max_sal number) is
                      select last_name, first_name, (salary - max_sal) overpayment
                        from employees
                       where job_id = job
                         and salary > max_sal
                       order by salary;
                                
                procedure print_overpaid is
                   last_name_   employees.last_name%type;
                   first_name_  employees.first_name%type;
                   overpayment_ employees.salary%type;
                begin
                   loop
                      fetch c into last_name_, first_name_, overpayment_;
                      exit when c%notfound;
                      dbms_output.put_line(last_name_
                         || ', '
                         || first_name_
                         || ' (by '
                         || overpayment_
                         || ')');
                   end loop;
                end print_overpaid;
                                
                begin
                   dbms_output.put_line('----------------------');
                   dbms_output.put_line('Overpaid Stock Clerks:');
                   dbms_output.put_line('----------------------');
                   open c('ST_CLERK', 5000);
                   print_overpaid;
                   close c;
                                
                   dbms_output.put_line('-------------------------------');
                   dbms_output.put_line('Overpaid Sales Representatives:');
                   dbms_output.put_line('-------------------------------');
                   open c('SA_REP', 10000);
                   print_overpaid;
                   close c;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_6_35_cursor_expression() throws IOException {
        var input = """
                declare
                  type emp_cur_typ is ref cursor;
                
                  emp_cur    emp_cur_typ;
                  dept_name  departments.department_name%type;
                  emp_name   employees.last_name%type;
                
                  cursor c1 is
                    select department_name,
                      cursor ( select e.last_name
                                from employees e
                                where e.department_id = d.department_id
                                order by e.last_name
                              ) employees
                    from departments d
                    where department_name like 'A%'
                    order by department_name;
                begin
                  open c1;
                  loop  -- Process each row of query result set
                    fetch c1 into dept_name, emp_cur;
                    exit when c1%notfound;
                    dbms_output.put_line('Department: ' || dept_name);
                
                    loop -- Process each row of subquery result set
                      fetch emp_cur into emp_name;
                      exit when emp_cur%notfound;
                      dbms_output.put_line('-- Employee: ' || emp_name);
                    end loop;
                  end loop;
                  close c1;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type emp_cur_typ is ref cursor;
                                
                   emp_cur   emp_cur_typ;
                   dept_name departments.department_name%type;
                   emp_name  employees.last_name%type;
                                
                   cursor c1 is
                      select department_name,
                             cursor (
                                select e.last_name
                                  from employees e
                                 where e.department_id = d.department_id
                                 order by e.last_name
                             ) employees
                        from departments d
                       where department_name like 'A%'
                       order by department_name;
                begin
                   open c1;
                   loop  -- Process each row of query result set
                      fetch c1 into dept_name, emp_cur;
                      exit when c1%notfound;
                      dbms_output.put_line('Department: ' || dept_name);
                                
                      loop -- Process each row of subquery result set
                         fetch emp_cur into emp_name;
                         exit when emp_cur%notfound;
                         dbms_output.put_line('-- Employee: ' || emp_name);
                      end loop;
                   end loop;
                   close c1;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
