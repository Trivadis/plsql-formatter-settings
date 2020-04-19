package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format
import org.junit.Test

class Select_list extends ConfiguredTestFormatter {

    @Test
    def select_list_comma_after() {
        '''
            SELECT CASE a
                      WHEN 1  THEN
                         'one'
                      WHEN 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a2,
                   b,
                   c,
                   to_char(d, 'YYYY-MM') AS d2,
                   CASE
                      WHEN a = 1  THEN
                         'one'
                      WHEN a = 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a3
              FROM t2;
        '''.formatAndAssert
    }

    @Test
    def select_list_comma_before() {
        formatter.options.put(formatter.breaksComma, Format.Breaks.Before)
        '''
            SELECT CASE a
                      WHEN 1  THEN
                         'one'
                      WHEN 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a2
                 , b
                 , c
                 , to_char(d, 'YYYY-MM') AS d2
                 , CASE
                      WHEN a = 1  THEN
                         'one'
                      WHEN a = 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a3
              FROM t2;
        '''.formatAndAssert(true)
    }

}