package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O5_align_column_and_table_aliases extends ConfiguredTestFormatter {

    @Nested
    class True {

        @BeforeEach
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
        public void table_alias_single_line() throws IOException {
            var sql = """
                    select *
                      from t123 a cross join t456 b;
                    """;
            formatAndAssert(sql);
        }
    }

    @Nested
    class False {

        @BeforeEach
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
