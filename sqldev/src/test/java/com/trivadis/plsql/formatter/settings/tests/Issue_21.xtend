package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Test

class Issue_21 extends ConfiguredTestFormatter {
    
    @Test
    def cursor_for() {
        '''
            BEGIN
               OPEN c1 FOR
                  SELECT *
                    FROM same_tab;
            END;
            /
        '''.formatAndAssert
    }

}
