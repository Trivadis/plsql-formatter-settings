package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_189_indent_private_plsql_unit_in_type extends ConfiguredTestFormatter {

    @Test
    public void plsql_unit_in_constructor() throws IOException {
        var input = """
                create or replace type body my_ot as
                constructor function my_ot(
                self  in out nocopy guess_ot,
                in_p1 in            integer
                ) return self as result is
                procedure private_stuff is
                begin
                null;
                end private_stuff;
                begin
                private_stuff;
                return;
                end my_ot;
                end;
                /
                """;
        var expected = """
                create or replace type body my_ot as
                   constructor function my_ot(
                      self  in out nocopy guess_ot,
                      in_p1 in            integer
                   ) return self as result is
                      procedure private_stuff is
                      begin
                         null;
                      end private_stuff;
                   begin
                      private_stuff;
                      return;
                   end my_ot;
                end;
                /
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
