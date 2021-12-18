Select d.department_name,v.  employee_id 
,v 
. last_name frOm departments d CROSS APPLY(select*from employees e
  wHERE e.department_id=d.department_id) v WHeRE 
d.department_name in ('Marketing'
,'Operations',
'Public Relations') Order By d.
department_name,v.employee_id;