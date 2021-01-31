package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Assert
import org.junit.Test

class Issue_2 extends ConfiguredTestFormatter {
    
    val input = '''
        select * from dual;
        
        -- test comment
        select * from dual;
    '''

    val expected = '''
        SELECT *
          FROM dual;
        
        -- test comment
        SELECT *
          FROM dual;
    '''.toString.trim
    
    def String windows(CharSequence input) {
        input.toString.macos.replace("\n","\r\n")
    }
    
    def String macos(CharSequence input) {
        return input.toString.replace("\r","")
    }
    
    @Test
    def macos() {
        //formatter.options.put(formatter.extraLinesAfterSignificantStatements, Format.BreaksX2.Keep)
        val actual = formatter.format(input.macos)
        Assert.assertEquals(expected.macos, actual.macos)
        val actual2 = formatter.format(actual.macos)
        Assert.assertEquals(expected.macos, actual2.macos)
    }

    @Test
    def windows() {
        //formatter.options.put(formatter.extraLinesAfterSignificantStatements, Format.BreaksX2.Keep)
        val actual = formatter.format(input.windows)
        Assert.assertEquals(expected.windows, actual.windows)
        val actual2 = formatter.format(actual.windows)
        Assert.assertEquals(expected.windows, actual2.windows)
    }

}