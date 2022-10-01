package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Exit_statement extends ConfiguredTestFormatter {

    @Test
    public void tokenized() throws IOException {
        var input = """
                begin
                loop
                exit
                when
                1
                =
                1
                ;
                end
                loop;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   loop
                      exit when 1 = 1;
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }
}
