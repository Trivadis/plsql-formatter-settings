package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class While_loop_statement extends ConfiguredTestFormatter {

    @Test
    public void example_14_47_tokenized() throws IOException {
        var input = """
                DECLARE
                  done  BOOLEAN := FALSE;
                BEGIN
                  WHILE
                  done
                  LOOP
                  DBMS_OUTPUT
                  .
                  PUT_LINE
                  (
                  'This line does not print.'
                  )
                  ;
                  done
                  :
                  =
                  TRUE;  -- This assignment is not made.
                  END
                  LOOP
                  ;
                                
                  WHILE
                  NOT
                  done
                  LOOP
                  DBMS_OUTPUT
                  .
                  PUT_LINE
                  (
                  'Hello, world!'
                  )
                  ;
                  done
                  :
                  =
                  TRUE;
                  END
                  LOOP
                  ;
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   done boolean := FALSE;
                begin
                   while done
                   loop
                      DBMS_OUTPUT.PUT_LINE('This line does not print.');
                      done :=
                         TRUE;  -- This assignment is not made.
                   end loop;
                                
                   while not done
                   loop
                      DBMS_OUTPUT.PUT_LINE('Hello, world!');
                      done :=
                         TRUE;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
