-- based on examples in Allround Automations PL/SQL Beautifier settings 
create or replace function mgrname (
   p_empno in emp.empno%type
) return emp.ename%type is
   result  emp.ename%type;
   i       integer;
begin
   result  := null;
   i       := 1;
   if p_empno is null then
      -- If empno is null, return an empty name
      result := null;
   else
      -- Fetch the name of the manager
      select m.ename
        into result
        from emp e,
             emp m
       where e.empno = p_empno
         and m.empno = e.mgr
         and d.deptno in (
                10, 20, 30, 40
             );
   end if;
   return ( result );
exception
   when no_data_found then
      return ( null );
end;
/

begin
   for emp_cursor in (
      select *
        from emp
   ) loop
      if emp_cursor.mgr is null or emp_cursor.mgr = 0 then
         dbms_output.put_line('No manager');
      else
         dbms_output.put_line('Manager = ' || to_char(emp_cursor));
      end if;
   end loop;
end;
/

begin
   -- Select
   select depno as department_number,
          dname as departmen_name,
          loc as department_location
     from dept,
          emp
    where emp.empno = p_empno
      and dept.deptno = emp.deptno;
   -- Insert
   insert into dept (
      deptno,
      dname,
      loc
   ) values (
      10,
      'Accounting',
      'New York'
   );
   -- Update
   update dept
      set dname = 'Accounting',
          loc = 'New York'
    where deptno = 10;
end;
/

create or replace procedure insertdept (
   p_deptno  in out  dept.deptno%type,
   p_dname   in      dept.dname%type,
   p_loc     in      dept.loc%type
) is
begin
   -- Determine the maximum department number if necessary
   if p_deptno is null then
      select nvl(max(deptno), 0) + 1
        into p_deptno
        from dept;
   end if;
   -- Insert the new record
   insert into dept (
      deptno,
      dname,
      loc
   ) values (
      p_deptno,
      p_dname,
      p_loc
   );
end;
/

declare
   type dept_record is record (
      deptno  number(2),
      dname   varchar2(13),
      loc     varchar2(13)
   );
begin
   null;
end;
/