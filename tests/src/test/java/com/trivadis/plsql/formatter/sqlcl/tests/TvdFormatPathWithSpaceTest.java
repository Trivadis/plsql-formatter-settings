package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TvdFormatPathWithSpaceTest extends AbstractSqlclTest {

    @BeforeEach
    public void registerCommandBeforeTest() {
        runScript("--register");
        byteArrayOutputStream.reset();
    }

    @Test
    public void path_with_spaces() throws IOException {
        final Path myFilesDir = Files.createTempDirectory("my files");
        final String actual = runCommand("tvdformat \"" + myFilesDir.toAbsolutePath() + "\"");
        // directory is empty, no files processed
        Assertions.assertTrue(actual.trim().isEmpty());
    }

}

