package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_129_in_out_parameter extends ConfiguredTestFormatter {

    @Test
    public void keep_in_out_mode_on_same_line() throws IOException {
        var input = """
                create or replace procedure dummy(
                   a in
                   out varchar2,
                   b in
                   out number
                ) is
                --BEFORE EACH ROW
                begin
                   null;
                end;
                """;
        var expected = """
                create or replace procedure dummy(
                   a in out varchar2,
                   b in out number
                ) is
                --BEFORE EACH ROW
                begin
                   null;
                end;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
