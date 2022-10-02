package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Raise_statement extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
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
        var actual = getFormatter().format(input);
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
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   raise;
                end;
                """;
        assertEquals(expected, actual);
    }
}
