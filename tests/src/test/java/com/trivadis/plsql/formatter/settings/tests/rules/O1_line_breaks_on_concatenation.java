package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O1_line_breaks_on_concatenation extends ConfiguredTestFormatter {

    @Nested
    class Breaks_Before {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksConcat, Format.Breaks.Before);
        }

        @Test
        public void add_breaks() throws IOException {
            var input = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.' || '12345689.12345689.' || '12345689.12345689.'
                       );
                    end;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.'
                          || '12345689.12345689.'
                          || '12345689.12345689.'
                       );
                    end;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void change_breaks() throws IOException {
            var input = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.' ||
                          '12345689.12345689.' ||
                          '12345689.12345689.'
                       );
                    end;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.'
                          || '12345689.12345689.'
                          || '12345689.12345689.'
                       );
                    end;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Breaks_After {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksConcat, Format.Breaks.After);
        }

        @Test
        public void add_breaks() throws IOException {
            var input = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.' || '12345689.12345689.' || '12345689.12345689.'
                       );
                    end;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.' ||
                          '12345689.12345689.' ||
                          '12345689.12345689.'
                       );
                    end;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void change_breaks() throws IOException {
            var input = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.'
                          || '12345689.12345689.'
                          || '12345689.12345689.'
                       );
                    end;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.' ||
                          '12345689.12345689.' ||
                          '12345689.12345689.'
                       );
                    end;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Breaks_None {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksConcat, Format.Breaks.None);
        }

        @Test
        public void keep_no_breaks() {
            var sql = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.' || '12345689.12345689.' || '12345689.12345689.'
                       );
                    end;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void keep_breaks_after() {
            var sql = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.' ||
                          '12345689.12345689.' ||
                          '12345689.12345689.'
                       );
                    end;
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void keep_breaks_before() {
            var sql = """
                    begin
                       dbms_output.put_line(
                          '12345689.12345689.'
                          || '12345689.12345689.'
                          || '12345689.12345689.'
                       );
                    end;
                    """;
            formatAndAssert(sql);
        }
    }
}
