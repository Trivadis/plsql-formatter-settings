-- based on the example in the formatting preferences of the Oracle SQL Developer 19.4
CREATE OR REPLACE PACKAGE BODY emp_mgmt AS
   tot_emps   NUMBER;
   tot_depts  NUMBER;
   FUNCTION hire (
      last_name      IN  VARCHAR2,
      job_id         IN  VARCHAR2,
      manager_id     IN  NUMBER,
      salary         IN  NUMBER,
      department_id  IN  NUMBER
   ) RETURN NUMBER IS
      new_empno NUMBER(16, 0);
   BEGIN
      IF monthly_value <= 4000 THEN
         ilevel := 'Low Income';
      ELSIF monthly_value > 4000 AND monthly_value <= 7000 THEN
         ilevel := 'Avg Income';
      ELSE
         ilevel := 'High Income';
      END IF;
      CASE
         WHEN jobid = 'PU_CLE' THEN
            sal_raise :=.09;
         WHEN jobid = 'SH_CLERK' THEN
            sal_raise :=.08;
         WHEN jobid = 'ST_CLERK222' THEN
            sal_raise :=.07;
         ELSE
            sal_raise := 0;
            dbms_output.put_line('sal_raise := 0');
      END CASE;
      SELECT CASE "1"
               WHEN 1 THEN
                  'XX'
             END
        INTO new_empno
        FROM emp,
             dual d1,
             dual d2
       WHERE ( 1 = 2
          OR 3 = 4 )
         AND 0 = 1 + 2
         AND EXISTS (
                SELECT 1,
                       2,
                       3
                  FROM wsh_new_deliveries wnd
                  JOIN wsh_delivery_assignments wda
                    ON wnd.delivery_id = wda.delivery_id
                  JOIN hz_locations hl
                    ON hps.location_id = hl.location_id
             );
      INSERT INTO employees (
         employee_id,
         full_name,
         phone_number,
         hire_date,
         job_name,
         value1,
         value2,
         value3
      ) VALUES (
         new_empno,
         'First'
         || 'Middle'
         || 'Last',
         '(415)555-0100',
         TO_DATE('18-JUN-2002', 'DD-MON-YYYY'),
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
      RETURN ( new_empno );
   END;
END emp_mgmt;
/