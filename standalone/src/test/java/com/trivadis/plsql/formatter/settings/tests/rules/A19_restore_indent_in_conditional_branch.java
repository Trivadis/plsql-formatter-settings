package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A19_restore_indent_in_conditional_branch extends ConfiguredTestFormatter {

    @Test
    public void in_package_body() throws IOException {
        // 4 space indentation
        var input = """
                create or replace package body pkg is
                    $IF TRUE $THEN
                        PROCEDURE P1 IS
                        BEGIN
                            NULL;
                        END;
                    $END
                    
                    procedure p2 is
                    begin
                        null;
                    end;
                end;
                /
                """;
        var actual = formatter.format(input);
        // 3 space indentation, applied only for p2
        // keywords are changed to lowercase in conditional compilation block
        var expected = """
                create or replace package body pkg is
                    $IF TRUE $THEN
                        procedure P1 is
                        begin
                            null;
                        end;
                    $END
                    
                   procedure p2 is
                   begin
                      null;
                   end;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
