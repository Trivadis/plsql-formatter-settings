package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A18_indent_comment extends ConfiguredTestFormatter {

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

    @Test
    public void increase_single_line_comment() throws IOException {
        var input = """
                begin
                -- comment
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   -- comment
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }


    @Test
    public void decrease_single_line_comment() throws IOException {
        var input = """
                begin
                    -- comment
                    null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   -- comment
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void keep_single_line_comment() throws IOException {
        var input = """
                begin
                null; -- comment
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   null; -- comment
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

}
