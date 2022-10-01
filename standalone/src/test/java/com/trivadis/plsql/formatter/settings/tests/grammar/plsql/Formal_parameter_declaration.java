package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Formal_parameter_declaration extends ConfiguredTestFormatter {

    @Test
    public void all_grammar_variants() throws IOException {
        var input = """
                create procedure p (
                p1 integer,
                p2 in integer,
                p3 in number := 3,
                p4 in integer default 4,
                p555 out clob,
                p6 out nocopy clob,
                p7 in out nocopy clob
                ) is
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create procedure p(
                   p1                 integer,
                   p2   in            integer,
                   p3   in            number  := 3,
                   p4   in            integer default 4,
                   p555 out           clob,
                   p6   out nocopy    clob,
                   p7   in out nocopy clob
                ) is
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
