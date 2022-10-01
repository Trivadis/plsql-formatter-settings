package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Pipe_row_statement extends ConfiguredTestFormatter {

    @Test
    public void tokenized_pipe_row() throws IOException {
        var input = """
                begin
                pipe
                row
                (
                some_row
                )
                ;
                end
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   pipe row (some_row);
                end;
                """;
        assertEquals(expected, actual);
    }
}
