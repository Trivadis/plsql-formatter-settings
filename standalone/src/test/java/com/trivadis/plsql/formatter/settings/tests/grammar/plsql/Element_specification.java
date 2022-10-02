package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        var actual = getFormatter().format(input);
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

    @Test
    public void attributes_aligned() {
        var sql = """
                create type demo_typ as object (
                   aaa      number,
                   bbbb     varchar2(20),
                   cccccccc date,
                   member function xyz return varchar2
                );
                """;
        formatAndAssert(sql);
    }

}
