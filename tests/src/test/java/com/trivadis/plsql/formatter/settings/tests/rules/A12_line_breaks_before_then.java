package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A12_line_breaks_before_then extends ConfiguredTestFormatter {

    @Test
    public void if_statement() throws IOException {
        var input = """
                begin
                   if b = 100
                      and a in (
                         '1-123456789', '2-123456789.', '3-123456789.',
                         '4-123456789.', '5-123456789.', '6-123456789.'
                      ) then
                      do_x;
                   elsif a = '42'
                   then
                      do_y;
                   else
                      do_z;
                   end if;
                end;
                /
                """;
        var expected = """
                begin
                   if b = 100
                      and a in (
                         '1-123456789', '2-123456789.', '3-123456789.',
                         '4-123456789.', '5-123456789.', '6-123456789.'
                      )
                   then
                      do_x;
                   elsif a = '42' then
                      do_y;
                   else
                      do_z;
                   end if;
                end;
                /
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
