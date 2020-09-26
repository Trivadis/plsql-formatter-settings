package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.Breaks
import org.junit.Test

class Issue_64 extends ConfiguredTestFormatter {
    
    @Test
    def update_commas_after() {
        '''
            UPDATE my_table
               SET n01 = 1,
                   n02 = 2,
                   n03 = 3,
                   n04 = my_function(1, 2, 3)
             WHERE n01 = 1
               AND n02 = 2
               AND n03 = my_function(1, 2, 3);
        '''.formatAndAssert
    }

    @Test
    def update_commas_before() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
            UPDATE my_table
               SET n01 = 1
                 , n02 = 2
                 , n03 = 3
                 , n04 = my_function(1, 2, 3)
             WHERE n01 = 1
               AND n02 = 2
               AND n03 = my_function(1, 2, 3);
        '''.formatAndAssert
    }

}
