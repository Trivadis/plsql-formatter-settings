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

        @Test
        public void at_end() throws IOException {
            var input = """
                    begin
                    null;
                    null;
                    end;
                    /
                       -- a comment
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       null;
                       null;
                    end;
                    /
                    -- a comment
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

        @Test
        public void keep_first_indent_only() throws IOException {
            var input = """
                    begin
                       null; /* comment
                     to be indented 1
                    to be indented 2 */
                       null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       null; /* comment
                             to be indented 1
                             to be indented 2 */
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void keep_all_indent() throws IOException {
            var input = """
                    begin
                       null; /* comment
                              to be indented 1
                              to be indented 2 */
                       null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       null; /* comment
                              to be indented 1
                              to be indented 2 */
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }


        @Test
        public void copyright_header() throws IOException {
            var input = """
                    create or replace package pkg as
                      /*
                      Licensed under the Apache License, Version 2.0 (the "License"):
                      you may not use this file except in compliance with the License.
                      You may obtain a copy of the License at
                                        
                          http://www.apache.org/licenses/LICENSE-2.0
                                        
                      Unless required by applicable law or agreed to in writing, software
                      distributed under the License is distributed on an "AS IS" BASIS,
                      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                      See the License for the specific language governing permissions and
                      limitations under the License.
                      */
                    
                      procedure p;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package pkg as
                       /*
                       Licensed under the Apache License, Version 2.0 (the "License"):
                       you may not use this file except in compliance with the License.
                       You may obtain a copy of the License at
                                         
                           http://www.apache.org/licenses/LICENSE-2.0
                                         
                       Unless required by applicable law or agreed to in writing, software
                       distributed under the License is distributed on an "AS IS" BASIS,
                       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                       See the License for the specific language governing permissions and
                       limitations under the License.
                       */
                    
                       procedure p;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void at_end() throws IOException {
            var input = """
                    begin
                    null;
                    null;
                    end;
                    /
                       /*
                        * a comment
                        */
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       null;
                       null;
                    end;
                    /
                    /*
                     * a comment
                     */
                    """;
            assertEquals(expected, actual);
        }
    }
}
