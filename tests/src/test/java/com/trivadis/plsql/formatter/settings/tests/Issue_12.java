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
            CREATE OR REPLACE PACKAGE abc AS
            
                FUNCTION get_id1 (
                    p_1  NUMBER,
                    p_2  NUMBER,
                    p_3  NUMBER
                ) RETURN NUMBER;
            
                FUNCTION get_id2 (
                    p_1  my_table.id%TYPE,
                    p_2  my_table.id%TYPE,
                    p_3  my_table.id%TYPE
                ) RETURN my_table.id%TYPE;
            
            END;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void package_body() {
        final String sql =
            """
            CREATE OR REPLACE PACKAGE BODY abc AS
            
                FUNCTION get_id1 (
                    p_1  NUMBER,
                    p_2  NUMBER,
                    p_3  NUMBER
                ) RETURN NUMBER IS
                    local_1  NUMBER;
                    local_2  NUMBER;
                    local_3  NUMBER;
                    local_4  NUMBER;
                    local_5  NUMBER;
                BEGIN
                    RETURN 9999;
                END get_id1;
            
                FUNCTION get_id2 (
                    p_1  my_table.id%TYPE,
                    p_2  my_table.id%TYPE,
                    p_3  my_table.id%TYPE
                ) RETURN my_table.id%TYPE IS
                    local_1  my_table.id%TYPE;
                    local_2  my_table.id%TYPE;
                    local_3  my_table.id%TYPE;
                    local_4  my_table.id%TYPE;
                    local_5  my_table.id%TYPE;
                BEGIN
                    -- some code
                    RETURN 9999;
                END get_id2;
            
            END;
            /
            """;
        formatAndAssert(sql);
    }

}
