package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_21 extends ConfiguredTestFormatter {

    @Test
    public void cursor_for() {
        final String sql = 
            """
            begin
               open c1 for
                  select *
                    from same_tab;
            end;
            /
            """;
        formatAndAssert(sql);
    }

}
