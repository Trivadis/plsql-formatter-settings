package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_249_globals_in_package_spec extends ConfiguredTestFormatter {
    @Test
    public void constants_only() throws IOException {
        var input = """
                create or replace package x as
                   co_aaaa constant char := 'a';
                   co_b constant char := 'b';
                   co_cccccccccc constant char := 'c';
                end;
                /
                create or replace package body x as
                   co_aaaa constant char := 'a';
                   co_b constant char := 'b';
                   co_cccccccccc constant char := 'c';
                end;
                /
                """;
        var expected = """
                create or replace package x as
                   co_aaaa       constant char := 'a';
                   co_b          constant char := 'b';
                   co_cccccccccc constant char := 'c';
                end;
                /
                create or replace package body x as
                   co_aaaa       constant char := 'a';
                   co_b          constant char := 'b';
                   co_cccccccccc constant char := 'c';
                end;
                /
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void variables_only() throws IOException {
        var input = """
                create or replace package x as
                   l_aaaa char := 'a';
                   l_b char := 'b';
                   l_cccccccccc char := 'c';
                end;
                /
                create or replace package body x as
                   l_aaaa char := 'a';
                   l_b char := 'b';
                   l_cccccccccc char := 'c';
                end;
                /
                """;
        var expected = """
                create or replace package x as
                   l_aaaa       char := 'a';
                   l_b          char := 'b';
                   l_cccccccccc char := 'c';
                end;
                /
                create or replace package body x as
                   l_aaaa       char := 'a';
                   l_b          char := 'b';
                   l_cccccccccc char := 'c';
                end;
                /

                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void constants_and_variables() throws IOException {
        var input = """
                create or replace package x as
                   co_aaaa constant char := 'a';
                   co_b constant char := 'b';
                   co_cccccccccc constant char := 'c';
                   l_aaaa char := 'a';
                   l_b char := 'b';
                   l_cccccccccc char := 'c';
                end;
                /
                create or replace package body x as
                   co_aaaa constant char := 'a';
                   co_b constant char := 'b';
                   co_cccccccccc constant char := 'c';
                   l_aaaa char := 'a';
                   l_b char := 'b';
                   l_cccccccccc char := 'c';
                end;
                /
                """;
        var expected = """
                create or replace package x as
                   co_aaaa       constant char := 'a';
                   co_b          constant char := 'b';
                   co_cccccccccc constant char := 'c';
                   l_aaaa        char          := 'a';
                   l_b           char          := 'b';
                   l_cccccccccc  char          := 'c';
                end;
                /
                create or replace package body x as
                   co_aaaa       constant char := 'a';
                   co_b          constant char := 'b';
                   co_cccccccccc constant char := 'c';
                   l_aaaa        char          := 'a';
                   l_b           char          := 'b';
                   l_cccccccccc  char          := 'c';
                end;
                /
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }
}
