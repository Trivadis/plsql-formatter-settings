package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Type_attribute extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_3_15_tokenized() throws IOException {
        var input = """
                DECLARE
                  surname
                  employees
                  .
                  last_name
                  %
                  TYPE
                  ;
                BEGIN
                  DBMS_OUTPUT.PUT_LINE('surname=' || surname);
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   surname employees.last_name%type;
                begin
                   dbms_output.put_line('surname=' || surname);
                end;
                /
                """;
        assertEquals(expected, actual);
    }

}
