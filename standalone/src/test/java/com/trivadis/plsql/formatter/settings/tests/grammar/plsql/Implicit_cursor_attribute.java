package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Implicit_cursor_attribute extends ConfiguredTestFormatter {

    @Test
    public void non_bulk() throws IOException {
        var input = """
                begin
                delete from t;
                if not sql
                %
                isopen
                or sql
                %
                found
                or sql
                %
                notfound
                or sql
                %
                rowcount > 0
                then
                null;
                end if;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   delete from t;
                   if not sql%isopen
                      or sql%found
                      or sql%notfound
                      or sql%rowcount > 0
                   then
                      null;
                   end if;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void bulk_exceptions_example_13_13() throws IOException {
        var input = """
                begin
                   forall j in depts.first..depts.last save exceptions
                      update emp_temp set job = job || ' (Senior)'
                       where deptno = depts(j);
                exception
                   when dml_errors then
                      for i in 1..sql
                      %
                      bulk_exceptions.count
                      loop
                         error_message := sqlerrm(-(sql
                         %
                         bulk_exceptions(i).error_code));
                         dbms_output.put_line(error_message);
                                
                         bad_stmt_no   := sql
                         %
                         bulk_exceptions(i).error_index;
                         dbms_output.put_line('Bad statement #: ' || bad_stmt_no);
                                
                         bad_deptno    := depts(bad_stmt_no);
                         dbms_output.put_line('Bad department #: ' || bad_deptno);
                                
                         select job into bad_job from emp_temp where deptno = bad_deptno;
                                
                         dbms_output.put_line('Bad job: ' || bad_job);
                      end loop;
                                
                      commit;  -- Commit results of successful updates
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   forall j in depts.first..depts.last save exceptions
                      update emp_temp set job = job || ' (Senior)'
                       where deptno = depts(j);
                exception
                   when dml_errors then
                      for i in 1..sql%bulk_exceptions.count
                      loop
                         error_message := sqlerrm(-(sql%bulk_exceptions(i).error_code));
                         dbms_output.put_line(error_message);
                                
                         bad_stmt_no   := sql%bulk_exceptions(i).error_index;
                         dbms_output.put_line('Bad statement #: ' || bad_stmt_no);
                                
                         bad_deptno    := depts(bad_stmt_no);
                         dbms_output.put_line('Bad department #: ' || bad_deptno);
                                
                         select job into bad_job from emp_temp where deptno = bad_deptno;
                                
                         dbms_output.put_line('Bad job: ' || bad_job);
                      end loop;
                                
                      commit;  -- Commit results of successful updates
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void bulk_rowcount_example_13_15() throws IOException {
        var input = """
                declare
                   type dept_tab is table of departments.department_id%type;
                   deptnums dept_tab;
                begin
                   select department_id bulk collect into deptnums from departments;
                                
                   forall i in 1..deptnums.count
                      insert into emp_by_dept (employee_id, department_id)
                      select employee_id, department_id
                        from employees
                       where department_id = deptnums(i)
                       order by department_id, employee_id;
                                
                   for i in 1..deptnums.count
                   loop
                      -- Count how many rows were inserted for each department; that is,
                      -- how many employees are in each department.
                      dbms_output.put_line(
                         'Dept '
                         || deptnums(i)
                         || ': inserted '
                         || sql
                         %
                         bulk_rowcount(i)
                         || ' records'
                      );
                   end loop;
                   dbms_output.put_line('Total records inserted: ' || sql%rowcount);
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type dept_tab is table of departments.department_id%type;
                   deptnums dept_tab;
                begin
                   select department_id bulk collect into deptnums from departments;
                                
                   forall i in 1..deptnums.count
                      insert into emp_by_dept (employee_id, department_id)
                      select employee_id, department_id
                        from employees
                       where department_id = deptnums(i)
                       order by department_id, employee_id;
                                
                   for i in 1..deptnums.count
                   loop
                      -- Count how many rows were inserted for each department; that is,
                      -- how many employees are in each department.
                      dbms_output.put_line(
                         'Dept '
                         || deptnums(i)
                         || ': inserted '
                         || sql%bulk_rowcount(i)
                         || ' records'
                      );
                   end loop;
                   dbms_output.put_line('Total records inserted: ' || sql%rowcount);
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
