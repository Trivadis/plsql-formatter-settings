package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Cursor_variable_declaration extends ConfiguredTestFormatter {

    @Test
    public void ref_cursor() throws IOException {
        var input = """
                declare
                type
                cur_type
                is
                ref
                cursor
                return
                myschema
                .
                mytable
                %
                rowtype
                ;
                c_cur
                is
                cur_type
                ;
                begin
                null;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type cur_type is
                      ref cursor return myschema.mytable%rowtype;
                   c_cur is cur_type;
                begin
                   null;
                end;
                """;
        assertEquals(expected, actual);
    }
}
