package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A10_align_parameter_modes extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().alignTypeDecl, true);
        getFormatter().options.put(getFormatter().alignAssignments, true);
    }


    @Test
    public void procedure() throws IOException {
        var input = """
                create procedure p (
                p1 in integer,
                p22222 out integer,
                p333 in out integer,
                p4 integer
                ) is
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create procedure p(
                   p1     in     integer,
                   p22222 out    integer,
                   p333   in out integer,
                   p4            integer
                ) is
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void procedure_single_line() throws IOException {
        var input = """
                create procedure p (p1 in integer, p22222 out integer, p333 in out integer, p4 integer) is
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create procedure p(p1     in     integer,
                                   p22222 out    integer,
                                   p333   in out integer,
                                   p4            integer) is
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
