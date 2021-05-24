package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A6_no_space_after_node extends ConfiguredTestFormatter {

    @Test
    public void label() throws IOException {
        final String input =
            """
            begin
               < < my_label > >
               null;
            end;
            /
            """;
        var actual = formatter.format(input);
        final String expected =
            """
            begin
               <<my_label>>
               null;
            end;
            /
            """;
        assertEquals(expected, actual);
    }
}
