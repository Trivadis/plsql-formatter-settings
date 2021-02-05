package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_1 extends ConfiguredTestFormatter {

    @Test
    public void json_dot_notation() {
        final String sql =
            """
            create table t (
               c clob check ( c is json )
            );
            
            insert into t values ( '{accountNumber:123, accountName:"Name", accountType:"A"}' );
            
            column accountNumber format a15
            column accountName format a15
            column accountType format a10
            
            select j.c.accountNumber,
                   j.c.accountName,
                   j.c.accountType
              from t j
             where j.c.accountType = 'A';
            
            drop table t purge;
            """;
        formatAndAssert(sql);
    }

}
