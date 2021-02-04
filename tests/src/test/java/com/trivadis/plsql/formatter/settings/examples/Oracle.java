package com.trivadis.plsql.formatter.settings.examples;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Oracle extends ConfiguredTestFormatter {

    @Test
    public void emp_mgmt() {
        final String sql =
            """
            create or replace package body emp_mgmt as
               tot_emps   number;
               tot_depts  number;
               function hire (
                  last_name      in  varchar2,
                  job_id         in  varchar2,
                  manager_id     in  number,
                  salary         in  number,
                  department_id  in  number
               ) return number is
                  new_empno number(16, 0);
               begin
                  if monthly_value <= 4000 then
                     ilevel := 'Low Income';
                  elsif monthly_value > 4000 and monthly_value <= 7000 then
                     ilevel := 'Avg Income';
                  else
                     ilevel := 'High Income';
                  end if;
                  case
                     when jobid = 'PU_CLE' then
                        sal_raise :=.09;
                     when jobid = 'SH_CLERK' then
                        sal_raise :=.08;
                     when jobid = 'ST_CLERK222' then
                        sal_raise :=.07;
                     else
                        sal_raise := 0;
                        dbms_output.put_line('sal_raise := 0');
                  end case;
                  select case "1"
                           when 1 then
                              'xx'
                         end
                    into new_empno
                    from emp,
                         dual d1,
                         dual d2
                   where ( 1 = 2
                      or 3 = 4 )
                     and 0 = 1 + 2
                     and exists (
                            select 1,
                                   2,
                                   3
                              from wsh_new_deliveries wnd
                              join wsh_delivery_assignments wda
                                on wnd.delivery_id = wda.delivery_id
                              join hz_locations hl
                                on hps.location_id = hl.location_id
                         );
                  insert into employees (
                     employee_id,
                     full_name,
                     phone_number,
                     hire_date,
                     job_name,
                     value1,
                     value2,
                     value3
                  ) values (
                     new_empno,
                     'First'
                     || 'Middle'
                     || 'Last',
                     '(415)555-0100',
                     to_date('18-jun-2002', 'DD-MON-YYYY'),
                     'IT_PROG',
                     90,
                     100,
                     110
                  );
                  tot_emps                := tot_emps + 1;  -- := alignment
                  out_rec.var_char1       := in_rec1.first_name;
                  out_rec.var_char2222    := in_rec1.last_name;
                  proc1(
                     p1111  => a1,
                     p11    => a1,
                     p2     => a2
                  );
                  return ( new_empno );
               end;
            end emp_mgmt;
            /
            """;

        formatAndAssert(sql);
    }

}
