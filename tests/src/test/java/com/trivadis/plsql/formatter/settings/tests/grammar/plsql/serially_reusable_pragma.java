package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class serially_reusable_pragma extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
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
        var actual = formatter.format(input);
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
        var actual = formatter.format(input);
        var expected = """
                create package pkg
                is
                   pragma restrict_references(default, rnds, wnds, rnps, wnps, trust);
                end;
                """;
        assertEquals(expected, actual);
    }

}
