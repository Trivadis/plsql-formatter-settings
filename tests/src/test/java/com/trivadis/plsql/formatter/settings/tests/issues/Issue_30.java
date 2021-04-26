package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Issue_30 extends ConfiguredTestFormatter {

    @Test
    public void into_clause_commas_after() {
        final String sql = 
            """
            select namespace,
                   key,
                   scope
              into l_namespace,
                   l_key,
                   l_scope
              from configuration
             where id = p_id;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void into_clause_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            select namespace
                 , key
                 , scope
              into l_namespace
                 , l_key
                 , l_scope
              from configuration
             where id = p_id;
            """;
        formatAndAssert(sql);
    }
}
