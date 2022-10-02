package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_198_indent_return extends ConfiguredTestFormatter {

    @Test
    public void line_break_after_return() throws IOException {
        var input = """
                function bool_to_int(in_bool in boolean) return integer
                      deterministic
                   is
                begin
                   return
                      case
                         when in_bool then
                            1
                         else
                            0
                      end;
                end bool_to_int;
                """;
        var expected = """
                function bool_to_int(in_bool in boolean) return integer
                      deterministic
                   is
                begin
                   return case
                             when in_bool then
                                1
                             else
                                0
                          end;
                end bool_to_int;
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void parenthesis_after_return() {
        var sql = """
                function bool_to_int(in_bool in boolean) return integer
                      deterministic
                   is
                begin
                   return (
                         case
                            when in_bool then
                               1
                            else
                               0
                         end
                      );
                end bool_to_int;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void terse_min_max_select() {
        var sql = """
                select max(min(case
                                  when job = 'CLERK' then
                                     sal
                                  else
                                     null
                               end))
                  from emp;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void verbose_min_max_select() {
        var sql = """
                select max(
                          min(
                             case
                                when job = 'CLERK' then
                                   sal
                                else
                                   null
                             end
                          )
                       )
                  from emp;
                """;
        formatAndAssert(sql);
    }
}
