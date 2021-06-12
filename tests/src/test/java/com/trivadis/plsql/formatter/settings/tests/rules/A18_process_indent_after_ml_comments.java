package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A18_process_indent_after_ml_comments extends ConfiguredTestFormatter {

    @Test
    public void add_newline_after_ml_comment() throws IOException {
        var input = """
                begin /* comment 1 */ null; /* comment 2 */ null; end; /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin /* comment 1 */
                   null; /* comment 2 */
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
