package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_83_nested_function_calls extends ConfiguredTestFormatter {

    @Test
    public void select_single_column_with_hint() {
        final String sql = 
            """
            select /*+ parallel(t, 2) */
                   a
              from t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void select_two_columns_with_hint() {
        final String sql = 
            """
            select /*+ parallel(t, 2) */
                   a,
                   b
              from t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void two_selects_with_hints() {
        final String sql = 
            """
            select /*+ parallel(t, 2) */
                   a
              from t;

            select /*+ parallel(t, 2) */
                   a
              from t;
            """;
        formatAndAssert(sql);
    }

}
