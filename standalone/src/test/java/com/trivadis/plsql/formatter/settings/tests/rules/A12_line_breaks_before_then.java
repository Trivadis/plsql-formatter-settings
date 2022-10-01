package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @Test
    public void searched_case_expression() throws IOException {
        var input = """
                select case
                          when a = 1 and c = 124 then
                             b
                          when a = 2 then
                             c
                          else
                             d
                       end,
                       dummy
                  from dual;
                """;
        var expected = """
                select case
                          when a = 1
                             and c = 124
                          then
                             b
                          when a = 2 then
                             c
                          else
                             d
                       end,
                       dummy
                  from dual;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

}
