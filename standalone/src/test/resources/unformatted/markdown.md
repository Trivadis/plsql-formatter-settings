# Titel

## Introduction

This is a Markdown file with some `code blocks`. 

## Package Body

Here's the content of package_body.pkb

```sql
create or replace package body the_api.math as function to_int_table(in_integers
in varchar2,in_pattern in varchar2 default '[0-9]+')return sys.ora_mining_number_nt deterministic accessible
by(package the_api.math,package the_api.test_math)is l_result sys
.ora_mining_number_nt:=sys.ora_mining_number_nt();l_pos integer:= 1;l_int integer;
begin<<integer_tokens>>loop l_int:=to_number(regexp_substr(in_integers,in_pattern,1,l_pos));
exit integer_tokens when l_int is null;l_result.extend;l_result(l_pos):= l_int;l_pos:=l_pos+1;
end loop integer_tokens;return l_result;end to_int_table;end math;
/
```

## Syntax Error

Here's the content of syntax_error.sql

```  sql
declare
    l_var1  integer;
    l_var2  varchar2(20);
begin
    for r in /*(*/ select x.* from x join y on y.a = x.a)
    loop
      p(r.a, r.b, r.c);
    end loop;
end;
/
```

## Query (to be ignored)

Here's the content of query.sql, but the code block must not be formatted:

```
Select d.department_name,v.  employee_id 
,v 
. last_name frOm departments d CROSS APPLY(select*from employees e
  wHERE e.department_id=d.department_id) v WHeRE 
d.department_name in ('Marketing'
,'Operations',
'Public Relations') Order By d.
department_name,v.employee_id;
```

## Query (to be formatted)

Here's the content of query.sql:

``` sql
Select d.department_name,v.  employee_id 
,v 
. last_name frOm departments d CROSS APPLY(select*from employees e
  wHERE e.department_id=d.department_id) v WHeRE 
d.department_name in ('Marketing'
,'Operations',
'Public Relations') Order By d.
department_name,v.employee_id;
```

## JavaScript code

Here's another code which must not be formatted

``` js
var foo = function (bar) {
  return bar++;
};
```
