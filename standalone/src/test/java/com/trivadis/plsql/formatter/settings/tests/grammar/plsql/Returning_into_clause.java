package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Returning_into_clause extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_6_58() throws IOException {
        // UPDATE Statement Assigns Values to Record Variable
        var input = """
                DECLARE
                  TYPE EmpRec IS RECORD (
                    last_name  employees.last_name%TYPE,
                    salary     employees.salary%TYPE
                  );
                  emp_info    EmpRec;
                  old_salary  employees.salary%TYPE;
                BEGIN
                  SELECT salary INTO old_salary
                   FROM employees
                   WHERE employee_id = 100;
                
                  UPDATE employees
                    SET salary = salary * 1.1
                    WHERE employee_id = 100
                    RETURNING last_name, salary INTO emp_info;
                
                  DBMS_OUTPUT.PUT_LINE (
                    'Salary of ' || emp_info.last_name || ' raised from ' ||
                    old_salary || ' to ' || emp_info.salary
                  );
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type emprec is record(
                         last_name employees.last_name%type,
                         salary    employees.salary%type
                      );
                   emp_info   emprec;
                   old_salary employees.salary%type;
                begin
                   select salary into old_salary
                     from employees
                    where employee_id = 100;
                                
                   update employees
                      set salary = salary * 1.1
                    where employee_id = 100
                returning last_name, salary into emp_info;
                                
                   dbms_output.put_line(
                      'Salary of '
                      || emp_info.last_name
                      || ' raised from '
                      || old_salary
                      || ' to '
                      || emp_info.salary
                   );
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_7_1_tokenized_returning_into_clause() throws IOException {
        // Static SQL Statements
        var input = """
                DECLARE
                  emp_id          employees_temp.employee_id%TYPE := 299;
                  emp_first_name  employees_temp.first_name%TYPE  := 'Bob';
                  emp_last_name   employees_temp.last_name%TYPE   := 'Henry';
                BEGIN
                  INSERT INTO employees_temp (employee_id, first_name, last_name)
                  VALUES (emp_id, emp_first_name, emp_last_name);
                
                  UPDATE employees_temp
                  SET first_name = 'Robert'
                  WHERE employee_id = emp_id;
                
                  DELETE FROM employees_temp
                  WHERE employee_id = emp_id
                  RETURNING
                  first_name
                  ,
                  last_name
                  INTO
                  emp_first_name
                  ,
                  emp_last_name
                  ;
                
                  COMMIT;
                  DBMS_OUTPUT.PUT_LINE (emp_first_name || ' ' || emp_last_name);
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   emp_id         employees_temp.employee_id%type := 299;
                   emp_first_name employees_temp.first_name%type  := 'Bob';
                   emp_last_name  employees_temp.last_name%type   := 'Henry';
                begin
                   insert into employees_temp (employee_id, first_name, last_name)
                   values (emp_id, emp_first_name, emp_last_name);
                                
                   update employees_temp
                      set first_name = 'Robert'
                    where employee_id = emp_id;
                                
                   delete from employees_temp
                    where employee_id = emp_id
                returning first_name,
                          last_name
                     into emp_first_name,
                          emp_last_name;
                                
                   commit;
                   dbms_output.put_line(emp_first_name
                      || ' '
                      || emp_last_name);
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_13_25() throws IOException {
        // Returning Deleted Rows in Two Nested Tables
        var input = """
                DECLARE
                  TYPE NumList IS TABLE OF employees.employee_id%TYPE;
                  enums  NumList;
                  TYPE NameList IS TABLE OF employees.last_name%TYPE;
                  names  NameList;
                BEGIN
                  DELETE FROM emp_temp
                  WHERE department_id = 30
                  RETURNING employee_id, last_name
                  BULK COLLECT INTO enums, names;
                                
                  DBMS_OUTPUT.PUT_LINE ('Deleted ' || SQL%ROWCOUNT || ' rows:');
                  FOR i IN enums.FIRST .. enums.LAST
                  LOOP
                    DBMS_OUTPUT.PUT_LINE ('Employee #' || enums(i) || ': ' || names(i));
                  END LOOP;
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type numlist is table of employees.employee_id%type;
                   enums numlist;
                   type namelist is table of employees.last_name%type;
                   names namelist;
                begin
                   delete from emp_temp
                    where department_id = 30
                returning employee_id, last_name
                     bulk collect into enums, names;
                                
                   dbms_output.put_line('Deleted '
                      || sql%rowcount
                      || ' rows:');
                   for i in enums.first..enums.last
                   loop
                      dbms_output.put_line('Employee #'
                         || enums(i)
                         || ': '
                         || names(i));
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_13_26_returning_into_clause_tokenized() throws IOException {
        // DELETE with RETURN BULK COLLECT INTO in FORALL Statement
        var input = """
                DECLARE
                  TYPE NumList IS TABLE OF NUMBER;
                  depts  NumList := NumList(10,20,30);
                                
                  TYPE enum_t IS TABLE OF employees.employee_id%TYPE;
                  e_ids  enum_t;
                                
                  TYPE dept_t IS TABLE OF employees.department_id%TYPE;
                  d_ids  dept_t;
                                
                BEGIN
                  FORALL j IN depts.FIRST..depts.LAST
                    DELETE FROM emp_temp
                    WHERE department_id = depts(j)
                    RETURNING
                    employee_id
                    ,
                    department_id
                    BULK
                    COLLECT
                    INTO
                    e_ids
                    ,
                    d_ids
                    ;
                                
                  DBMS_OUTPUT.PUT_LINE ('Deleted ' || SQL%ROWCOUNT || ' rows:');
                                
                  FOR i IN e_ids.FIRST .. e_ids.LAST
                  LOOP
                    DBMS_OUTPUT.PUT_LINE (
                      'Employee #' || e_ids(i) || ' from dept #' || d_ids(i)
                    );
                  END LOOP;
                END;
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type numlist is table of number;
                   depts numlist := numlist(10, 20, 30);
                                
                   type enum_t is table of employees.employee_id%type;
                   e_ids enum_t;
                                
                   type dept_t is table of employees.department_id%type;
                   d_ids dept_t;
                                
                begin
                   forall j in depts.first..depts.last
                      delete from emp_temp
                       where department_id = depts(j)
                   returning employee_id,
                             department_id
                        bulk collect
                        into e_ids,
                             d_ids;
                                
                   dbms_output.put_line('Deleted '
                      || sql%rowcount
                      || ' rows:');
                                
                   for i in e_ids.first..e_ids.last
                   loop
                      dbms_output.put_line(
                         'Employee #'
                         || e_ids(i)
                         || ' from dept #'
                         || d_ids(i)
                      );
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }
}
