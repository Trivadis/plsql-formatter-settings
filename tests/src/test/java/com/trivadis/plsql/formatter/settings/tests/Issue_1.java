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
            --
            insert into t values ( '{accountNumber:123, accountName:"Name", accountType:"A"}' );
            --
            column accountnumber format a15
            column accountname format a15
            column accounttype format a10
            --
            select j.c.accountnumber,
                   j.c.accountname,
                   j.c.accounttype
              from t j
             where j.c.accounttype = 'a';
            --\s
            drop table t purge;
            """;
        formatAndAssert(sql);
    }

}
