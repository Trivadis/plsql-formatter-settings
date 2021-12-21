package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Raise_statement extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized_raise_exception() throws IOException {
        var input = """
                begin
                raise
                my_exception
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   raise my_exception;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_raise() throws IOException {
        var input = """
                begin
                raise
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   raise;
                end;
                """;
        assertEquals(expected, actual);
    }
}
