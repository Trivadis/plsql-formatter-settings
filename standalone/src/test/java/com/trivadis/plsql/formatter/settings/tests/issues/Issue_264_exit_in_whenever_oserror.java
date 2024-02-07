package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_264_exit_in_whenever_oserror extends ConfiguredTestFormatter {
    @Test
    public void exit_in_whenever_os_error() {
        var input = """
                whenever sqlerror exit failure
                whenever oserror exit failure
                select * from dual;
                """;
        formatAndAssert(input);
    }
}
