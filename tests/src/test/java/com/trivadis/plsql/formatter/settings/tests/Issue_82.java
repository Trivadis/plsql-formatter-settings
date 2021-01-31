package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_82 extends ConfiguredTestFormatter {

    @Test
    public void bulk_collect_outer_apply() {
        final String sql = 
            """
            DECLARE
               l_array my_array_tab;
            BEGIN
               SELECT t.a,
                      t.b,
                      t.c,
                      n.stuff
                 BULK COLLECT
                 INTO l_array
                 FROM some_table s
                OUTER APPLY ( s.nested_tab ) n;
            END;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void bulk_collect_cross_apply() {
        final String sql = 
            """
            DECLARE
               l_array my_array_tab;
            BEGIN
               SELECT t.a,
                      t.b,
                      t.c,
                      n.stuff
                 BULK COLLECT
                 INTO l_array
                 FROM some_table s
                CROSS APPLY ( s.nested_tab ) n;
            END;
            /
            """;
        formatAndAssert(sql);
    }

}
