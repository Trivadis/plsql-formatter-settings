package com.trivadis.plsql.formatter.sqlcl.tests

import org.junit.Assert
import org.junit.Test

class TvdFormatRegisterTest extends AbstractSqlclTest {
    
    @Test
    def void not_registered_feeback() {
        reset();
        val expected = '''
            Error starting at line : 1 in command -
            tvdformat
            Error report -
            Unknown Command
        '''.toString().trim()
        val actual = runCommand("tvdformat").trim()
        Assert.assertEquals(expected, actual);
    }   
    
    @Test
    def void register_feedback() {
        val expected = '''
            tvdformat registered as SQLcl command.
        '''
        val actual = runScript("--register")
        Assert.assertEquals(expected, actual)
        byteArrayOutputStream.reset
        val actual2 = runScript("-r")
        Assert.assertEquals(expected, actual2)
    }

}
