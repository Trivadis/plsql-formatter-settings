package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Function_declaration_and_definition extends ConfiguredTestFormatter {

    @Test
    public void example_9_2() throws IOException {
        var input = """
                DECLARE
                  -- Declare and define function
                                
                  FUNCTION square (original NUMBER)   -- parameter list
                    RETURN NUMBER                     -- RETURN clause
                  AS
                                                      -- Declarative part begins
                    original_squared NUMBER;
                  BEGIN                               -- Executable part begins
                    original_squared := original * original;
                    RETURN original_squared;          -- RETURN statement
                  END;
                BEGIN
                  DBMS_OUTPUT.PUT_LINE(square(100));  -- invocation
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   -- Declare and define function
                                
                   function square(original number)   -- parameter list
                      return number                     -- RETURN clause
                   as
                      -- Declarative part begins
                      original_squared number;
                   begin                               -- Executable part begins
                      original_squared := original * original;
                      return original_squared;          -- RETURN statement
                   end;
                begin
                   DBMS_OUTPUT.PUT_LINE(square(100));  -- invocation
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
