package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_266_line_break_on_conversion_error extends ConfiguredTestFormatter {
    @Test
    public void subquery_with_on_conversion_error() {
        var input = """
                select *
                  from (
                          select to_number(
                                    sys_context('userenv', 'current_schemaid') default null on conversion error,
                                    '99999990',
                                    q'[nls_numeric_characters='.,']'
                                 ) as model_id
                            from dual
                       );
                """;
        formatAndAssert(input);
    }

    @Test
    public void subquery_with_on_conversion_error_with_line_breaks() {
        var input = """
                select *
                  from (
                          select to_number(
                                    sys_context('userenv', 'current_schemaid')
                                    default
                                    null
                                    on
                                    conversion
                                    error,
                                    '99999990',
                                    q'[nls_numeric_characters='.,']'
                                 ) as model_id
                            from dual
                       );
                """;
        formatAndAssert(input);
    }

}
