package com.trivadis.plsql.formatter.standalone.tests;

import com.trivadis.plsql.formatter.TvdFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TvdFormatTest extends AbstractTvdFormatTest {

    @Test
    public void jsonArrayDirTest() throws ScriptException, IOException {
        var configFileContent = """
                [
                    "#TEMP_DIR#"
                ]
                """.replace("#TEMP_DIR#", tempDir.toString());
        var configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{tempDir + File.separator + "config.json",
                "xml=" + tempDir + File.separator + "trivadis_advanced_format.xml",
                "arbori=" + tempDir + File.separator + "trivadis_custom_format.arbori"};
        TvdFormat.main(args);
        var expected = getExpectedContent("query.sql");
        var actual = getFormattedContent("query.sql");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void jsonArrayFileTest() throws ScriptException, IOException {
        var configFileContent = """
                [
                    "#TEMP_DIR##FILE_SEP#query.sql"
                ]
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        var configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{tempDir + File.separator + "config.json",
                "xml=" + tempDir + File.separator + "trivadis_advanced_format.xml",
                "arbori=" + tempDir + File.separator + "trivadis_custom_format.arbori"};
        TvdFormat.main(args);
        var expected = getExpectedContent("query.sql");
        var actual = getFormattedContent("query.sql");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void jsonObjectFileTest() throws ScriptException, IOException {
        var configFileContent = """
                {
                    "xml": "#TEMP_DIR##FILE_SEP#trivadis_advanced_format.xml",
                    "arbori": "#TEMP_DIR##FILE_SEP#trivadis_custom_format.arbori",
                    "files": [
                        "#TEMP_DIR##FILE_SEP#query.sql"
                    ]
                }
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        var configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{tempDir + File.separator + "config.json"};
        TvdFormat.main(args);
        var expected = getExpectedContent("query.sql");
        var actual = getFormattedContent("query.sql");
        Assertions.assertEquals(expected, actual);
    }
}
