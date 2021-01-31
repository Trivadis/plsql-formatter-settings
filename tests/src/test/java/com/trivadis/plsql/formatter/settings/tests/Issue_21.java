package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_21 extends ConfiguredTestFormatter {

    @Test
    public void cursor_for() {
        final String sql = 
            """
            BEGIN
               OPEN c1 FOR
                  SELECT *
                    FROM same_tab;
            END;
            /
            """;
        formatAndAssert(sql);
    }

}
