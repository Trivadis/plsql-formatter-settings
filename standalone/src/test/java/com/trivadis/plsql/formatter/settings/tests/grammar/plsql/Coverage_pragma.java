package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Coverage_pragma extends ConfiguredTestFormatter {

    @Test
    public void example_13_7() throws IOException {
        var input = """
                begin
                   if (x > 0) then
                      y := 2;
                   else
                      pragma
                      coverage
                      (
                      'NOT_FEASIBLE'
                      )
                      ; -- 1
                      z := 3;
                   end if;
                   if (y > 0) then
                      z := 2;
                   else
                      pragma
                      coverage
                      (
                      'NOT_FEASIBLE'
                      )
                      ; -- 2
                      z := 3;
                   end if;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   if (x > 0) then
                      y := 2;
                   else
                      pragma coverage ('NOT_FEASIBLE'); -- 1
                      z := 3;
                   end if;
                   if (y > 0) then
                      z := 2;
                   else
                      pragma coverage ('NOT_FEASIBLE'); -- 2
                      z := 3;
                   end if;
                end;
                """;
        assertEquals(expected, actual);
    }
}
