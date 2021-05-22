package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TvdFormatPathWithSpaceTest extends AbstractSqlclTest {

    @Before
    public void registerCommandBeforeTest() {
        runScript("--register");
        byteArrayOutputStream.reset();
    }

    @Test
    public void path_with_spaces() throws IOException {
        final Path myFilesDir = Files.createTempDirectory("my files");
        final String actual = runCommand("tvdformat \"" + myFilesDir.toAbsolutePath() + "\"");
        // directory is empty, no files processed
        Assert.assertTrue(actual.trim().isEmpty());
    }

}

