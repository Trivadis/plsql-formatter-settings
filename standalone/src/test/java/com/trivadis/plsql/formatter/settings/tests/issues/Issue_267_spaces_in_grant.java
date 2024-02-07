package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_267_spaces_in_grant extends ConfiguredTestFormatter {
    @Test
    public void keep_spaces_in_grant_statements() {
        var input = """
                grant   debug     connect       session   to scott;
                grant   debug     any         procedure   to scott;
                grant   execute   on    dbms_debug_jdwp   to scott;
                """;
        formatAndAssert(input);
    }

    @Test
    public void keep_spaces_in_grant_statements_one_token_per_line() {
        var input = """
                grant
                debug
                connect
                session
                to
                scott;

                grant
                debug
                any
                procedure
                to
                scott;

                grant
                execute
                on
                dbms_debug_jdwp
                to
                scott;
                """;
        formatAndAssert(input);
    }
}
