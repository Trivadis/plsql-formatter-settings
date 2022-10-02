package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Rowtype_attribute extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_6_41_simplified_tokenized() throws IOException {
        var input = """
                DECLARE
                dept_rec
                departments
                %
                ROWTYPE
                ;
                BEGIN
                null;
                END;
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   dept_rec departments%rowtype;
                begin
                   null;
                end;
                """;
        assertEquals(expected, actual);
    }

}
