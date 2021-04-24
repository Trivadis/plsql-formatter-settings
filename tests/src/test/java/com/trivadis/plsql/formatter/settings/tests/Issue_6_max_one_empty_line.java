package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_6_max_one_empty_line extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.Keep);
        getFormatter().options.put(getFormatter().kwCase, Format.Case.NoCaseChange);
    }

    @Test
    @Disabled("LightweightFormatter")
    public void pkg() throws IOException {
        final String input =
            """
            /*
             * comment before pkg
             */
            create or replace package pkg is
               
                 
                /*
                 * pkg
                 */
                      
                               
                                              
                /*
                 * p1
                 */
                procedure p1;
                procedure p2;
                
                
                procedure p3;
                
                
                    -- p4 (1)
                    
                 
                    
                  -- p4 (2)
                
                
                
                procedure p4;
                
                
                -- p5
                procedure p5;
                
            end pkg;
            /
            """;
        final String expected = 
            """
            /*
             * comment before pkg
             */
            create or replace package pkg is

                /*
                 * pkg
                 */

                /*
                 * p1
                 */
               procedure p1;
               procedure p2;

               procedure p3;

                    -- p4 (1)

                  -- p4 (2)

               procedure p4;

                -- p5
               procedure p5;

            end pkg;
            /
            """.trim();
        final String actual = getFormatter().format(input);
        Assertions.assertEquals(expected, actual);
    }

}
