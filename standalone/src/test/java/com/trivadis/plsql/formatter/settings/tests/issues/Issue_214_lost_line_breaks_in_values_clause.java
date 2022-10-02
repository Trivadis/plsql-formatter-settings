package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_214_lost_line_breaks_in_values_clause extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup_non_trivadis_default_settings() {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        setOption(getFormatter().spaceAfterCommas, false);
    }

    @Test
    public void insert() {
        var sql = """
                insert into departments_log (
                   department_id
                  ,department_name
                  ,modification_date)
                values (:old.department_id
                  ,:old.department_name
                  ,sysdate);
                """;
        formatAndAssert(sql);
    }
}
