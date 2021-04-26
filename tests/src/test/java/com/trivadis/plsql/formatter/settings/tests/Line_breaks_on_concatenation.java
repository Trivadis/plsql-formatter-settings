package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Line_breaks_on_concatenation extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().breaksConcat, Format.Breaks.Before);
    }

    @Test
    public void break_before_concat() throws IOException {
        var input = """
                begin
                   dbms_output.put_line(
                      '1' || '2' || '3'
                   );
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   dbms_output.put_line(
                      '1' 
                      || '2' 
                      || '3'
                   );
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void break_after_concat() throws IOException {
        getFormatter().options.put(getFormatter().breaksConcat, Format.Breaks.After);
        var input = """
                begin
                   dbms_output.put_line(
                      '1' || '2' || '3'
                   );
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   dbms_output.put_line(
                      '1' ||
                      '2' ||
                      '3'
                   );
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void no_break_concat() throws IOException {
        getFormatter().options.put(getFormatter().breaksConcat, Format.Breaks.None);
        var input = """
                begin
                   dbms_output.put_line(
                      '1' || '2' || '3'
                   );
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   dbms_output.put_line(
                      '1' || '2' || '3'
                   );
                end;
                """;
        assertEquals(expected, actual);
    }

}