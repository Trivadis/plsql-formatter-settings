package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Return_statement extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized_return_expression() throws IOException {
        var input = """
                begin
                return
                a
                *
                b
                /
                c
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   return a * b / c;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_return() throws IOException {
        var input = """
                begin
                return
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   return;
                end;
                """;
        assertEquals(expected, actual);
    }
}
