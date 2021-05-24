package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A3_formatter_off_on extends ConfiguredTestFormatter {

    @Test
    public void one_stmt_sl_comment_eclipse_style() {
        final String sql =
            """
            select *
              from dual;
            -- @formatter:off
            SELECT
               *
                  from
                     dual
            ; -- @formatter:on
            select *
              from dual;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void one_stmt_ml_comment_eclipse_style() {
        final String sql =
            """
            select *
              from dual;
            /* @formatter:off */
            SELECT
               *
                  from
                     dual
            ; /* @formatter:on */
            select *
              from dual;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void one_stmt_sl_comment_plsqldev_style() {
        final String sql =
            """
            select *
              from dual;
            -- NoFormat Start
            SELECT
               *
                  from
                     dual
            ; -- NoFormat End
            select *
              from dual;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void one_stmt_ml_comment_plsqldev_style() {
        final String sql = 
            """
            select *
              from dual;
            /* NoFormat Start */
            SELECT
               *
                  from
                     dual
            ; /* NoFormat End */
            select *
              from dual;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void two_stmt_mixed_style() {
        final String sql =
                """
                select *
                  from dual;
                /* noformat start, however in SQLDev 20.2 keyword is uppercase nonetheless, indent lost in 20.3 */
                DELETE
                      FrOm\s
                         EmP where a
                                   = b
                ;
                /* @formatter:ON, the next statement is formatted by SQLDev, indent lost in 20.3 */
                delete from emp
                 where dept = 10;
                /* @formatter:OFF, the next statement is not formated by SQLDev */
                UPDATE
                        emp
                      set
                    sal = sal + 10
                   ;
                -- noformat end, after this line everthing is formatted by SQLDev
                select *
                  from emp
                  join dept
                    on dept.deptno = emp.deptno;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void format_if_on_and_off_defined_in_same_comment() throws IOException {
        final String unformatted =
            """
            select * from dual;
            -- @formatter:on @formatter:off
            SELECT *
                  from
                     dual
            ; -- @formatter:on
            select * from dual;
            """.trim();
        final String expected =
            """
            select * from dual;
            -- @formatter:on @formatter:off
            select *
              from dual; -- @formatter:on
            select * from dual;
            """.trim();
        final String actual = formatter.format(unformatted);
        Assertions.assertEquals(expected, actual);
    }

}
