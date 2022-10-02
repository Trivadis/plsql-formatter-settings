package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Serially_reusable_pragma extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_11_4_tokenized() throws IOException {
        var input = """
                CREATE OR REPLACE PACKAGE bodiless_pkg AUTHID DEFINER IS
                  PRAGMA
                  SERIALLY_REUSABLE
                  ;
                  n NUMBER := 5;
                END;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create or replace package bodiless_pkg authid definer is
                   pragma serially_reusable;
                   n number := 5;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                create
                package
                pkg
                is
                pragma
                restrict_references
                (
                default
                ,
                rnds
                ,
                wnds
                ,
                rnps
                ,
                wnps
                ,
                trust
                )
                ;
                end
                ;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create package pkg
                is
                   pragma restrict_references(default, rnds, wnds, rnps, wnps, trust);
                end;
                """;
        assertEquals(expected, actual);
    }

}
