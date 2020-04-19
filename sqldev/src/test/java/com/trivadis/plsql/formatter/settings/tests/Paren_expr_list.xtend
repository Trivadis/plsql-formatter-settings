package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Test

class Paren_expr_list extends ConfiguredTestFormatter {

    @Test
    def unnamed_parameters() {
        '''
            SELECT func(a, b, c, 100, 200, 300, 400, 500)
              FROM dual;
        '''.formatAndAssert
    }

    @Test
    def named_parameters() {
        '''
            SELECT func(
                      p1               => a,
                      p2               => b,
                      p300             => c,
                      p40000           => 100,
                      p50              => 200,
                      p6               => 300,
                      p7               => 400,
                      p88888888888888  => 500
                   )
              FROM dual;
        '''.formatAndAssert
    }

}