package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.Breaks
import org.junit.Test

class Issue_29 extends ConfiguredTestFormatter {
    
    @Test
    def commas_after() {
        '''
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
        '''.formatAndAssert
    }

    @Test
    def commas_before() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
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
        '''.formatAndAssert
    }

}
