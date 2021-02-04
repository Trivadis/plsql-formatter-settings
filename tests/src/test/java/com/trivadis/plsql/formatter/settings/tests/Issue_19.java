package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_19 extends ConfiguredTestFormatter {

    @Test
    public void arg_list_split_5_or_more_params() {
        final String sql =
            """
            begin
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
            end;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void expr_list_split_5_or_more_params() {
        final String sql =
            """
            select f(),
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
              from dual;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void nested_arg_list() {
        final String sql =
            """
            begin
               f(
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
                  )
               );
            end;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void nested_expr_list() {
        final String sql =
            """
            select f(
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
                      )
                   )
              from dual;
            """;
        formatAndAssert(sql);
    }

}
