package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Before;
import org.junit.Test;

public class Issue_12 extends ConfiguredTestFormatter {

    @Before
    public void setup() {
        getFormatter().options.put(getFormatter().identSpaces, 4);
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X2);
    }

    @Test
    public void package_spec() {
        final String sql = 
            """
            create or replace package abc as
            
                function get_id1 (
                    p_1  number,
                    p_2  number,
                    p_3  number
                ) return number;
            
                function get_id2 (
                    p_1  my_table.id%type,
                    p_2  my_table.id%type,
                    p_3  my_table.id%type
                ) return my_table.id%type;
            
            end;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void package_body() {
        final String sql =
            """
            create or replace package body abc as
            
                function get_id1 (
                    p_1  number,
                    p_2  number,
                    p_3  number
                ) return number is
                    local_1  number;
                    local_2  number;
                    local_3  number;
                    local_4  number;
                    local_5  number;
                begin
                    return 9999;
                end get_id1;
            
                function get_id2 (
                    p_1  my_table.id%type,
                    p_2  my_table.id%type,
                    p_3  my_table.id%type
                ) return my_table.id%type is
                    local_1  my_table.id%type;
                    local_2  my_table.id%type;
                    local_3  my_table.id%type;
                    local_4  my_table.id%type;
                    local_5  my_table.id%type;
                begin
                    -- some code
                    return 9999;
                end get_id2;
            
            end;
            /
            """;
        formatAndAssert(sql);
    }

}
