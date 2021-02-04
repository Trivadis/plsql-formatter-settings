package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Test;

public class Issue_64 extends ConfiguredTestFormatter {

    @Test
    public void update_commas_after() {
        final String sql = 
            """
            update my_table
               set n01 = 1,
                   n02 = 2,
                   n03 = 3,
                   n04 = my_function(1, 2, 3)
             where n01 = 1
               and n02 = 2
               and n03 = my_function(1, 2, 3);
            """;
        formatAndAssert(sql);
    }

    @Test
    public void update_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            update my_table
               set n01 = 1
                 , n02 = 2
                 , n03 = 3
                 , n04 = my_function(1, 2, 3)
             where n01 = 1
               and n02 = 2
               and n03 = my_function(1, 2, 3);
            """;
        formatAndAssert(sql);
    }

}
