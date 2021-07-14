package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Paren_expr_list extends ConfiguredTestFormatter {

    @Test
    public void unnamed_parameters() {
        var sql = """
                select func(a, b, c, 100),
                       func(
                          a123456789,
                          b123456789,
                          c123456789,
                          d123456789,
                          e123456789,
                          f123456789,
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
        var sql = """
                select func(
                          p1              => a,
                          p2              => b,
                          p300            => c,
                          p40000          => 100,
                          p50             => 200,
                          p6              => 300,
                          p7              => 400,
                          p88888888888888 => 500
                       )
                  from dual;
                """;
        formatAndAssert(sql);
    }
}
