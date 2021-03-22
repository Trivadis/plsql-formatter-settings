package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.Assert;
import org.junit.Test;

public class FormatWrongArgumentTest extends AbstractSqlclTest {

    @Test
    public void no_arguments() {
        final String expected =
            """
            
            missing mandatory <rootPath> argument.
            
            usage: script format.js <rootPath> [options]
            
            mandatory argument: (one of the following)
              <rootPath>      file or path to directory containing files to format (content will be replaced!)
              <config.json>   configuration file in JSON format (must end with .json)
              *               use * to format the SQLcl buffer
            
            options:
              --register, -r  register SQLcl command tvdformat, without processing, no <rootPath> required
              ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
              mext=<ext>      comma separated list of markdown file extensions to process, e.g. ext=md,mdown
              xml=<file>      path to the file containing the xml file for advanced format settings
                              xml=default uses default advanced settings included in sqlcl
                              xml=embedded uses advanced settings defined in format.js
              arbori=<file>   path to the file containing the Arbori program for custom format settings
                              arbori=default uses default Arbori program included in sqlcl

            """;
        final String actual = runScript();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void wrong_root_path() {
        final String actual = this.runScript("/tmp/42");
        Assert.assertTrue(actual.contains("directory /tmp/42 does not exist."));
    }

    @Test
    public void wrong_argument() {
        final String actual = this.runScript(this.tempDir.toString(), "xyz=10");
        Assert.assertTrue(actual.contains("invalid argument xyz=10."));
    }

}
