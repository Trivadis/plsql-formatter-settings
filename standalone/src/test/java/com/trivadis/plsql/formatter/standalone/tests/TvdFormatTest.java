package com.trivadis.plsql.formatter.standalone.tests;

import com.trivadis.plsql.formatter.TvdFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @Test
    public void jsonArrayDirTest() throws ScriptException, IOException {
        var configFileContent = """
                [
                    "#TEMP_DIR#"
                ]
                """.replace("#TEMP_DIR#", getTempDir());
        var configFile = Paths.get(getTempDir() +  "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{getTempDir() + "/config.json", "xml=" + getXML(), "arbori=" + getArbori()};
        TvdFormat.main(args);
        var actual = getFormattedContent("query.sql").replace("\r", "");
        Assertions.assertEquals(EXPECTED_QUERY_SQL, actual);
        Assertions.assertTrue(getConsoleOutput().contains("1 of 4"));
    }

    @Test
    public void jsonArrayFileTest() throws ScriptException, IOException {
        var configFileContent = """
                [
                    "#TEMP_DIR#/query.sql"
                ]
                """.replace("#TEMP_DIR#", getTempDir());
        var configFile = Paths.get(getTempDir() + "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{getTempDir() + "/config.json", "xml=" + getXML(), "arbori=" + getArbori()};
        TvdFormat.main(args);
        var actual = getFormattedContent("query.sql").replace("\r", "");
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
                        "#TEMP_DIR#/query.sql",
                        "#TEMP_DIR#/package_body.pkb"
                    ]
                }
                """.replace("#XML#", getXML())
                .replace("#ARBORI#", getArbori())
                .replace("#TEMP_DIR#", getTempDir())
                .replace("\\", "/");
        var configFile = Paths.get(getTempDir() + "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var args = new String[]{getTempDir() + "/config.json"};
        TvdFormat.main(args);
        var actual = getFormattedContent("query.sql").replace("\r", "");
        Assertions.assertEquals(EXPECTED_QUERY_SQL, actual);
        Assertions.assertTrue(getConsoleOutput().contains("1 of 2"));
    }
}
