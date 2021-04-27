package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Paren_expr_list extends ConfiguredTestFormatter {

    @Test
    public void unnamed_parameters() {
        final String sql = 
            """
            select func(a, b, c, 100),
                   func(
                      a,
                      b,
                      c,
                      100,
                      200,
                      300,
                      400,
                      500
                   )
              from dual;
            """; 
        formatAndAssert(sql);
    }

    @Test
    public void named_parameters() {
        final String sql = 
            """
            select func(
                      p1               => a,
                      p2               => b,
                      p300             => c,
                      p40000           => 100,
                      p50              => 200,
                      p6               => 300,
                      p7               => 400,
                      p88888888888888  => 500
                   )
              from dual;
            """; 
        formatAndAssert(sql);
    }

}
