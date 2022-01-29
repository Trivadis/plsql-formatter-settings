package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
