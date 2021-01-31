package com.trivadis.plsql.formatter.sqlcl.tests

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TvdFormatWrongArgumentTest extends AbstractSqlclTest {
    
    @Before
    def void registerCommandBeforeTest() {
        runScript("--register")
        byteArrayOutputStream.reset
    }
    
    @Test
    def void no_arguments() {
        val expected = '''
            
            missing mandatory <rootPath> argument.
            
            usage: tvdformat <rootPath> [options]
            
            mandatory arguments:
              <rootPath>      file or path to directory containing files to format (content will be replaced!)
                              use * to format the SQLcl buffer
            
            options:
              ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
              mext=<ext>      comma separated list of markdown file extensions to process, e.g. ext=md,mdown
              xml=<file>      path to the file containing the xml file for advanced format settings
                              xml=default uses default advanced settings included in sqlcl
                              xml=embedded uses advanced settings defined in format.js
              arbori=<file>   path to the file containing the Arbori program for custom format settings
                              arbori=default uses default Arbori program included in sqlcl

        '''
        val actual = runCommand("Tvdformat")
        Assert.assertEquals(expected, actual)
    }

    @Test
    def void wrong_root_path() {
        val actual = runCommand("tvdformat /tmp/42")
        Assert.assertTrue(actual.contains("directory /tmp/42 does not exist."))
    }

    @Test
    def void wrong_argument() {
        val actual = runCommand("tvdformat " + tempDir.toString() +  " xyz=10")
        Assert.assertTrue(actual.contains("invalid argument xyz=10."))
    }

}
