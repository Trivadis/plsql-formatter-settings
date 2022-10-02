package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_68_merge extends ConfiguredTestFormatter {

    @Test
    public void merge_commas_after() {
        var sql = """
                merge into people_target pt
                using people_source ps
                   on (pt.person_id = ps.person_id)
                 when matched then
                      update
                         set pt.first_name = ps.first_name,
                             pt.last_name = ps.last_name,
                             pt.title = ps.title
                      delete
                       where pt.title = 'Mrs.'
                 when not matched then
                      insert (
                         pt.person_id,
                         pt.first_name,
                         pt.last_name,
                         pt.title
                      )
                      values (
                         ps.person_id,
                         ps.first_name,
                         ps.last_name,
                         ps.title
                      )
                       where ps.title = 'Mr';
                """;
        formatAndAssert(sql);
    }

    @Test
    public void merge_commas_before() {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                merge into people_target pt
                using people_source ps
                   on (pt.person_id = ps.person_id)
                 when matched then
                      update
                         set pt.first_name = ps.first_name
                           , pt.last_name = ps.last_name
                           , pt.title = ps.title
                      delete
                       where pt.title = 'Mrs.'
                 when not matched then
                      insert (
                         pt.person_id
                       , pt.first_name
                       , pt.last_name
                       , pt.title
                      )
                      values (
                         ps.person_id
                       , ps.first_name
                       , ps.last_name
                       , ps.title
                      )
                       where ps.title = 'Mr';
                """;
        formatAndAssert(sql);
        setOption(getFormatter().breaksComma, Format.Breaks.After);
    }

    @Test
    public void merge_subquery_commas_after() {
        var sql = """
                merge into people_target pt
                using (
                         select person_id,
                                first_name,
                                last_name,
                                title
                           from people_source
                      ) ps
                   on (pt.person_id = ps.person_id)
                 when matched then
                      update
                         set pt.first_name = ps.first_name,
                             pt.last_name = ps.last_name,
                             pt.title = ps.title
                      delete
                       where pt.title = 'Mrs.'
                 when not matched then
                      insert (
                         pt.person_id,
                         pt.first_name,
                         pt.last_name,
                         pt.title
                      )
                      values (
                         ps.person_id,
                         ps.first_name,
                         ps.last_name,
                         ps.title
                      )
                       where ps.title = 'Mr';
                """;
        formatAndAssert(sql);
    }

    @Test
    public void merge_subquery_commas_before_and_sl_comments() {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                merge into people_target pt
                using (
                         select person_id                -- person identifier
                              , first_name               -- first name
                              , last_name                -- last name
                              , title                    -- title
                           from people_source
                      ) ps
                   on (pt.person_id = ps.person_id)
                 when matched then
                      update
                         set pt.first_name = ps.first_name  -- first_name
                           , pt.last_name = ps.last_name    -- last_name
                           , pt.title = ps.title            -- title
                      delete
                       where pt.title = 'Mrs.'
                 when not matched then
                      insert (
                         pt.person_id                       -- person identifier
                       , pt.first_name                      -- first_name
                       , pt.last_name                       -- last_name
                       , pt.title                           -- title
                      )
                      values (
                         ps.person_id                       -- person identifier
                       , ps.first_name                      -- first_name
                       , ps.last_name                       -- last_name
                       , ps.title                           -- title
                      )
                       where ps.title = 'Mr';
                """;
        formatAndAssert(sql);
        setOption(getFormatter().breaksComma, Format.Breaks.After);
    }
}
