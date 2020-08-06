package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.Breaks
import org.junit.Test

class Issue_30 extends ConfiguredTestFormatter {
    
    @Test
    def into_clause_commas_after() {
        '''
            SELECT namespace,
                   key,
                   scope
              INTO l_namespace,
                   l_key,
                   l_scope
              FROM configuration
             WHERE id = p_id;
        '''.formatAndAssert
    }

    @Test
    def into_clause_commas_before() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
            SELECT namespace
                 , key
                 , scope
              INTO l_namespace
                 , l_key
                 , l_scope
              FROM configuration
             WHERE id = p_id;
        '''.formatAndAssert
    }
}
