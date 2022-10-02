package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TvdFormatIssue232Test extends AbstractSqlclTest {

    @BeforeEach
    public void registerCommandBeforeTest() throws IOException {
        runScript("--register");
        byteArrayOutputStream.reset();
        var markdownContent = """
                # Titel
                                
                ## Introduction
                                
                This is a Markdown file with some `code blocks`.
                                
                ## Indented code block
                                
                With version sqlcl-22.2.1 and older the block is extended
                to the end of the one in the next chapter.
                                
                - Bullet point one (don't format)
                  ``` sql
                  select
                  *
                  from
                  dual;
                  ```
                - Bullet point two (don't format)
                  ``` sql
                  select
                  *
                  from
                  dual;
                  ```
                                
                ## sql code block with attributes
                                
                A code block to be formatted. Is not formatted in sqlcl-22.2.1.
                                
                ```sql title="My SQL block" linenum=10
                select
                *
                from
                dual;
                ```
                """;
        final Path markdownFile = Paths.get(getTempDir() + "/issue-232.md");
        Files.write(markdownFile, markdownContent.getBytes());
    }

    @Test
    public void format_markdown() {
        var actual = runCommand( "tvdformat " + getTempDir() + "/issue-232.md");
        Assertions.assertTrue(actual.contains("issue-232.md"));
        var actualContent = getFormattedContent("issue-232.md");
        var expectedContent = """
                # Titel
                                
                ## Introduction
                                
                This is a Markdown file with some `code blocks`.
                                
                ## Indented code block
                                
                With version sqlcl-22.2.1 and older the block is extended
                to the end of the one in the next chapter.
                                
                - Bullet point one (don't format)
                  ``` sql
                  select
                  *
                  from
                  dual;
                  ```
                - Bullet point two (don't format)
                  ``` sql
                  select
                  *
                  from
                  dual;
                  ```
                                
                ## sql code block with attributes
                                
                A code block to be formatted. Is not formatted in sqlcl-22.2.1.
                                
                ```sql title="My SQL block" linenum=10
                select *
                  from dual;
                ```
                """;
        Assertions.assertEquals(expectedContent, actualContent);
    }
}
