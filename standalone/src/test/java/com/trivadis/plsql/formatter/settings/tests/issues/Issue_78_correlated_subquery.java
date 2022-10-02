package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_78_correlated_subquery extends ConfiguredTestFormatter {

    @Test
    public void subselect_with_commas_after() {
        var sql = """
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
        var sql = """
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
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
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
        setOption(getFormatter().breaksComma, Format.Breaks.After);
    }

    @Test
    public void subselect_with_commas_before_with_alias() {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
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
        setOption(getFormatter().breaksComma, Format.Breaks.After);
    }

    @Test
    public void first_subselect_with_commas_after() {
        var sql = """
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
        var sql = """
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
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                select (
                          select count(1)
                            from user_indexes i
                           where i.table_name = t.table_name
                       )
                     , t.blocks
                  from user_tables t;
                """;
        formatAndAssert(sql);
        setOption(getFormatter().breaksComma, Format.Breaks.After);
    }

    @Test
    public void first_subselect_with_commas_before_with_alias() {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                select (
                          select count(1)
                            from user_indexes i
                           where i.table_name = t.table_name
                       ) as num_indexes
                     , t.blocks
                  from user_tables t;
                """;
        formatAndAssert(sql);
        setOption(getFormatter().breaksComma, Format.Breaks.After);
    }

    @Test
    public void subselect_with_single_select_term() {
        var sql = """
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
        var sql = """
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
