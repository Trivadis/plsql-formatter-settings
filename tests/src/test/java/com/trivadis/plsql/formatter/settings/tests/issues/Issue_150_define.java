package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue_150_define extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup_non_trivadis_default_settings() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    @Disabled("SQLDev 21.2.1 bug")
    public void set_define_after_at_command() {
        // in SQLDev 21.2.1 SET is part of the @ command
        // As a result DEFINE becomes a command, and it starts on a new line
        // Same behavior with START as with @.
        var sql = """
                define table_folder = 'table'
                set define on
                @./demo/&&table_folder/drop_demo_tables.sql
                set define off
                """;
        formatAndAssert(sql);
    }

    @Test
    public void commit_after_at_command() {
        // workaround
        var sql = """
                define table_folder = 'table'
                set define on
                @./demo/&&table_folder/drop_demo_tables.sql
                commit; -- workaround
                set define off
                """;
        formatAndAssert(sql);
    }
}
