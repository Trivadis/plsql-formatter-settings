package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Test;

public class Issue_29 extends ConfiguredTestFormatter {

    @Test
    public void commas_after() {
        final String sql = 
            """
            SELECT *
              FROM dba_tables
             WHERE table_name IN (
                      SELECT queue_table
                        FROM dba_queue_tables
                   )
               AND ( owner, table_name ) NOT IN (
                      SELECT owner,
                             name
                        FROM dba_snapshots
                   )
               AND temporary = 'N'
             ORDER BY blocks DESC;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            SELECT *
              FROM dba_tables
             WHERE table_name IN (
                      SELECT queue_table
                        FROM dba_queue_tables
                   )
               AND ( owner, table_name ) NOT IN (
                      SELECT owner
                           , name
                        FROM dba_snapshots
                   )
               AND temporary = 'N'
             ORDER BY blocks DESC;
            """;
        formatAndAssert(sql);
    }

}
