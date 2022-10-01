package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Goto_statement extends ConfiguredTestFormatter {

    @Test
    public void example_14_31() throws IOException {
        var input = """
                DECLARE
                  p  VARCHAR2(30);
                  n  PLS_INTEGER := 37;
                BEGIN
                  FOR j in 2..ROUND(SQRT(n)) LOOP
                    IF n MOD j = 0 THEN
                      p := ' is not a prime number';
                      GOTO print_now;
                    END IF;
                  END LOOP;
                                
                  p := ' is a prime number';
                
                  <<print_now>>
                  DBMS_OUTPUT.PUT_LINE(TO_CHAR(n) || p);
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   p varchar2(30);
                   n pls_integer := 37;
                begin
                   for j in 2..ROUND(SQRT(n))
                   loop
                      if n mod j = 0 then
                         p := ' is not a prime number';
                         goto print_now;
                      end if;
                   end loop;
                                
                   p := ' is a prime number';
                                
                   <<print_now>>
                   DBMS_OUTPUT.PUT_LINE(TO_CHAR(n) || p);
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void goto_tokenized() throws IOException {
        var input = """
                begin
                goto
                target;
                do_not_execute_this
                ;
                <
                <
                target
                >
                >
                null
                ;
                end
                ;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   goto target;
                   do_not_execute_this;
                   <<target>>
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
