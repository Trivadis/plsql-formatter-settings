package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_83 extends ConfiguredTestFormatter {

    @Test
    public void select_single_column_with_hint() {
        final String sql = 
            """
            SELECT /*+ parallel(t, 2) */
                   a
              FROM t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void select_two_columns_with_hint() {
        final String sql = 
            """
            SELECT /*+ parallel(t, 2) */
                   a,
                   b
              FROM t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void two_selects_with_hints() {
        final String sql = 
            """
            SELECT /*+ parallel(t, 2) */
                   a
              FROM t;

            SELECT /*+ parallel(t, 2) */
                   a
              FROM t;
            """;
        formatAndAssert(sql);
    }

}
