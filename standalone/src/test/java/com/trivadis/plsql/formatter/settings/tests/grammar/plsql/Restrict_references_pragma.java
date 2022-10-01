package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Restrict_references_pragma extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_14_11() throws IOException {
        var input = """
                CREATE OR REPLACE PACKAGE finance AS
                  FUNCTION compound_ (
                    years  IN NUMBER,
                    amount IN NUMBER,
                    rate   IN NUMBER
                   ) RETURN NUMBER;
                  PRAGMA RESTRICT_REFERENCES (compound_, WNDS, WNPS, RNDS, RNPS);
                END finance;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create or replace package finance as
                   function compound_(
                      years  in number,
                      amount in number,
                      rate   in number
                   ) return number;
                   pragma restrict_references(compound_, wnds, wnps, rnds, rnps);
                end finance;
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
