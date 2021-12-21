package com.trivadis.plsql.formatter.standalone.tests;

import com.trivadis.plsql.formatter.TvdFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TvdFormatTest extends AbstractTvdFormatTest {

    private final String EXPECTED_QUERY_SQL = """
            select d.department_name,
                   v.employee_id,
                   v.last_name
              from departments d
             cross apply (
                      select *
                        from employees e
                       where e.department_id = d.department_id
                   ) v
             where d.department_name in ('Marketing', 'Operations', 'Public Relations')
             order by d.department_name, v.employee_id;
            """;

    private final String XML = Objects.requireNonNull(
            Thread.currentThread().getContextClassLoader().getResource("trivadis_advanced_format.xml")).getPath();

    private final String ARBORI = Objects.requireNonNull(
            Thread.currentThread().getContextClassLoader().getResource("trivadis_custom_format.arbori")).getPath();


    @Test
    public void jsonArrayDirTest() throws ScriptException, IOException {
        var configFileContent = """
                [
                    "#TEMP_DIR#"
                ]
                """.replace("#TEMP_DIR#", tempDir.toString());
        var configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{tempDir + File.separator + "config.json", "xml=" + XML, "arbori=" + ARBORI};
        TvdFormat.main(args);
        var actual = getFormattedContent("query.sql");
        Assertions.assertEquals(EXPECTED_QUERY_SQL, actual);
        Assertions.assertTrue(getConsoleOutput().contains("1 of 4"));
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
        var args = new String[]{tempDir + File.separator + "config.json", "xml=" + XML, "arbori=" + ARBORI};
        TvdFormat.main(args);
        var actual = getFormattedContent("query.sql");
        Assertions.assertEquals(EXPECTED_QUERY_SQL, actual);
        Assertions.assertTrue(getConsoleOutput().contains("1 of 1"));
    }

    @Test
    public void jsonObjectFileTest() throws ScriptException, IOException {
        var configFileContent = """
                {
                    "xml": "#XML#",
                    "arbori": "#ARBORI#",
                    "files": [
                        "#TEMP_DIR##FILE_SEP#query.sql",
                        "#TEMP_DIR##FILE_SEP#package_body.pkb"
                    ]
                }
                """.replace("#XML#", XML)
                .replace("#ARBORI#", ARBORI)
                .replace("#TEMP_DIR#", tempDir.toString())
                .replace("#FILE_SEP#", File.separator);
        var configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{tempDir + File.separator + "config.json"};
        TvdFormat.main(args);
        var actual = getFormattedContent("query.sql");
        Assertions.assertEquals(EXPECTED_QUERY_SQL, actual);
        Assertions.assertTrue(getConsoleOutput().contains("1 of 2"));
    }
}
