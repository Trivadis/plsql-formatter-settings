package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class R5_commas extends ConfiguredTestFormatter {

    @Nested
    class Commas_before {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
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
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
                         , /* multi line comment */
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_ml_comment_without_space() throws IOException {
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
                          , /* multi line comment */
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
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
                         , b /* multi line comment */,
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_statement_with_ml_comment_without_space_before_comma() throws IOException {
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
                          ,b /* multi line comment */,
                           c
                    from t;
                    """;
            assertEquals(expected, actual);
        }

    }

    @Nested
    class Commas_after {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
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
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
            getFormatter().options.put(getFormatter().spaceAfterCommas, false);
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
        }

    }

}