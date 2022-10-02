package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_13_do_not_right_align_set extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().identSpaces, 4);
    }

    @Test
    public void original_case_with_unnecessary_semicolon_for_set() {
        var sql = """
                set echo off;
                            
                create or replace package body emp_mgmt as
                            
                    function hire(
                        last_name     in varchar2,
                        job_id        in varchar2,
                        manager_id    in number,
                        salary        in number,
                        department_id in number
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
        var sql = """
                set echo off
                            
                select *
                  from dual;
                            
                set echo on
                """;
        formatAndAssert(sql);
    }
}
