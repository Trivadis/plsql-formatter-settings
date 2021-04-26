package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Issue_29 extends ConfiguredTestFormatter {

    @Test
    public void commas_after() {
        final String sql = 
            """
            select *
              from dba_tables
             where table_name in (
                      select queue_table
                        from dba_queue_tables
                   )
               and ( owner, table_name ) not in (
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
        final String sql = 
            """
            select *
              from dba_tables
             where table_name in (
                      select queue_table
                        from dba_queue_tables
                   )
               and ( owner, table_name ) not in (
                      select owner
                           , name
                        from dba_snapshots
                   )
               and temporary = 'N'
             order by blocks desc;
            """;
        formatAndAssert(sql);
    }

}
