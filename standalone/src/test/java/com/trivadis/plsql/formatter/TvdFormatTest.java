package com.trivadis.plsql.formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TvdFormatTest extends AbstractTvdFormatTest {

    @Test
    public void jsonArrayDirTest() throws ScriptException, IOException {
        final String configFileContent = "[\"" + tempDir.toString() + "\"]";
        final Path configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        String[] args = new String[]{tempDir + File.separator + "config.json",
                "xml=" + tempDir + File.separator + "trivadis_advanced_format.xml",
                "arbori=" + tempDir + File.separator + "trivadis_custom_format.arbori"};
        TvdFormat.main(args);
        String expected = getExpectedContent("query.sql");
        String actual = getFormattedContent("query.sql");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void jsonArrayFileTest() throws ScriptException, IOException {
        final String configFileContent = "[\"" + tempDir.toString() + File.separator + "query.sql\"]";
        final Path configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        String[] args = new String[]{tempDir + File.separator + "config.json",
                "xml=" + tempDir + File.separator + "trivadis_advanced_format.xml",
                "arbori=" + tempDir + File.separator + "trivadis_custom_format.arbori"};
        TvdFormat.main(args);
        String expected = getExpectedContent("query.sql");
        String actual = getFormattedContent("query.sql");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void jsonObjectFileTest() throws ScriptException, IOException {
        final String configFileContent = "{\n"
                + "    \"xml\": \"" + tempDir + File.separator + "trivadis_advanced_format.xml\",\n"
                + "    \"arbori\": \"" + tempDir + File.separator + "trivadis_custom_format.arbori\",\n"
                + "    \"files\": [\"" + tempDir.toString() + File.separator + "query.sql\"]\n"
                + "}";
        final Path configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        String[] args = new String[]{tempDir + File.separator + "config.json"};
        TvdFormat.main(args);
        String expected = getExpectedContent("query.sql");
        String actual = getFormattedContent("query.sql");
        Assertions.assertEquals(expected, actual);
    }
}
