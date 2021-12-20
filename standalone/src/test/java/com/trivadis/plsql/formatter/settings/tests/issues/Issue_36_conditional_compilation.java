package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_36_conditional_compilation extends ConfiguredTestFormatter {

    @Test
    public void if_only() {
        var sql = """
                create or replace procedure p is
                begin
                   -- comment 1
                   $if dbms_db_version.ver_le_12_2 $then
                      -- comment 2
                      dbms_output.put_line('older: first line');
                      dbms_output.put_line('older: second line');
                   $end
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void if_else() {
        var sql = """
                create or replace procedure p is
                begin
                   -- comment 1
                   $if dbms_db_version.ver_le_12_2 $then
                      -- comment 2
                      dbms_output.put_line('older');
                   $else
                      -- comment 3
                      dbms_output.put_line('newer');
                   $end
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void if_elsif_else() {
        var sql = """
                create or replace procedure p is
                begin
                   -- comment 1
                   $if dbms_db_version.ver_le_12_2 $then
                      -- comment 2
                      dbms_output.put_line('older');
                   $elsif dbms_db_version.ver_le_18 $then
                      -- comment 3
                      dbms_output.put_line('newer');
                   $else
                      -- comment 4
                      dbms_output.put_line('newest');
                   $end
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void false_if_elsif_else_lower() {
        var sql = """
                create or replace procedure p is
                begin
                   -- comment 1
                   $if false $then
                      -- comment 2
                      dbms_output.put_line('false');
                   $elsif dbms_db_version.ver_le_18 $then
                      -- comment 3
                      dbms_output.put_line('18c');
                   $else
                      -- comment 4
                      dbms_output.put_line('newer than 18c');
                   $end
                end;
                /
                """;
        formatAndAssert(sql);
    }
}
