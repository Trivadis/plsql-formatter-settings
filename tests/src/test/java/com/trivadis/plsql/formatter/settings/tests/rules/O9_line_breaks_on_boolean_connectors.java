package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O9_line_breaks_on_boolean_connectors extends ConfiguredTestFormatter {

    @Nested
    class Breaks_Before_and_After {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.BeforeAndAfter);
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
    class Breaks_Before {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.Before);
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
    class Breaks_After {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.After);
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
    class Breaks_None {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.None);
        }

        @Test
        public void keep_no_breaks() {
            var sql = """
                    select *
                      from t
                     where a = 1 and b = 2 and (c = 3 or d = 4);
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
                           b = 2
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
                       and b = 2
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
                           b = 2 and
                           (c = 3 or d = 4);
                    """;
            formatAndAssert(sql);
        }
    }
}
