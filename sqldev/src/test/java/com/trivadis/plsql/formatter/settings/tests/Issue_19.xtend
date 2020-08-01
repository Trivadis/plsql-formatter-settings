package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Test

class Issue_19 extends ConfiguredTestFormatter {
    
    @Test
    def arg_list_split_5_or_more_params() {
        '''
            BEGIN
               f();
               f(1);
               f(1, 2);
               f(1, 2, 3);
               f(1, 2, 3, 4);
               f(
                  1,
                  2,
                  3,
                  4,
                  5
               );
               f(
                  1,
                  2,
                  3,
                  4,
                  5,
                  6
               );
            END;
            /
        '''.formatAndAssert
    }

    @Test
    def expr_list_split_5_or_more_params() {
        '''
            SELECT f(),
                   f(1),
                   f(1, 2),
                   f(1, 2, 3),
                   f(1, 2, 3, 4),
                   f(
                      1,
                      2,
                      3,
                      4,
                      5
                   ),
                   f(
                      1,
                      2,
                      3,
                      4,
                      5,
                      6
                   )
              FROM dual;
        '''.formatAndAssert
    }

    @Test
    def nested_arg_list() {
        '''
            BEGIN
               f(
                  f(1),
                  f(1, 2),
                  f(1, 2, 3),
                  f(1, 2, 3, 4),
                  f(1, 2, 3, 4, 5),
               );
            END;
            /
        '''.formatAndAssert
    }

    @Test
    def nested_expr_list() {
        '''
            SELECT f(
                      f(1),
                      f(1, 2),
                      f(1, 2, 3),
                      f(1, 2, 3, 4),
                      f(1, 2, 3, 4, 5)
                   ),
              FROM dual;
        '''.formatAndAssert
    }

}
