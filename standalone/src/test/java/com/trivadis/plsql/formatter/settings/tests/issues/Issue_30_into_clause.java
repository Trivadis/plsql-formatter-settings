package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_30_into_clause extends ConfiguredTestFormatter {

    @Test
    public void into_clause_commas_after() {
        var sql = """
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
        var sql = """
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
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
    }
}
