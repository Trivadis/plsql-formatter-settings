package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Assert
import org.junit.Test

class FormatterOffOn extends ConfiguredTestFormatter {
    
    @Test
    def one_stmt_sl_comment_eclipse_style() {
        '''
            SELECT *
              FROM dual;
            -- @formatter:off
            SELECT
               *
                  from
                     dual
            ; -- @formatter:on
            SELECT *
              FROM dual;
        '''.formatAndAssert
    }

    @Test
    def one_stmt_ml_comment_eclipse_style() {
        '''
            SELECT *
              FROM dual;
            /* @formatter:off */
            SELECT
               *
                  from
                     dual
            ; /* @formatter:on */
            SELECT *
              FROM dual;
        '''.formatAndAssert
    }


    @Test
    def one_stmt_sl_comment_plsqldev_style() {
        '''
            SELECT *
              FROM dual;
            -- NoFormat Start
            SELECT
               *
                  from
                     dual
            ; -- NoFormat End
            SELECT *
              FROM dual;
        '''.formatAndAssert
    }

    @Test
    def one_stmt_ml_comment_plsqldev_style() {
        '''
            SELECT *
              FROM dual;
            /* NoFormat Start */
            SELECT
               *
                  from
                     dual
            ; /* NoFormat End */
            SELECT *
              FROM dual;
        '''.formatAndAssert
    }
    
    @Test
    def two_stmt_mixed_style() {
        '''
            SELECT *
              FROM dual;
            /* noformat start, however in SQLDev 20.2 keyword is uppercase nonetheless*/
               DELETE
                  FrOm 
                     EmP where a
                               = b
            ;
            /* @formatter:ON, the next statement is formatted by SQLDev */
            DELETE FROM emp
             WHERE dept = 10;
            /* @formatter:OFF, the next statement is not formated by SQLDev */
                      UPDATE
                    emp
                  set
                sal = sal + 10
               ;
            -- noformat end, after this line everthing is formatted by SQLDev
            SELECT *
              FROM emp
              JOIN dept
                ON dept.deptno = emp.deptno;
        '''.formatAndAssert
    }

    @Test
    def format_if_on_and_off_defined_in_same_comment() {
        val unformatted = '''
            select * from dual;
            -- @formatter:on @formatter:off
            SELECT
               *
                  from
                     dual
            ; -- @formatter:on
            select * from dual;
        '''
        val expected = '''
            SELECT *
              FROM dual;
            -- @formatter:on @formatter:off
            SELECT *
              FROM dual; -- @formatter:on
            SELECT *
              FROM dual;
        '''
        val actual = formatter.format(unformatted)
        Assert.assertEquals(expected.trim(), actual.trim());
    }


}
