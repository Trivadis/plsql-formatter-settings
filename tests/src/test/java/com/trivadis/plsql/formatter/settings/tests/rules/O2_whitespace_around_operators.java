package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O2_whitespace_around_operators extends ConfiguredTestFormatter {

    @Nested
    class True {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().spaceAroundOperators, true);
        }

        @Test
        public void plus_minus_multiply_divide() throws IOException {
            var input = """
                    select a+b-c*d/e from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a + b - c * d / e from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_before() throws IOException {
            var input = """
                    select a
                           +b
                           -c
                           *d
                           /e
                      from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                           + b
                           - c
                           * d
                           / e
                      from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_after() throws IOException {
            var input = """
                    select a+
                           b-
                           c*
                           d/
                           e
                      from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a +
                           b -
                           c *
                           d /
                           e
                      from dual;
                    """;
            assertEquals(expected, actual);
        }

    }

    @Nested
    class False {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().spaceAroundOperators, false);
        }

        @Test
        public void plus_minus_multiply_divide() throws IOException {
            var input = """
                    select a  +  b  -  c  *  d  /  e from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a+b-c*d/e from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_before() throws IOException {
            var input = """
                    select a
                           + b
                           - c
                           * d
                           / e
                      from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                           +b
                           -c
                           *d
                           /e
                      from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_newlines_after() throws IOException {
            var input = """
                    select a +
                           b -
                           c *
                           d /
                           e
                      from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a+
                           b-
                           c*
                           d/
                           e
                      from dual;
                    """;
            assertEquals(expected, actual);
        }
    }
}
