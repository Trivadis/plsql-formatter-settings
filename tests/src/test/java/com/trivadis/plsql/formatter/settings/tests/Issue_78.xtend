package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.Breaks
import org.junit.Test

class Issue_78 extends ConfiguredTestFormatter {
    
    @Test
    def subselect_with_commas_after() {
        '''
            SELECT table_name,
                   (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ),
                   t.BLOCKS
              FROM user_tables t;
        '''.formatAndAssert
    }

    @Test
    def subselect_with_commas_before() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
            SELECT table_name
                 , (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   )
                 , t.BLOCKS
              FROM user_tables t;
        '''.formatAndAssert
    }

    @Test
    def subselect_with_commas_before_with_alias() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
            SELECT table_name
                 , (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ) AS num_indexes
                 , t.BLOCKS
              FROM user_tables t;
        '''.formatAndAssert
    }
}
