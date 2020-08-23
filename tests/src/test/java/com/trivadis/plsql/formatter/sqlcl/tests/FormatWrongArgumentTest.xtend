package com.trivadis.plsql.formatter.sqlcl.tests

import org.junit.Assert
import org.junit.Test

class WrongArgumentTest extends AbstractSqlclTest {
    
    @Test
    def void no_arguments() {
        val expected = '''
            
            format.js for SQLcl 20.2
            Copyright 2020 by Philipp Salvisberg (philipp.salvisberg@trivadis.com)
            
            missing mandatory <rootPath> argument.
            
            usage: script format.js <rootPath> [options]
            
            mandatory arguments:
              <rootPath>     path to directory containing files to format (content will be replaced!)
            
            options:
              ext=<ext>      comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
              xml=<file>     path to the file containing the xml file for advanced format settings
                             xml=default uses default advanced settings included in sqlcl
                             xml=embedded uses advanced settings defined in format.js
              arbori=<file>  path to the file containing the Arbori program for custom format settings
                             arbori=default uses default Arbori program included in sqlcl
            
        '''
        val actual = runScript()
        Assert.assertEquals(expected, actual)
    }

    @Test
    def void wrong_root_path() {
        val actual = runScript("/tmp/42")
        Assert.assertTrue(actual.contains("directory /tmp/42 does not exist."))
    }

    @Test
    def void wrong_argument() {
        val actual = runScript(tempDir.toString(), "xyz=10")
        Assert.assertTrue(actual.contains("invalid argument xyz=10."))
    }

}
