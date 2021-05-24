package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

public class TvdFormatPathWithSpaceTest extends AbstractSqlclTest {

    @BeforeEach
    public void registerCommandBeforeTest() {
        runScript("--register");
        byteArrayOutputStream.reset();
    }

    @Test
    public void path_with_spaces() throws IOException {
        var myFilesDir = Files.createTempDirectory("my files");
        var actual = runCommand("tvdformat \"" + myFilesDir.toAbsolutePath() + "\"");
        // directory is empty, no files processed
        Assertions.assertTrue(actual.trim().isEmpty());
    }
}
