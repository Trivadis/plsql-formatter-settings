package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Open_statement extends ConfiguredTestFormatter {

    @Test
    public void tokenized_open_cursor_without_params() throws IOException {
        var input = """
                begin
                open
                c1
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_open_cursor_positional_params() throws IOException {
        var input = """
                begin
                open
                c1
                (
                1
                ,
                '2'
                ,
                a
                )
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1(1, '2', a);
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_open_cursor_named_params() throws IOException {
        var input = """
                begin
                open
                c1
                (
                one
                =
                >
                1
                ,
                two
                =
                >
                '2'
                ,
                three
                =
                >
                a
                )
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1(
                      one   => 1,
                      two   => '2',
                      three => a
                   );
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void single_line_open_cursor_named_params() throws IOException {
        var input = """
                begin open c1(one=>1,two=>'2',three=>a);end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1(one => 1, two => '2', three => a);
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_open_cursor_mixed_params() throws IOException {
        var input = """
                begin
                open
                c1
                (
                1
                ,
                two
                =
                >
                '2'
                ,
                three
                =
                >
                a
                )
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1(
                      1,
                      two   => '2',
                      three => a
                   );
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void single_line_open_cursor_mixed_params() throws IOException {
        var input = """
                begin open c1(1,two=>'2',three=>a);end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1(1, two => '2', three => a);
                end;
                """;
        assertEquals(expected, actual);
    }
}
