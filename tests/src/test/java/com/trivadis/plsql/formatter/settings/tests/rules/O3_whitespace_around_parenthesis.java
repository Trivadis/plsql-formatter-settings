package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O3_whitespace_around_parenthesis extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.None);
    }

    @Nested
    class Default {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().spaceAroundBrackets, Format.Space.Default);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count (*)  from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in  (1, 2, 3, 4, 5)  and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Inside {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().spaceAroundBrackets, Format.Space.Inside);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count( * )from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in( 1, 2, 3, 4, 5 )and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in(
                              1, 2, 3, 4, 5
                           )and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Outside {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().spaceAroundBrackets, Format.Space.Outside);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count (*) from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in (1, 2, 3, 4, 5) and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in (
                              1, 2, 3, 4, 5
                           ) and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class NoSpace {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().spaceAroundBrackets, Format.Space.NoSpace);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count(*)from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in(1, 2, 3, 4, 5)and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in(
                              1, 2, 3, 4, 5
                           )and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }
}
