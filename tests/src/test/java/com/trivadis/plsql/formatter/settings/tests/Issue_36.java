package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_36 extends ConfiguredTestFormatter {

    @Test
    public void if_only() {
        final String sql = 
            """
            CREATE OR REPLACE PROCEDURE p IS
            BEGIN
               -- comment 1
               $IF DBMS_DB_VERSION.VER_LE_12_2 $THEN
                  -- comment 2
                  dbms_output.put_line('older: first line');
                  dbms_output.put_line('older: second line');
               $END
            END;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void if_else() {
        final String sql = 
            """
            CREATE OR REPLACE PROCEDURE p IS
            BEGIN
               -- comment 1
               $IF DBMS_DB_VERSION.VER_LE_12_2 $THEN
                  -- comment 2
                  dbms_output.put_line('older');
               $ELSE
                  -- comment 3
                  dbms_output.put_line('newer');
               $END
            END;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void if_elsif_else() {
        final String sql = 
            """
            CREATE OR REPLACE PROCEDURE p IS
            BEGIN
               -- comment 1
               $IF DBMS_DB_VERSION.VER_LE_12_2 $THEN
                  -- comment 2
                  dbms_output.put_line('older');
               $ELSIF DBMS_DB_VERSION.VER_LE_18 $THEN
                  -- comment 3
                  dbms_output.put_line('newer');
               $ELSE
                  -- comment 4
                  dbms_output.put_line('newest');
               $END
            END;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void false_if_elsif_else_lower() {
        final String sql = 
            """
            CREATE OR REPLACE PROCEDURE p IS
            BEGIN
               -- comment 1
               $if false $then
                  -- comment 2
                  dbms_output.put_line('false');
               $elsif DBMS_DB_VERSION.VER_LE_18 $then
                  -- comment 3
                  dbms_output.put_line('18c');
               $else
                  -- comment 4
                  dbms_output.put_line('newer than 18c');
               $end
            END;
            /
            """;
        formatAndAssert(sql);
    }

}
