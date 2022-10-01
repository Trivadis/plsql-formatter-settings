package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Block extends ConfiguredTestFormatter {

    @Test
    public void labelled_body() {
        var sql = """
                <<empty>>
                begin
                   <<null>> null;
                end empty;
                """;
        formatAndAssert(sql);
    }
}
