-- based on examples in Allround Automations PL/SQL Beautifier settings 

CREATE OR REPLACE FUNCTION mgrname(
   p_empno IN emp.empno%TYPE
) RETURN emp.ename%TYPE IS
   result emp.ename%TYPE;
   i      INTEGER;
BEGIN
   result := NULL;
   i      := 1;
   IF p_empno IS NULL THEN
      -- If empno is null, return an empty name
      result := NULL;
   ELSE
      -- Fetch the name of the manager
      SELECT m.ename
        INTO result
        FROM emp e, emp m
       WHERE e.empno = p_empno
         AND m.empno = e.mgr
         AND d.deptno IN (10, 20, 30, 40);
   END IF;
   RETURN(result);
EXCEPTION
   WHEN NO_DATA_FOUND THEN
      RETURN(NULL);
END;
/

BEGIN
   FOR emp_cursor IN (
      SELECT * FROM emp
   ) LOOP
      IF emp_cursor.mgr IS NULL OR emp_cursor.mgr = 0 THEN
         dbms_output.put_line('No manager');
      ELSE
         dbms_output.put_line('Manager = ' || to_char(emp_cursor));
      END IF;
   END LOOP;
END;
/

BEGIN
   -- Select
   SELECT depno AS department_number, 
          dname AS departmen_name, 
          loc   AS department_location
     FROM dept, emp
    WHERE emp.empno = p_empno
      AND dept.deptno = emp.deptno;
   -- Insert
   INSERT INTO dept (
      deptno, 
      dname, 
      loc
   ) VALUES (
      10, 
      'Accounting', 
      'New York'
   );
   -- Update
   UPDATE dept
      SET dname = 'Accounting', 
          loc = 'New York'
    WHERE deptno = 10;
END;
/

CREATE OR REPLACE PROCEDURE insertdept(
   p_deptno IN OUT dept.deptno%TYPE,
   p_dname  IN     dept.dname%TYPE,
   p_loc    IN     dept.loc%TYPE
) IS
BEGIN
   -- Determine the maximum department number if necessary
   IF p_deptno IS NULL THEN
      SELECT nvl(MAX(deptno), 0) + 1
        INTO p_deptno
        FROM dept;
   END IF;
   -- Insert the new record
   INSERT INTO dept (
      deptno, 
      dname, 
      loc
   ) VALUES (
      p_deptno, 
      p_dname, 
      p_loc
   );
END;
/

DECLARE
   TYPE dept_record IS RECORD (
      deptno NUMBER(2),
      dname  VARCHAR2(13),
      loc    VARCHAR2(13)
   );
BEGIN
   NULL;
END;
/
