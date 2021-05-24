package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_82_outer_apply extends ConfiguredTestFormatter {

    @Test
    public void bulk_collect_outer_apply() {
        final String sql = 
            """
            declare
               l_array my_array_tab;
            begin
               select t.a,
                      t.b,
                      t.c,
                      n.stuff
                 bulk collect
                 into l_array
                 from some_table s
                outer apply (s.nested_tab) n;
            end;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void bulk_collect_cross_apply() {
        final String sql = 
            """
            declare
               l_array my_array_tab;
            begin
               select t.a,
                      t.b,
                      t.c,
                      n.stuff
                 bulk collect
                 into l_array
                 from some_table s
                cross apply (s.nested_tab) n;
            end;
            /
            """;
        formatAndAssert(sql);
    }

}
