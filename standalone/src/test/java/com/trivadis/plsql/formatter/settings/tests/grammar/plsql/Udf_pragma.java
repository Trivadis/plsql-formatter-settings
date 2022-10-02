package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Udf_pragma extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                create function f(in_str in varchar2) return varchar2
                as
                pragma
                udf
                ;
                begin
                   return upper(in_str);
                end;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create function f(in_str in varchar2) return varchar2
                as
                   pragma udf;
                begin
                   return upper(in_str);
                end;
                /
                """;
        assertEquals(expected, actual);
    }

}
