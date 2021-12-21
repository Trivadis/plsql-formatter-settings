package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Scalar_variable_declaration extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
        getFormatter().options.put(getFormatter().spaceAfterCommas, false);
    }

    @Test
    public void example_3_11() throws IOException {
        // Scalar Variable Declarations
        var input = """
                DECLARE
                  part_number       NUMBER(6);     -- SQL data type
                  part_name         VARCHAR2(20);  -- SQL data type
                  in_stock          BOOLEAN;       -- PL/SQL-only data type
                  part_price        NUMBER(6,2);   -- SQL data type
                  part_description  VARCHAR2(50);  -- SQL data type
                BEGIN
                  NULL;
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   part_number      number(6);     -- SQL data type
                   part_name        varchar2(20);  -- SQL data type
                   in_stock         boolean;       -- PL/SQL-only data type
                   part_price       number(6,2);   -- SQL data type
                   part_description varchar2(50);  -- SQL data type
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                declare
                var
                number
                (
                6
                ,
                2
                )
                not
                null
                :
                =
                12.34
                ;
                begin
                null;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   var number(6,2) not null :=
                      12.34;
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
