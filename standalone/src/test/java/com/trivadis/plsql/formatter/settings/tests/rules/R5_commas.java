package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class R5_commas extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Commas_before {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.Before);
            setOption(getFormatter().spaceAfterCommas, true);
            setOption(getFormatter().alignRight, false);
        }

        @Test
        public void select_statement_single_line() throws IOException {
            var input = """
                    select a , b , c from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a, b, c from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_space() throws IOException {
            var input = """
                    select a,
                           b,
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                         , b
                         , c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_without_space() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a,
                           b,
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                          ,b
                          ,c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_statement_with_sl_comment_with_space() throws IOException {
            var input = """
                    select a,
                           b, -- single line comment
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                         , b
                         , -- single line comment
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_sl_comment_without_space() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a,
                           b, -- single line comment
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                          ,b
                          , -- single line comment
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_statement_with_ml_comment_with_space() throws IOException {
            var input = """
                    select a,
                           b, /* multi line comment */
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                         , b
                         , /* multi line comment */ c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_ml_comment_without_space() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a,
                           b, /* multi line comment */
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                          ,b
                          , /* multi line comment */c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_statement_with_ml_comment_with_space_before_comma() throws IOException {
            var input = """
                    select a,
                           b /* multi line comment */,
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            // cannot add line break after multi-line comment! SQLDev bug.
            var expected = """
                    select a
                         , b /* multi line comment */
                         , c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_ml_comment_without_space_before_comma() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a,
                           b /* multi line comment */,
                           c
                    from t;
                    """;
            var actual = formatter.format(input);
            // cannot add line break after multi-line comment! SQLDev bug.
            var expected = """
                    select a
                          ,b /* multi line comment */
                          ,c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Commas_after {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksComma, Format.Breaks.After);
            setOption(getFormatter().spaceAfterCommas, true);
            setOption(getFormatter().alignRight, false);
        }

        @Test
        public void select_statement_with_space() throws IOException {
            var input = """
                    select a
                         , b
                         , c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b,
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_without_space() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a
                          ,b
                          ,c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b,
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_statement_with_sl_comment_with_space() throws IOException {
            var input = """
                    select a
                         , b -- single line comment
                         , c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b -- single line comment
                         , c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_sl_comment_without_space() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a
                          ,b -- single line comment
                          ,c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b -- single line comment
                          ,c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_statement_with_ml_comment_with_space() throws IOException {
            var input = """
                    select a
                         , b /* multi line comment */
                         , c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b /* multi line comment */
                         , c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_ml_comment_without_space() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a
                          ,b /* multi line comment */
                          ,c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b /* multi line comment */
                          ,c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_statement_with_ml_comment_with_space_before_comma() throws IOException {
            var input = """
                    select a
                         , b /* multi line comment */
                         , c
                    from t;
                    """;
            var actual = formatter.format(input);
            // cannot add line break after multi-line comment! SQLDev bug.
            var expected = """
                    select a,
                           b /* multi line comment */
                         , c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_ml_comment_without_space_before_comma() throws IOException {
            setOption(getFormatter().spaceAfterCommas, false);
            var input = """
                    select a
                          ,b /* multi line comment */
                          ,c
                    from t;
                    """;
            var actual = formatter.format(input);
            // cannot add line break after multi-line comment! SQLDev bug.
            var expected = """
                    select a,
                           b /* multi line comment */
                          ,c
                    from t;
                    """;
            assertEquals(expected, actual);
            setOption(getFormatter().spaceAfterCommas, true);
        }
    }
}
