package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class O5_align_column_and_table_aliases extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class True {

        @BeforeAll
        public void setup() {
            getFormatter().options.put(getFormatter().alignTabColAliases, true);
        }

        @Test
        public void column_alias() throws IOException {
            var input = """
                    select a123 as a,
                           b as b
                      from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a123 as a,
                           b    as b
                      from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void column_alias_single_line() {
            var sql = """
                    select a123 as a, b as b
                      from t;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void table_alias() throws IOException {
            var input = """
                    select *
                      from t123 a
                     cross join t456 b;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t123      a
                     cross join t456 b;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void table_alias_single_line() {
            var sql = """
                    select *
                      from t123      a
                     cross join t456 b;
                    """;
            formatAndAssert(sql);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class False {

        @BeforeAll
        public void setup() {
            getFormatter().options.put(getFormatter().alignTabColAliases, false);
        }

        @Test
        public void column_alias() {
            var sql = """
                    select a123 as a,
                           b as b
                      from t;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void table_alias() {
            var sql = """
                    select *
                      from t123 a
                     cross join t456 b;
                    """;
            formatAndAssert(sql);
        }
    }
}
