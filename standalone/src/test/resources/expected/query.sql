select d.department_name,
       v.employee_id,
       v.last_name
  from departments d
 cross apply (
          select *
            from employees e
           where e.department_id = d.department_id
       ) v
 where d.department_name in ('Marketing', 'Operations', 'Public Relations')
 order by d.department_name, v.employee_id;
