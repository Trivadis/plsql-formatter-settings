package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A11_align_parameter_names extends ConfiguredTestFormatter {

    @Nested
    class Commas_before {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().alignTypeDecl, true);
            getFormatter().options.put(getFormatter().alignAssignments, true);
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        }

        @Test
        public void procedure() throws IOException {
            var input = """
                    create procedure p (p1 in integer
                    ,p2 in integer
                    ,p3 in integer)
                    is
                    begin
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create procedure p(p1 in integer
                                     , p2 in integer
                                     , p3 in integer)
                    is
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void procedure_multiline() throws IOException {
            var input = """
                    create procedure p (p1 in integer, p2 in integer
                    ,p3 in integer, p4 in integer) is
                    begin
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create procedure p(p1 in integer, p2 in integer
                                     , p3 in integer, p4 in integer) is
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Commas_after {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().alignTypeDecl, true);
            getFormatter().options.put(getFormatter().alignAssignments, true);
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
        }

        @Test
        public void procedure() throws IOException {
            var input = """
                    create procedure p (p1 in integer,
                    p2 in integer,
                    p3 in integer)
                    is
                    begin
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create procedure p(p1 in integer,
                                       p2 in integer,
                                       p3 in integer)
                    is
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void procedure_multiline() throws IOException {
            var input = """
                    create procedure p (p1 in integer, p2 in integer,
                    p3 in integer, p4 in integer) is
                    begin
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create procedure p(p1 in integer, p2 in integer,
                                       p3 in integer, p4 in integer) is
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }
}
