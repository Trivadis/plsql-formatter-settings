package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_188_remove_line_break_after_update extends ConfiguredTestFormatter {

    @Test
    public void update_tokenized() throws IOException {
        var input = """
                update
                emp
                set
                sal
                =
                sal
                +
                10
                where
                sal
                < 3000
                ;
                """;
        var expected = """
                update emp
                   set sal = sal + 10
                 where sal < 3000;
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }
}
