package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.BreaksX2
import org.junit.Before
import org.junit.Test

class Issue_13 extends ConfiguredTestFormatter {
    
    @Before
    def void setup() {
        formatter.options.put(formatter.identSpaces, 4)
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, BreaksX2.X2);
    }
    
    @Test
    def original_case_with_unnecessary_semicolon_for_set() {
        '''
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
        '''.formatAndAssert
    }


    @Test
    def simplified_case() {
        '''
            SET ECHO OFF
            
            SELECT *
              FROM dual;
            
            SET ECHO ON
        '''.formatAndAssert
    }

}
