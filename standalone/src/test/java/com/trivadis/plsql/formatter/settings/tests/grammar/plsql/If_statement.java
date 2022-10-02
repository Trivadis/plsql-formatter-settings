package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class If_statement extends ConfiguredTestFormatter {

    @Test
    public void example_5_4() throws IOException {
        var input = """
                DECLARE
                  PROCEDURE p (sales NUMBER)
                  IS
                    bonus  NUMBER := 0;
                  BEGIN
                    IF sales > 50000 THEN
                      bonus := 1500;
                    ELSIF sales > 35000 THEN
                      bonus := 500;
                    ELSE
                      bonus := 100;
                    END IF;
                
                    DBMS_OUTPUT.PUT_LINE (
                      'Sales = ' || sales || ', bonus = ' || bonus || '.'
                    );
                  END p;
                BEGIN
                  p(55000);
                  p(40000);
                  p(30000);
                END;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   procedure p(sales number)
                   is
                      bonus number := 0;
                   begin
                      if sales > 50000 then
                         bonus := 1500;
                      elsif sales > 35000 then
                         bonus := 500;
                      else
                         bonus := 100;
                      end if;
                                
                      DBMS_OUTPUT.PUT_LINE(
                         'Sales = '
                         || sales
                         || ', bonus = '
                         || bonus
                         || '.'
                      );
                   end p;
                begin
                   p(55000);
                   p(40000);
                   p(30000);
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
