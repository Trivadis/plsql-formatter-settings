package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A18_indent_comment extends ConfiguredTestFormatter {

    @Nested
    class sqldev_bug_fix {

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

    @Nested
    class single_line_comment {

        @Test
        public void increase_indent() throws IOException {
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
        public void decrease_indent() throws IOException {
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
        public void keep_indent() throws IOException {
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

    @Nested
    class multi_line_comment {

        @Test
        public void increase_indent() throws IOException {
            var input = """
                    begin
                    /* ------------------------
                     * comment
                     * more comment
                     * ------------------------ */
                    commit;
                                    
                    /*
                       comment
                       more comment
                    */
                    rollback;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       /* ------------------------
                        * comment
                        * more comment
                        * ------------------------ */
                       commit;

                       /*
                          comment
                          more comment
                       */
                       rollback;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void decrease_indent() throws IOException {
            var input = """
                    begin
                        /* ------------------------
                         * comment
                         * more comment
                         * ------------------------ */
                       commit;

                          /*
                             comment
                             more comment
                          */
                       rollback;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       /* ------------------------
                        * comment
                        * more comment
                        * ------------------------ */
                       commit;

                       /*
                          comment
                          more comment
                       */
                       rollback;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void keep_indent() throws IOException {
            var input = """
                    begin
                    null; /* comment */
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       null; /* comment */
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }
}
