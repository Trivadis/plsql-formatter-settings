package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Sqlcode_function extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_12_23() throws IOException {
        var input = """
                CREATE OR REPLACE PROCEDURE p AUTHID DEFINER AS
                  name    EMPLOYEES.LAST_NAME%TYPE;
                  v_code  NUMBER;
                  v_errm  VARCHAR2(64);
                BEGIN
                  SELECT last_name INTO name
                  FROM EMPLOYEES
                  WHERE EMPLOYEE_ID = -1;
                EXCEPTION
                  WHEN OTHERS THEN
                    v_code := SQLCODE;
                    v_errm := SUBSTR(SQLERRM, 1, 64);
                    DBMS_OUTPUT.PUT_LINE
                      ('Error code ' || v_code || ': ' || v_errm);
                
                    /* Invoke another procedure,
                       declared with PRAGMA AUTONOMOUS_TRANSACTION,
                       to insert information about errors. */
                
                    INSERT INTO errors (code, message)
                    VALUES (v_code, v_errm);
                                
                    RAISE;
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create or replace procedure p
                   authid definer
                as
                   name   employees.last_name%type;
                   v_code number;
                   v_errm varchar2(64);
                begin
                   select last_name into name
                     from employees
                    where employee_id = -1;
                exception
                   when others then
                      v_code := sqlcode;
                      v_errm := substr(sqlerrm, 1, 64);
                      dbms_output.put_line('Error code '
                         || v_code
                         || ': '
                         || v_errm);
                                
                      /* Invoke another procedure,
                         declared with PRAGMA AUTONOMOUS_TRANSACTION,
                         to insert information about errors. */
                                
                      insert into errors (code, message)
                      values (v_code, v_errm);
                                
                      raise;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
