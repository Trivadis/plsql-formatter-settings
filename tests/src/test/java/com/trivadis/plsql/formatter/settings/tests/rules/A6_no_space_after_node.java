package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A6_no_space_after_node extends ConfiguredTestFormatter {

    @Test
    public void label() throws IOException {
        var input = """
                begin
                   < < my_label > >
                   null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   <<my_label>>
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
