package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TvdFormatIgnoreTest extends AbstractSqlclTest {

    @BeforeEach
    public void registerCommandBeforeTest() {
        runScript("--register");
        byteArrayOutputStream.reset();
    }

    @Test
    public void ignore_as_as_parameter_file_does_not_exist() {
        var actual = runCommand( "tvdformat " + getTempDir() + " ignore=does_not_exist.txt");
        Assertions.assertTrue(actual.contains("Ignore file does_not_exist.txt does not exist."));
    }

    @Test
    public void ignore_in_json_object_file_does_not_exist() throws IOException {
        var configFileContent = """
                {
                    "files":["#TEMP_DIR#"],
                    "ignore":"does_not_exist.txt"
                }
                """.replace("#TEMP_DIR#", getTempDir().replace("\\","/"));
        final Path configFile = Paths.get(getTempDir() + "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = runCommand( "tvdformat " + getTempDir() + "/config.json ignore=does_not_exist.txt");
        Assertions.assertTrue(actual.contains("Ignore file does_not_exist.txt does not exist."));
    }

    @Test
    public void ignore_two_files() throws IOException {
        var ignoreFileContent = """
                # ignore package bodies
                **/*.pkb
                
                # Ignore files with syntax errors
                **/*syntax*
                """;
        final Path ignoreFile = Paths.get(getTempDir() + "/ignore.txt");
        Files.write(ignoreFile, ignoreFileContent.getBytes());
        var actual = runCommand( "tvdformat " + getTempDir() + " ignore=" + getTempDir() + "/ignore.txt");
        Assertions.assertTrue(actual.contains("2 of 2"));
    }

    @Test
    public void ignore_two_files_windows_separator() throws IOException {
        // must run in an own test class, reason is not clear
        var ignoreFileContent = """
                # ignore package bodies (single backslash)
                **\\*.pkb
                
                # Ignore files with syntax errors (single backslash)
                **\\*syntax*
                """;
        final Path ignoreFile = Paths.get(getTempDir() + "/ignore2.txt");
        Files.write(ignoreFile, ignoreFileContent.getBytes());
        var actual = runCommand( "tvdformat " + getTempDir() + " ignore=" + getTempDir() + "/ignore2.txt");
        Assertions.assertTrue(actual.contains("2 of 2"));
    }
}
