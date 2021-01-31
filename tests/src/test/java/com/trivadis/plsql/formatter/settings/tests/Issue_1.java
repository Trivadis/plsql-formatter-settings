package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_1 extends ConfiguredTestFormatter {

    @Test
    public void json_dot_notation() {
        final String sql =
            """
            CREATE TABLE t (
               c CLOB CHECK ( c IS JSON )
            );
            --
            INSERT INTO t VALUES ( '{accountNumber:123, accountName:"Name", accountType:"A"}' );
            --
            COLUMN accountNumber FORMAT A15
            COLUMN accountName FORMAT A15
            COLUMN accountType FORMAT A10
            --
            SELECT j.c.accountNumber,
                   j.c.accountName,
                   j.c.accountType
              FROM t j
             WHERE j.c.accountType = 'A';
            --\s
            DROP TABLE t PURGE;
            """;
        formatAndAssert(sql);
    }

}
