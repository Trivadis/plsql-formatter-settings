-- based on https://github.com/Trivadis/plsql-and-sql-coding-guidelines/blob/main/docs/3-coding-style/coding-style.md#example

create or replace procedure set_salary(
   in_employee_id in employees.employee_id%type
) is
   cursor c_employees(
      p_employee_id in employees.employee_id%type
   ) is 
      select last_name, first_name, salary
        from employees
       where employee_id = p_employee_id
       order by last_name, first_name;
   r_employee   c_employees%ROWTYPE;
   l_new_salary employees.salary%type;
begin
   open c_employees(p_employee_id => in_employee_id);
   fetch c_employees into r_employee;
   close c_employees;
   new_salary(
      in_employee_id => in_employee_id,
      out_salary     => l_new_salary
   );
   -- Check whether salary has changed
   if r_employee.salary <> l_new_salary then
      update employees
         set salary = l_new_salary
       where employee_id = in_employee_id;
   end if;
end set_salary;
/
