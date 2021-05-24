package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Issue_13 extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().identSpaces, 4);
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X2);
    }

    @Test
    public void original_case_with_unnecessary_semicolon_for_set() {
        final String sql =
            """
            set echo off;
            
            create or replace package body emp_mgmt as
            
                function hire (
                    last_name      in  varchar2,
                    job_id         in  varchar2,
                    manager_id     in  number,
                    salary         in  number,
                    department_id  in  number
                ) return number is
                    new_empno number(16, 0);
                begin
                    --some code
                    return (new_empno);
                end;
            
            end emp_mgmt;
            /
            
            set echo on
            """;
        formatAndAssert(sql);
    }

    @Test
    public void simplified_case() {
        final String sql =
            """
            set echo off
            
            select *
              from dual;
            
            set echo on
            """;
        formatAndAssert(sql);

    }
}
