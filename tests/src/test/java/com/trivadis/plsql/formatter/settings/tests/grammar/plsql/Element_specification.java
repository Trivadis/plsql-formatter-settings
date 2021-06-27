package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Element_specification extends ConfiguredTestFormatter {

    @Test
    public void example_14_30() throws IOException {
        var input = """
                create
                type
                demo_typ2
                as
                object
                (
                a1
                number
                ,
                member
                function
                get_square
                return
                number
                );
                """;
        var actual = formatter.format(input);
        var expected = """
                create type demo_typ2
                as
                object
                (
                   a1 number,
                   member function get_square return number
                );
                """;
        assertEquals(expected, actual);
    }
}
