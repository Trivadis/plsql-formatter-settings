package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Basic_loop_statement extends ConfiguredTestFormatter {

    @Test
    public void tokenized() throws IOException {
        var input = """
                begin
                <
                <
                infinite_loop
                >
                >
                loop
                null
                ;
                end
                loop
                infinite_loop
                ;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   <<infinite_loop>>
                   loop
                      null;
                   end loop infinite_loop;
                end;
                """;
        assertEquals(expected, actual);
    }
}
