package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_81_boolean_conditions_and_parenthesis extends ConfiguredTestFormatter {

    @Test
    public void sql_condition_single_line() throws IOException {
        var input = """
                select * from dba_tables where (owner = 'SYSTEM' and (table_name = 'REDO_DB' or table_name = 'REDO_LOG')) or (owner = 'SYS' and (table_name = 'ALERT_QT' or table_name = 'ALL_UNIFIED_AUDIT_ACTIONS'));
                """;
        var expected = """
                select *
                  from dba_tables
                 where (owner = 'SYSTEM' and (table_name = 'REDO_DB' or table_name = 'REDO_LOG'))
                    or (owner = 'SYS' and (table_name = 'ALERT_QT' or table_name = 'ALL_UNIFIED_AUDIT_ACTIONS'));
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void plsql_expr_single_line() throws IOException {
        var input = """
                begin if((1=2)or((2=2 and 3=3)or(4=5)))then dbms_output.put_line('Yes');end if;end;
                """;
        var expected = """
                begin
                   if ((1 = 2) or ((2 = 2 and 3 = 3) or (4 = 5))) then
                      dbms_output.put_line('Yes');
                   end if;
                end;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void plsql_expr_multiline() throws IOException {
        var input = """
                begin
                   if ( ( variable1 = variable2 ) or ( (
                      variable2 = variable2 and variable3 = variable3
                   ) or ( variable4 = variable5 ) ) ) then
                      dbms_output.put_line('Yes');
                   end if;
                end;
                """;
        var expected = """
                begin
                   if ((variable1 = variable2) or ((
                               variable2 = variable2 and variable3 = variable3
                            ) or (variable4 = variable5)))
                   then
                      dbms_output.put_line('Yes');
                   end if;
                end;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
