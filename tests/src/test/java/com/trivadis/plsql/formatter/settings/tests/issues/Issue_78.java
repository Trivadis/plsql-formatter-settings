package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Issue_78 extends ConfiguredTestFormatter {

    @Test
    public void subselect_with_commas_after() {
        final String sql = 
            """
            select table_name,
                   (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   ),
                   t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_commas_after_with_alias() {
        final String sql = 
            """
            select table_name,
                   (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   ) as num_indexes,
                   t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            select table_name
                 , (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   )
                 , t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_commas_before_with_alias() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            select table_name
                 , (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   ) as num_indexes
                 , t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_after() {
        final String sql = 
            """
            select (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   ),
                   t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_after_with_alias() {
        final String sql = 
            """
            select (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   ) as num_indexes,
                   t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            select (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   )
                 , t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void first_subselect_with_commas_before_with_alias() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            select (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   ) as num_indexes
                 , t.blocks
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_single_select_term() {
        final String sql = 
            """
            select (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   )
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void subselect_with_single_select_term_with_alias() {
        final String sql = 
            """
            select (
                      select count(1)
                        from user_indexes i
                       where i.table_name = t.table_name
                   ) as num_indexes
              from user_tables t;
            """;
        formatAndAssert(sql);
    }

}
