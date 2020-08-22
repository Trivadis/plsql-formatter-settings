package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format
import oracle.dbtools.app.Format.BreaksX2
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class Issue_6_max_one_empty_line extends ConfiguredTestFormatter {

    @Before
    def void setup() {
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, BreaksX2.Keep);
        formatter.options.put(formatter.kwCase, Format.Case.NoCaseChange)
    }

    @Test
    def pkg() {
        val input = '''
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
        '''
        val expected = '''
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
        '''.toString.trim
        val actual = formatter.format(input)
        Assert.assertEquals(expected, actual)
    }

}
