package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class O9_line_breaks_on_boolean_connectors extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Breaks_Before_and_After {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.BeforeAndAfter);
        }

        @Test
        public void add_breaks() throws IOException {
            var input = """
                    select *
                      from t
                     where a = 1 and b = 2 and (c = 3 or d = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where a = 1
                       and
                           b = 2
                       and
                           (c = 3 or d = 4);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void change_breaks() throws IOException {
            var input = """
                    select *
                      from t
                     where a = 1
                           and b = 2 and
                           (c = 3 or d = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where a = 1
                       and
                           b = 2
                       and
                           (c = 3 or d = 4);
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Breaks_Before {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.Before);
        }

        @Test
        public void add_breaks() throws IOException {
            var input = """
                    select *
                      from t
                     where a = 1 and b = 2 and (c = 3 or d = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where a = 1
                       and b = 2
                       and (c = 3 or d = 4);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void change_breaks() throws IOException {
            var input = """
                    select *
                      from t
                     where a = 1
                           and b = 2 and
                           (c = 3 or d = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where a = 1
                       and b = 2
                       and (c = 3 or d = 4);
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Breaks_After {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.After);
        }

        @Test
        public void add_breaks() throws IOException {
            var input = """
                    select *
                      from t
                     where a = 1 and b = 2 and (c = 3 or d = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where a = 1 and
                           b = 2 and
                           (c = 3 or d = 4);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void change_breaks() throws IOException {
            var input = """
                    select *
                      from t
                     where a = 1
                           and b = 2 and
                           (c = 3 or d = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where a = 1 and
                           b = 2 and
                           (c = 3 or d = 4);
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Breaks_None {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.None);
        }

        @Test
        public void keep_no_breaks() {
            var sql = """
                    select *
                      from t
                     where a = 1 and (b = 2 or b = 2.5) and (c = 3 or d = 4);
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void keep_breaks_before_and_after() {
            var sql = """
                    select *
                      from t
                     where a = 1
                       and
                           (b = 2 or b = 2.5)
                       and
                           (c = 3 or d = 4);
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void keep_breaks_before() {
            var sql = """
                    select *
                      from t
                     where a = 1
                       and (b = 2 or b = 2.5)
                       and (c = 3 or d = 4);
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void keep_breaks_after() {
            var sql = """
                    select *
                      from t
                     where a = 1 and
                           (b = 2 or b = 2.5) and
                           (c = 3 or d = 4);
                    """;
            formatAndAssert(sql);
        }
    }
}
