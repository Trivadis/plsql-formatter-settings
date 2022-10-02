package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class A11_align_parameter_names extends ConfiguredTestFormatter {

    @Nested
    class Commas_before {

        @BeforeEach
        public void setup() {
            setOption(getFormatter().alignTypeDecl, true);
            setOption(getFormatter().alignAssignments, true);
            setOption(getFormatter().breaksComma, Format.Breaks.Before);
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
                    create procedure p(p1 in integer
                                     , p2 in integer
                                     , p3 in integer
                                     , p4 in integer) is
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Commas_after {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().alignTypeDecl, true);
            setOption(getFormatter().alignAssignments, true);
            setOption(getFormatter().breaksComma, Format.Breaks.After);
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
                    create procedure p(p1 in integer,
                                       p2 in integer,
                                       p3 in integer,
                                       p4 in integer) is
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }
}
