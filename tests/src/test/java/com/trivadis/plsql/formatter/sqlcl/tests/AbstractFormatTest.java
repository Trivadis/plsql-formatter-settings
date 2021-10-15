package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class AbstractFormatTest extends AbstractSqlclTest {

    public void process_dir(final RunType runType) {
        // console output
        var expected = """
                            
                Formatting file 1 of 3: #TEMP_DIR##FILE_SEP#package_body.pkb... done.
                Formatting file 2 of 3: #TEMP_DIR##FILE_SEP#query.sql... done.
                Formatting file 3 of 3: #TEMP_DIR##FILE_SEP#syntax_error.sql... Syntax Error at line 6, column 12
                            
                            
                   for r in /*(*/ select x.* from x join y on y.a = x.a)
                            ^^^                                         \s
                            
                Expected: expr#,simple_expression,name_wo_function_call,iden... skipped.
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        var actual = run(runType, tempDir.toString(), "mext=");
        Assertions.assertEquals(expected, actual);

        // package_body.pkb
        var expectedPackageBody = """
                create or replace package body the_api.math as
                   function to_int_table(in_integers in varchar2,
                                         in_pattern  in varchar2 default '[0-9]+') return sys.ora_mining_number_nt
                      deterministic
                      accessible by (package the_api.math, package the_api.test_math)
                   is
                      l_result sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                      l_pos    integer                  := 1;
                      l_int    integer;
                   begin
                      <<integer_tokens>>
                      loop
                         l_int           := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                         exit integer_tokens when l_int is null;
                         l_result.extend;
                         l_result(l_pos) := l_int;
                         l_pos           := l_pos + 1;
                      end loop integer_tokens;
                      return l_result;
                   end to_int_table;
                end math;
                /
                """;
        var actualPackageBody = getFormattedContent("package_body.pkb");
        Assertions.assertEquals(expectedPackageBody, actualPackageBody);

        // query.sql
        var expectedQuery = """
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
        var actualQuery = getFormattedContent("query.sql");
        Assertions.assertEquals(expectedQuery, actualQuery);

        // syntax_error.sql
        Assertions.assertEquals(getOriginalContent("syntax_error.sql"), getFormattedContent("syntax_error.sql"));
    }

    public void process_pkb_only(final RunType runType) {
        // run
        var actual = run(runType, tempDir.toString(), "ext=pkb", "mext=");
        Assertions.assertTrue(actual.contains("file 1 of 1"));

        // package_body.pkb
        var expectedPackageBody = """
                create or replace package body the_api.math as
                   function to_int_table(in_integers in varchar2,
                                         in_pattern  in varchar2 default '[0-9]+') return sys.ora_mining_number_nt
                      deterministic
                      accessible by (package the_api.math, package the_api.test_math)
                   is
                      l_result sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                      l_pos    integer                  := 1;
                      l_int    integer;
                   begin
                      <<integer_tokens>>
                      loop
                         l_int           := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                         exit integer_tokens when l_int is null;
                         l_result.extend;
                         l_result(l_pos) := l_int;
                         l_pos           := l_pos + 1;
                      end loop integer_tokens;
                      return l_result;
                   end to_int_table;
                end math;
                /
                """;
        var actualPackageBody = getFormattedContent("package_body.pkb");
        Assertions.assertEquals(expectedPackageBody, actualPackageBody);
    }

    public void process_with_default_arbori(final RunType runType) {
        // run
        var actual = run(runType, tempDir.toString(), "arbori=default");
        Assertions.assertTrue(actual.contains("package_body.pkb"));
        Assertions.assertTrue(actual.contains("query.sql"));

        // package_body.pkb
        var expectedPackageBody = """
                create or replace package body the_api.math as
                                
                   function to_int_table (
                      in_integers in varchar2,
                      in_pattern  in varchar2 default '[0-9]+'
                   ) return sys.ora_mining_number_nt
                      deterministic
                      accessible by ( package the_api.math, package the_api.test_math )
                   is
                                
                      l_result sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                      l_pos    integer := 1;
                      l_int    integer;
                   begin
                      << integer_tokens >> loop
                         l_int           := to_number(regexp_substr(
                                                         in_integers,
                                                         in_pattern,
                                                         1,
                                                         l_pos
                                            ));
                         exit integer_tokens when l_int is null;
                         l_result.extend;
                         l_result(l_pos) := l_int;
                         l_pos           := l_pos + 1;
                      end loop integer_tokens;
                                
                      return l_result;
                   end to_int_table;
                                
                end math;
                /
                """;
        var actualPackageBody = getFormattedContent("package_body.pkb");
        Assertions.assertEquals(expectedPackageBody, actualPackageBody);

        // query.sql
        var expectedQuery = """
                select d.department_name,
                       v.employee_id,
                       v.last_name
                  from departments d
                cross apply (
                   select *
                     from employees e
                    where e.department_id = d.department_id
                ) v
                 where d.department_name in ( 'Marketing',
                                              'Operations',
                                              'Public Relations' )
                 order by d.department_name,
                          v.employee_id;
                """;
        var actualQuery = getFormattedContent("query.sql");
        Assertions.assertEquals(expectedQuery, actualQuery);
    }

    public void process_with_xml(final RunType runType) {
        // run
        var actual = run(runType, tempDir.toString(), "xml=" +
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("advanced_format.xml")).getPath());
        Assertions.assertTrue(actual.contains("package_body.pkb"));
        Assertions.assertTrue(actual.contains("query.sql"));

        // package_body.pkb
        var expectedPackageBody = """
                CREATE OR REPLACE PACKAGE BODY the_api.math AS
                   FUNCTION to_int_table(in_integers IN VARCHAR2
                                       , in_pattern  IN VARCHAR2 DEFAULT '[0-9]+') RETURN sys.ora_mining_number_nt
                      DETERMINISTIC
                      ACCESSIBLE BY (PACKAGE the_api.math, PACKAGE the_api.test_math)
                   IS
                      l_result sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                      l_pos    INTEGER                  := 1;
                      l_int    INTEGER;
                   BEGIN
                      <<integer_tokens>>
                      LOOP
                         l_int           := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                         EXIT integer_tokens WHEN l_int IS NULL;
                         l_result.extend;
                         l_result(l_pos) := l_int;
                         l_pos           := l_pos + 1;
                      END LOOP integer_tokens;
                      RETURN l_result;
                   END to_int_table;
                END math;
                /
                """;
        var actualPackageBody = getFormattedContent("package_body.pkb");
        Assertions.assertEquals(expectedPackageBody, actualPackageBody);

        // query.sql
        var expectedQuery = """
                SELECT d.department_name
                     , v.employee_id
                     , v.last_name
                  FROM departments d
                 CROSS APPLY (
                          SELECT *
                            FROM employees e
                           WHERE e.department_id = d.department_id
                       ) v
                 WHERE d.department_name IN ('Marketing', 'Operations', 'Public Relations')
                 ORDER BY d.department_name, v.employee_id;
                """;
        var actualQuery = getFormattedContent("query.sql");
        Assertions.assertEquals(expectedQuery, actualQuery);
    }

    public void process_with_default_xml_default_arbori(final RunType runType) {
        // run
        var actual = run(runType, tempDir.toString(), "xml=default", "arbori=default");
        Assertions.assertTrue(actual.contains("package_body.pkb"));
        Assertions.assertTrue(actual.contains("query.sql"));

        // package_body.pkb
        var expectedPackageBody = """
                CREATE OR REPLACE PACKAGE BODY the_api.math AS
                                
                    FUNCTION to_int_table (
                        in_integers IN VARCHAR2,
                        in_pattern  IN VARCHAR2 DEFAULT '[0-9]+'
                    ) RETURN sys.ora_mining_number_nt
                        DETERMINISTIC
                        ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
                    IS
                                
                        l_result sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                        l_pos    INTEGER := 1;
                        l_int    INTEGER;
                    BEGIN
                        << integer_tokens >> LOOP
                            l_int := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                            EXIT integer_tokens WHEN l_int IS NULL;
                            l_result.extend;
                            l_result(l_pos) := l_int;
                            l_pos := l_pos + 1;
                        END LOOP integer_tokens;
                                
                        RETURN l_result;
                    END to_int_table;
                                
                END math;
                /
                """;
        var actualPackageBody = getFormattedContent("package_body.pkb");
        Assertions.assertEquals(expectedPackageBody, actualPackageBody);

        // query.sql
        var expectedQuery = """
                SELECT
                    d.department_name,
                    v.employee_id,
                    v.last_name
                FROM
                    departments d
                    CROSS APPLY (
                        SELECT
                            *
                        FROM
                            employees e
                        WHERE
                            e.department_id = d.department_id
                    )           v
                WHERE
                    d.department_name IN ( 'Marketing', 'Operations', 'Public Relations' )
                ORDER BY
                    d.department_name,
                    v.employee_id;
                """;
        var actualQuery = getFormattedContent("query.sql");
        Assertions.assertEquals(expectedQuery, actualQuery);
    }

    public void process_with_embedded_xml_default_arbori(final RunType runType) {
        // run
        var actual = run(runType, tempDir.toString(), "xml=embedded", "arbori=default");
        Assertions.assertTrue(actual.contains("package_body.pkb"));
        Assertions.assertTrue(actual.contains("query.sql"));

        // package_body.pkb
        var expectedPackageBody = """
                create or replace package body the_api.math as
                                
                   function to_int_table (
                      in_integers in varchar2,
                      in_pattern  in varchar2 default '[0-9]+'
                   ) return sys.ora_mining_number_nt
                      deterministic
                      accessible by ( package the_api.math, package the_api.test_math )
                   is
                                
                      l_result sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                      l_pos    integer := 1;
                      l_int    integer;
                   begin
                      << integer_tokens >> loop
                         l_int           := to_number(regexp_substr(
                                                         in_integers,
                                                         in_pattern,
                                                         1,
                                                         l_pos
                                            ));
                         exit integer_tokens when l_int is null;
                         l_result.extend;
                         l_result(l_pos) := l_int;
                         l_pos           := l_pos + 1;
                      end loop integer_tokens;
                                
                      return l_result;
                   end to_int_table;
                                
                end math;
                /
                """;
        var actualPackageBody = getFormattedContent("package_body.pkb");
        Assertions.assertEquals(expectedPackageBody, actualPackageBody);

        // query.sql
        var expectedQuery = """
                select d.department_name,
                       v.employee_id,
                       v.last_name
                  from departments d
                cross apply (
                   select *
                     from employees e
                    where e.department_id = d.department_id
                ) v
                 where d.department_name in ( 'Marketing',
                                              'Operations',
                                              'Public Relations' )
                 order by d.department_name,
                          v.employee_id;
                """;
        var actualQuery = getFormattedContent("query.sql");
        Assertions.assertEquals(expectedQuery, actualQuery);
    }

    public void process_markdown_only(final RunType runType) {
        // run
        var actualConsole = run(runType, tempDir.toString(), "ext=");
        Assertions.assertTrue(actualConsole.contains("Formatting file 1 of 1: " + tempDir.toString() + File.separator + "markdown.md... done."));

        // markdown.md
        var actualMarkdown = getFormattedContent("markdown.md");
        var expectedMarkdown = """
                # Titel
                                
                ## Introduction
                                
                This is a Markdown file with some `code blocks`.\s
                                
                ## Package Body
                                
                Here's the content of package_body.pkb
                                
                ```sql
                create or replace package body the_api.math as
                   function to_int_table(in_integers in varchar2,
                                         in_pattern  in varchar2 default '[0-9]+') return sys.ora_mining_number_nt
                      deterministic
                      accessible by (package the_api.math, package the_api.test_math)
                   is
                      l_result sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                      l_pos    integer                  := 1;
                      l_int    integer;
                   begin
                      <<integer_tokens>>
                      loop
                         l_int           := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                         exit integer_tokens when l_int is null;
                         l_result.extend;
                         l_result(l_pos) := l_int;
                         l_pos           := l_pos + 1;
                      end loop integer_tokens;
                      return l_result;
                   end to_int_table;
                end math;
                /
                ```
                                
                ## Syntax Error
                                
                Here's the content of syntax_error.sql
                                
                ```  sql
                declare
                    l_var1  integer;
                    l_var2  varchar2(20);
                begin
                    for r in /*(*/ select x.* from x join y on y.a = x.a)
                    loop
                      p(r.a, r.b, r.c);
                    end loop;
                end;
                /
                ```
                                
                ## Query (to be ignored)
                                
                Here's the content of query.sql, but the code block must not be formatted:
                                
                ```
                Select d.department_name,v.  employee_id\s
                ,v\s
                . last_name frOm departments d CROSS APPLY(select*from employees e
                  wHERE e.department_id=d.department_id) v WHeRE\s
                d.department_name in ('Marketing'
                ,'Operations',
                'Public Relations') Order By d.
                department_name,v.employee_id;
                ```
                                
                ## Query (to be formatted)
                                
                Here's the content of query.sql:
                                
                ``` sql
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
                ```
                                
                ## JavaScript code
                                
                Here's another code which must not be formatted
                                
                ``` js
                var foo = function (bar) {
                  return bar++;
                };
                ```
                """;
        Assertions.assertEquals(expectedMarkdown, actualMarkdown);
    }

    public void process_config_file_array(final RunType runType) throws IOException {
        var expected = """
                                
                Formatting file 1 of 2: #TEMP_DIR##FILE_SEP#query.sql... done.
                Formatting file 2 of 2: #TEMP_DIR##FILE_SEP#markdown.md... done.
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        var configFileContent = """
                [
                    "#TEMP_DIR##FILE_SEP#query.sql",
                    "#TEMP_DIR##FILE_SEP#markdown.md",
                    "#TEMP_DIR##FILE_SEP#dont_format.txt"
                ]
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        final Path configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString());
        Assertions.assertEquals(expected, actual);
    }

    public void process_config_file_object(final RunType runType) throws IOException {
        var expected = """
                                
                Formatting file 1 of 2: #TEMP_DIR##FILE_SEP#query.sql... done.
                Formatting file 2 of 2: #TEMP_DIR##FILE_SEP#markdown.md2... done.
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        Files.move(Paths.get(tempDir + File.separator + "markdown.md"), Paths.get(tempDir.toString() + File.separator + "markdown.md2"));
        var configFileContent = """
                {
                    "files": [
                        "#TEMP_DIR##FILE_SEP#query.sql",
                        "#TEMP_DIR##FILE_SEP#markdown.md2",
                        "#TEMP_DIR##FILE_SEP#dont_format.txt"
                    ],
                    "ext": ["sql"],
                    "mext": ["md2", "md3", "md4"],
                    "xml": "default",
                    "arbori": "default"
                }
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        final Path configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString());
        Assertions.assertEquals(expected, actual);
        var original = getOriginalContent("markdown.md");
        var processed = getFormattedContent("markdown.md2");
        Assertions.assertNotEquals(original, processed);
        var formattedPart = """
                ``` sql
                SELECT
                    d.department_name,
                    v.employee_id,
                    v.last_name
                FROM
                """;
        Assertions.assertTrue(processed.contains(formattedPart));
    }

    public void process_config_file_object_and_param(final RunType runType) throws IOException {
        var expected = """
                                
                Formatting file 1 of 1: #TEMP_DIR##FILE_SEP#query.sql... done.
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        var configFileContent = """
                {
                    "files": [
                        "#TEMP_DIR##FILE_SEP#query.sql",
                        "#TEMP_DIR##FILE_SEP#markdown.md"
                    ]
                }
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        final Path configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString(), "mext=md2");
        Assertions.assertEquals(expected, actual);
    }

    public void process_config_dir_array(final RunType runType) throws IOException {
        var expected = """
                                
                Formatting file 1 of 4: #TEMP_DIR##FILE_SEP#markdown.md... done.
                Formatting file 2 of 4: #TEMP_DIR##FILE_SEP#package_body.pkb... done.
                Formatting file 3 of 4: #TEMP_DIR##FILE_SEP#query.sql... done.
                Formatting file 4 of 4: #TEMP_DIR##FILE_SEP#syntax_error.sql... Syntax Error at line 6, column 12


                   for r in /*(*/ select x.* from x join y on y.a = x.a)
                            ^^^                                         \s
                                                                                     
                Expected: expr#,simple_expression,name_wo_function_call,iden... skipped.
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        var configFileContent = """
                [
                    "#TEMP_DIR#"
                ]
                """.replace("#TEMP_DIR#", tempDir.toString()).replace("#FILE_SEP#", File.separator);
        final Path configFile = Paths.get(tempDir + File.separator + "config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString());
        Assertions.assertEquals(expected, actual);
    }
}
