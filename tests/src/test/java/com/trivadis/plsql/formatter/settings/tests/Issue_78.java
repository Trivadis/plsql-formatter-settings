package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Test;

public class Issue_78 extends ConfiguredTestFormatter {

    @Test
    public void subselect_with_commas_after() {
        final String sql = 
            """
            SELECT table_name,
                   (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ),
                   t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_commas_after_with_alias() {
        final String sql = 
            """
            SELECT table_name,
                   (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ) AS num_indexes,
                   t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            SELECT table_name
                 , (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   )
                 , t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_commas_before_with_alias() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            SELECT table_name
                 , (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ) AS num_indexes
                 , t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_after() {
        final String sql = 
            """
            SELECT (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ),
                   t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_after_with_alias() {
        final String sql = 
            """
            SELECT (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ) AS num_indexes,
                   t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            SELECT (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   )
                 , t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_before_with_alias() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            SELECT (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ) AS num_indexes
                 , t.BLOCKS
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_single_select_term() {
        final String sql = 
            """
            SELECT (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   )
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_single_select_term_with_alias() {
        final String sql = 
            """
            SELECT (
                      SELECT COUNT(1)
                        FROM user_indexes i
                       WHERE i.table_name = t.table_name
                   ) AS num_indexes
              FROM user_tables t;
            """;
        formatAndAssert(sql);
    }

}
