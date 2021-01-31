package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Before;
import org.junit.Test;

public class Issue_13 extends ConfiguredTestFormatter {

    @Before
    public void setup() {
        getFormatter().options.put(getFormatter().identSpaces, 4);
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X2);
    }

    @Test
    public void original_case_with_unnecessary_semicolon_for_set() {
        final String sql =
            """
            SET ECHO OFF;
            
            CREATE OR REPLACE PACKAGE BODY emp_mgmt AS
            
                FUNCTION hire (
                    last_name      IN  VARCHAR2,
                    job_id         IN  VARCHAR2,
                    manager_id     IN  NUMBER,
                    salary         IN  NUMBER,
                    department_id  IN  NUMBER
                ) RETURN NUMBER IS
                    new_empno NUMBER(16, 0);
                BEGIN
                    --some code
                    RETURN ( new_empno );
                END;
            
            END emp_mgmt;
            /
            
            SET ECHO ON
            """;
        formatAndAssert(sql);
    }

    @Test
    public void simplified_case() {
        final String sql =
            """
            SET ECHO OFF
            
            SELECT *
              FROM dual;
            
            SET ECHO ON
            """;
        formatAndAssert(sql);

    }
}
