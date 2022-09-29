package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class AbstractFormatTest extends AbstractSqlclTest {

    public void process_dir(final RunType runType) {
        // console output
        var expected = """
                            
                Formatting file 1 of 3: #TEMP_DIR#/package_body.pkb... done.
                Formatting file 2 of 3: #TEMP_DIR#/query.sql... done.
                Formatting file 3 of 3: #TEMP_DIR#/syntax_error.sql... Syntax Error at line 6, column 12
                            
                            
                   for r in /*(*/ select x.* from x join y on y.a = x.a)
                            ^^^
                            
                Expected: constraint,':',"aggr_name",'COUNT','-','(','JSON_T... skipped.
                """.replace("#TEMP_DIR#", getTempDir());
        var actual = run(runType, getTempDir(), "mext=");
        assertEquals(expected, actual);

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
        var actual = run(runType, getTempDir(), "ext=pkb", "mext=");
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
        var actual = run(runType, getTempDir(), "arbori=default");
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
                         l_int           := to_number ( regexp_substr(
                            in_integers,
                            in_pattern,
                            1,
                            l_pos
                         ) );
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
        var actual = run(runType, getTempDir(), "xml=" +
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
                         l_int           := TO_NUMBER(regexp_substr(in_integers, in_pattern, 1, l_pos));
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
        var actual = run(runType, getTempDir(), "xml=default", "arbori=default");
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
                            l_int := TO_NUMBER ( regexp_substr(in_integers, in_pattern, 1, l_pos) );
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
        var actual = run(runType, getTempDir(), "xml=embedded", "arbori=default");
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
                         l_int           := to_number ( regexp_substr(
                            in_integers,
                            in_pattern,
                            1,
                            l_pos
                         ) );
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
        var actualConsole = run(runType, getTempDir(), "ext=none", "serr=ext");
        Assertions.assertTrue(actualConsole.contains("Formatting file 1 of 1: " + getTempDir() + "/markdown.md... #1... done... #2... skipped... #3... done... done."));

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
                                
                Formatting file 1 of 2: #TEMP_DIR#/query.sql... done.
                Formatting file 2 of 2: #TEMP_DIR#/markdown.md... #1... done... #2... skipped... #3... done... done.
                """.replace("#TEMP_DIR#", getTempDir());
        var configFileContent = """
                [
                    "#TEMP_DIR#/query.sql",
                    "#TEMP_DIR#/markdown.md",
                    "#TEMP_DIR#/dont_format.txt"
                ]
                """.replace("#TEMP_DIR#", getTempDir());
        final Path configFile = Paths.get(getTempDir() + "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString(), "serr=ext");
        Assertions.assertEquals(expected, actual);
    }

    public void process_config_file_object(final RunType runType) throws IOException {
        var expected = """
                                
                Formatting file 1 of 2: #TEMP_DIR#/query.sql... done.
                Formatting file 2 of 2: #TEMP_DIR#/markdown.md2... #1... done... #2... skipped... #3... done... done.
                """.replace("#TEMP_DIR#", getTempDir());
        Files.move(Paths.get(getTempDir() + "/markdown.md"), Paths.get(getTempDir() + "/markdown.md2"));
        var configFileContent = """
                {
                    "files": [
                        "#TEMP_DIR#/query.sql",
                        "#TEMP_DIR#/markdown.md2",
                        "#TEMP_DIR#/dont_format.txt"
                    ],
                    "ext": ["sql"],
                    "mext": ["md2", "md3", "md4"],
                    "xml": "default",
                    "arbori": "default"
                }
                """.replace("#TEMP_DIR#", getTempDir());
        final Path configFile = Paths.get(getTempDir() + "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString(), "serr=ext");
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
                                
                Formatting file 1 of 1: #TEMP_DIR#/query.sql... done.
                """.replace("#TEMP_DIR#", getTempDir());
        var configFileContent = """
                {
                    "files": [
                        "#TEMP_DIR#/query.sql",
                        "#TEMP_DIR#/markdown.md"
                    ]
                }
                """.replace("#TEMP_DIR#", getTempDir());
        final Path configFile = Paths.get(getTempDir() + "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString(), "mext=md2, serr=ext");
        Assertions.assertEquals(expected, actual);
    }

    public void process_config_dir_array(final RunType runType) throws IOException {
        var expected = """
                                
                Formatting file 1 of 4: #TEMP_DIR#/markdown.md... #1... done... #2... skipped... #3... done... done.
                Formatting file 2 of 4: #TEMP_DIR#/package_body.pkb... done.
                Formatting file 3 of 4: #TEMP_DIR#/query.sql... done.
                Formatting file 4 of 4: #TEMP_DIR#/syntax_error.sql... Syntax Error at line 6, column 12


                   for r in /*(*/ select x.* from x join y on y.a = x.a)
                            ^^^
                                                                                     
                Expected: constraint,':',"aggr_name",'COUNT','-','(','JSON_T... skipped.
                """.replace("#TEMP_DIR#", getTempDir());
        var configFileContent = """
                [
                    "#TEMP_DIR#"
                ]
                """.replace("#TEMP_DIR#", getTempDir());
        final Path configFile = Paths.get(getTempDir() + "/config.json");
        Files.write(configFile, configFileContent.getBytes());
        var actual = run(runType, configFile.toString(), "serr=ext");
        assertEquals(expected, actual);
    }

    public void process_sql_txt_default(final RunType runType) {
        var expected = """
                            
                """;
        final Path file = Paths.get(getTempDir() + "/sql.txt");
        var actual = run(runType, file.toString());
        Assertions.assertEquals(expected, actual);

        var expectedContent = """
                select
                *
                from
                dual
                ;
                """;
        var actualContent = getFormattedContent("sql.txt");
        Assertions.assertEquals(expectedContent, actualContent);
    }

    public void process_sql_txt_force(final RunType runType) {
        var expected = """
                
                Formatting file 1 of 1: #TEMP_DIR#/sql.txt... done.
                """.replace("#TEMP_DIR#", getTempDir());
        final Path file = Paths.get(getTempDir() + "/sql.txt");
        var actual = run(runType, file.toString(), "ext=txt");
        Assertions.assertEquals(expected, actual);

        var expectedContent = """
                select *
                  from dual;
                """;
        var actualContent = getFormattedContent("sql.txt");
        Assertions.assertEquals(expectedContent, actualContent);
    }

    public void process_dir_all_errors(final RunType runType) {
        // console output
        var expected = """
                                
                Formatting file 1 of 4: #TEMP_DIR#/markdown.md... #1... done... #2... Syntax Error at line 6, column 13
                                
                                
                    for r in /*(*/ select x.* from x join y on y.a = x.a)
                             ^^^
                                
                Expected: constraint,':',"aggr_name",'COUNT','-','(','JSON_T... skipped... #3... done... done.
                Formatting file 2 of 4: #TEMP_DIR#/package_body.pkb... done.
                Formatting file 3 of 4: #TEMP_DIR#/query.sql... done.
                Formatting file 4 of 4: #TEMP_DIR#/syntax_error.sql... Syntax Error at line 6, column 12
                                
                                
                   for r in /*(*/ select x.* from x join y on y.a = x.a)
                            ^^^
                                
                Expected: constraint,':',"aggr_name",'COUNT','-','(','JSON_T... skipped.
                """.replace("#TEMP_DIR#", getTempDir());
        var actual = run(runType, getTempDir());
        assertEquals(expected, actual);
    }

    public void process_dir_mext_errors(final RunType runType) {
        // console output
        var expected = """
                                
                Formatting file 1 of 4: #TEMP_DIR#/markdown.md... #1... done... #2... Syntax Error at line 6, column 13
                                
                                
                    for r in /*(*/ select x.* from x join y on y.a = x.a)
                             ^^^
                                
                Expected: constraint,':',"aggr_name",'COUNT','-','(','JSON_T... skipped... #3... done... done.
                Formatting file 2 of 4: #TEMP_DIR#/package_body.pkb... done.
                Formatting file 3 of 4: #TEMP_DIR#/query.sql... done.
                Formatting file 4 of 4: #TEMP_DIR#/syntax_error.sql... skipped.
                """.replace("#TEMP_DIR#", getTempDir());
        var actual = run(runType, getTempDir(), "serr=mext");
        assertEquals(expected, actual);
    }

    public void process_dir_ext_errors(final RunType runType) {
        // console output
        var expected = """
                                
                Formatting file 1 of 4: #TEMP_DIR#/markdown.md... #1... done... #2... skipped... #3... done... done.
                Formatting file 2 of 4: #TEMP_DIR#/package_body.pkb... done.
                Formatting file 3 of 4: #TEMP_DIR#/query.sql... done.
                Formatting file 4 of 4: #TEMP_DIR#/syntax_error.sql... Syntax Error at line 6, column 12
                                
                                
                   for r in /*(*/ select x.* from x join y on y.a = x.a)
                            ^^^
                                
                Expected: constraint,':',"aggr_name",'COUNT','-','(','JSON_T... skipped.
                """.replace("#TEMP_DIR#", getTempDir());
        var actual = run(runType, getTempDir(), "serr=ext");
        assertEquals(expected, actual);
    }

    public void process_dir_no_errors(final RunType runType) {
        // console output
        var expected = """
                                
                Formatting file 1 of 4: #TEMP_DIR#/markdown.md... #1... done... #2... skipped... #3... done... done.
                Formatting file 2 of 4: #TEMP_DIR#/package_body.pkb... done.
                Formatting file 3 of 4: #TEMP_DIR#/query.sql... done.
                Formatting file 4 of 4: #TEMP_DIR#/syntax_error.sql... skipped.
                """.replace("#TEMP_DIR#", getTempDir());
        var actual = run(runType, getTempDir(), "serr=none");
        Assertions.assertEquals(expected, actual);
    }
}
