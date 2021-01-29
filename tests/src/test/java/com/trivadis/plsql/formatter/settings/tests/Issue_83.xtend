package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Test

class Issue_83 extends ConfiguredTestFormatter {
    
    @Test
    def select_single_column_with_hint() {
        '''
        	SELECT /*+ parallel(t, 2) */
        	       a
        	  FROM t;
        '''.formatAndAssert
    }

    @Test
    def select_two_columns_with_hint() {
        '''
        	SELECT /*+ parallel(t, 2) */
        	       a,
        	       b
        	  FROM t;
        '''.formatAndAssert
    }

    @Test
    def two_selects_with_hints() {
        '''
        	SELECT /*+ parallel(t, 2) */
        	       a
        	  FROM t;

        	SELECT /*+ parallel(t, 2) */
        	       a
        	  FROM t;
        '''.formatAndAssert
    }


}
