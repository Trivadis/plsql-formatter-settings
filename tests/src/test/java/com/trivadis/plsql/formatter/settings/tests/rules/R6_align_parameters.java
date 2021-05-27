package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class R6_align_parameters extends ConfiguredTestFormatter {

    @Nested
    class Commas_before {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
            getFormatter().options.put(getFormatter().alignNamedArgs, true);
        }

        @Test
        public void new_line_assoc_parameters_aligned() throws IOException {
            var input = """
                    begin
                       pkg.add(in_key => '1'
                              ,in_value => 'yes'
                              ,in_comment => 'something');
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       pkg.add(
                          in_key     => '1'
                        , in_value   => 'yes'
                        , in_comment => 'something'
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void keep_single_line() throws IOException {
            var input = """
                    begin
                       pkg.add(in_key    =>   '1', in_value    =>   'yes', in_comment    =>   'something');
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       pkg.add(in_key => '1', in_value => 'yes', in_comment => 'something');
                    end;
                    /
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
            getFormatter().options.put(getFormatter().alignNamedArgs, true);
        }

        @Test
        public void new_line_assoc_parameters_aligned() throws IOException {
            var input = """
                    begin
                       pkg.add(in_key => '1',
                          in_value => 'yes',
                          in_comment => 'something');
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       pkg.add(
                          in_key     => '1',
                          in_value   => 'yes',
                          in_comment => 'something'
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void keep_single_line() throws IOException {
            var input = """
                    begin
                       pkg.add(in_key    =>   '1', in_value    =>   'yes', in_comment    =>   'something');
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       pkg.add(in_key => '1', in_value => 'yes', in_comment => 'something');
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void align_closing_paren() throws IOException {
            var input = """
                    select pkg.add(in_key => '1',
                                   in_value => 'yes',
                                   in_comment => 'something') as value
                      from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select pkg.add(
                              in_key     => '1',
                              in_value   => 'yes',
                              in_comment => 'something'
                           ) as value
                      from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void nested_multiline_function() throws IOException {
            var input = """
                    select f1(p1 => '1',
                    p2222 => 'yes',
                    p33 => f2(p1 => '1'
                    || '2'
                    || '3',
                    p222222 => '2')) as value
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select f1(p1    => '1',
                              p2222 => 'yes',
                              p33   => f2(p1      => '1'
                                                     || '2'
                                                     || '3',
                                          p222222 => '2')) as value
                      from t;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Commas_after_dont_align_named_args {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
            getFormatter().options.put(getFormatter().alignNamedArgs, false);
        }

        @Test
        public void new_line_assoc_parameters_aligned() throws IOException {
            var input = """
                    begin
                       pkg.add(in_key => '1',
                          in_value => 'yes',
                          in_comment => 'something');
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       pkg.add(
                          in_key => '1',
                          in_value => 'yes',
                          in_comment => 'something'
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void keep_single_line() throws IOException {
            var input = """
                    begin
                       pkg.add(in_key    =>   '1', in_value    =>   'yes', in_comment    =>   'something');
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       pkg.add(in_key => '1', in_value => 'yes', in_comment => 'something');
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }
}
