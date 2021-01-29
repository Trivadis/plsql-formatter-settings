package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Test

class Issue_82 extends ConfiguredTestFormatter {
    
    @Test
    def bulk_collect_outer_apply() {
        '''
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
        '''.formatAndAssert
    }

    @Test
    def bulk_collect_cross_apply() {
        '''
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
        '''.formatAndAssert
    }
}
