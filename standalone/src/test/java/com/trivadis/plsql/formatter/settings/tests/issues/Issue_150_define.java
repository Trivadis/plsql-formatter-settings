package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import oracle.dbtools.parser.plsql.SyntaxError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_150_define extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup_non_trivadis_default_settings() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void set_define_after_at_command() {
        // in SQLDev 21.2.1 SET is part of the @ command
        // As a result DEFINE becomes a command, and it starts on a new line
        // Same behavior with START as with @.
        // Fixed in SQLDev 21.4.0.
        // Syntax error in SQLcl 23.1.0
        var sql = """
                define table_folder = 'table'
                set define on
                @./demo/&&table_folder/drop_demo_tables.sql
                set define off
                """;
       Assertions.assertThrows(SyntaxError.class, () -> formatAndAssert(sql));
    }

    @Test
    public void commit_after_at_command() {
        // workaround for SQLcl 22.2.1 up to 22.4.0 does not work anymore
        // Syntax error in SQLcl 23.1.0
        // new workaround is to use start instead of @ and no "./"
        var sql = """
                define table_folder = 'table'
                set define on
                start demo/&&table_folder/drop_demo_tables.sql
                set define off
                """;
        formatAndAssert(sql);
    }
}
