package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_29_not_in extends ConfiguredTestFormatter {

    @Test
    public void commas_after() {
        var sql = """
                select *
                  from dba_tables
                 where table_name in (
                          select queue_table
                            from dba_queue_tables
                       )
                   and (owner, table_name) not in (
                          select owner,
                                 name
                            from dba_snapshots
                       )
                   and temporary = 'N'
                 order by blocks desc;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                select *
                  from dba_tables
                 where table_name in (
                          select queue_table
                            from dba_queue_tables
                       )
                   and (owner, table_name) not in (
                          select owner
                               , name
                            from dba_snapshots
                       )
                   and temporary = 'N'
                 order by blocks desc;
                """;
        formatAndAssert(sql);
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
    }
}
