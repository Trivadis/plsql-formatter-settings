package com.trivadis.plsql.formatter.sqlcl.tests

import java.io.File
import oracle.dbtools.raptor.newscriptrunner.CommandRegistry
import oracle.dbtools.raptor.newscriptrunner.SQLCommand
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TvdFormatTest extends AbstractSqlclTest {
    
    @Before
    def void register() {
        runScript("--register")
        byteArrayOutputStream.reset();
    }
    
    @Test
    def void duplicate_registration() {
        reset();
        val originalListeners = CommandRegistry.getListeners(null, ctx).get(
            SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        val expected = '''
            tvdformat registered as SQLcl command.
        '''
                
        // first registrations
        val actual1 = runScript("--register")
        Assert.assertEquals(expected, actual1)
        val listeners1 = CommandRegistry.getListeners(null, ctx).get(
            SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        Assert.assertEquals(originalListeners.size() + 1, listeners1.size())
        
        // second registration
        byteArrayOutputStream.reset
        val actual2 = runScript("-r")
        Assert.assertEquals(expected, actual2)
        val listeners2 = CommandRegistry.getListeners(null, ctx).get(
            SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        Assert.assertEquals(originalListeners.size() + 1, listeners2.size())
    }

 @Test
    def void process_dir() {
        // console output
        val expected = '''

            Formatting file 1 of 3: «tempDir.toString()»«File.separator»package_body.pkb... done.
            Formatting file 2 of 3: «tempDir.toString()»«File.separator»query.sql... done.
            Formatting file 3 of 3: «tempDir.toString()»«File.separator»syntax_error.sql... Syntax Error at line 4, column 12
            
            
               for r in /*(*/ select x.* from x join y on y.a = x.a)
                        ^^^                                          
            
            Expected: name_wo_function_call,identifier,term,factor,name,... skipped.
        '''
        val actual = runCommand("tvdformat " + tempDir.toString())
        Assert.assertEquals(expected, actual)
        
        // package_body.pkb
        val expectedPackageBody = '''
            CREATE OR REPLACE PACKAGE BODY the_api.math AS
               FUNCTION to_int_table (
                  in_integers  IN  VARCHAR2,
                  in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
               ) RETURN sys.ora_mining_number_nt
                  DETERMINISTIC
                  ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
               IS
                  l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                  l_pos     INTEGER := 1;
                  l_int     INTEGER;
               BEGIN
                  <<integer_tokens>>
                  LOOP
                     l_int               := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                     EXIT integer_tokens WHEN l_int IS NULL;
                     l_result.extend;
                     l_result(l_pos)     := l_int;
                     l_pos               := l_pos + 1;
                  END LOOP integer_tokens;
                  RETURN l_result;
               END to_int_table;
            END math;
            /
        '''.toString.trim
        val actualPackageBody = getFormattedContent("package_body.pkb")
        Assert.assertEquals(expectedPackageBody, actualPackageBody)

        // query.sql
        val expectedQuery = '''
            SELECT d.department_name,
                   v.employee_id,
                   v.last_name
              FROM departments d CROSS APPLY (
                      SELECT *
                        FROM employees e
                       WHERE e.department_id = d.department_id
                   ) v
             WHERE d.department_name IN (
                      'Marketing',
                      'Operations',
                      'Public Relations'
                   )
             ORDER BY d.department_name,
                      v.employee_id;
        '''.toString.trim
        val actualQuery = getFormattedContent("query.sql")
        Assert.assertEquals(expectedQuery, actualQuery)

        // syntax_error.sql
        Assert.assertEquals(getOriginalContent("syntax_error.sql"), getFormattedContent("syntax_error.sql"))

    }

    @Test
    def void process_pkb_only() {
        // run
        val actual = runCommand("tvdformat " + tempDir.toString() + " ext=pkb")
        Assert.assertTrue(actual.contains("file 1 of 1"))
        
        // package_body.pkb
        val expectedPackageBody = '''
            CREATE OR REPLACE PACKAGE BODY the_api.math AS
               FUNCTION to_int_table (
                  in_integers  IN  VARCHAR2,
                  in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
               ) RETURN sys.ora_mining_number_nt
                  DETERMINISTIC
                  ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
               IS
                  l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                  l_pos     INTEGER := 1;
                  l_int     INTEGER;
               BEGIN
                  <<integer_tokens>>
                  LOOP
                     l_int               := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                     EXIT integer_tokens WHEN l_int IS NULL;
                     l_result.extend;
                     l_result(l_pos)     := l_int;
                     l_pos               := l_pos + 1;
                  END LOOP integer_tokens;
                  RETURN l_result;
               END to_int_table;
            END math;
            /
        '''.toString.trim
        val actualPackageBody = getFormattedContent("package_body.pkb")
        Assert.assertEquals(expectedPackageBody, actualPackageBody)
    }

    @Test
    def void process_with_original_arbori() {
        // run
        val actual = runCommand("tvdformat " + tempDir.toString() + " arbori=" +
            Thread.currentThread().getContextClassLoader().getResource("original/20.2.0/custom_format.arbori").path)
        Assert.assertTrue(actual.contains("package_body.pkb"))
        Assert.assertTrue(actual.contains("query.sql"))

        // package_body.pkb
        val expectedPackageBody = '''
            CREATE OR REPLACE PACKAGE BODY the_api.math AS
               FUNCTION to_int_table (
                  in_integers  IN  VARCHAR2,
                  in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
               ) RETURN sys.ora_mining_number_nt
                  DETERMINISTIC
                  ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
               IS
                  l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                  l_pos     INTEGER := 1;
                  l_int     INTEGER;
               BEGIN
                  << integer_tokens >> LOOP
                     l_int               := to_number(regexp_substr(
                                                     in_integers,
                                                     in_pattern,
                                                     1,
                                                     l_pos
                                        ));
                     EXIT integer_tokens WHEN l_int IS NULL;
                     l_result.extend;
                     l_result(l_pos)     := l_int;
                     l_pos               := l_pos + 1;
                  END LOOP integer_tokens;
                  RETURN l_result;
               END to_int_table;
            END math;
            /
        '''.toString.trim
        val actualPackageBody = getFormattedContent("package_body.pkb")
        Assert.assertEquals(expectedPackageBody, actualPackageBody)
 
        // query.sql
        val expectedQuery = '''
            SELECT d.department_name,
                   v.employee_id,
                   v.last_name
              FROM departments d CROSS APPLY (
               SELECT *
                 FROM employees e
                WHERE e.department_id = d.department_id
            ) v
             WHERE d.department_name IN ( 'Marketing',
                                          'Operations',
                                          'Public Relations' )
             ORDER BY d.department_name,
                      v.employee_id;
        '''.toString.trim
        val actualQuery = getFormattedContent("query.sql")
        Assert.assertEquals(expectedQuery, actualQuery)
    }

    @Test
    def void process_with_default_arbori() {
        // run
        val actual = runCommand("tvdformat " + tempDir.toString() + " arbori=default")
        Assert.assertTrue(actual.contains("package_body.pkb"))
        Assert.assertTrue(actual.contains("query.sql"))
        

        // package_body.pkb
        val expectedPackageBody = '''
            CREATE OR REPLACE PACKAGE BODY the_api.math AS
               FUNCTION to_int_table (
                  in_integers  IN  VARCHAR2,
                  in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
               ) RETURN sys.ora_mining_number_nt
                  DETERMINISTIC
                  ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
               IS
                  l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                  l_pos     INTEGER := 1;
                  l_int     INTEGER;
               BEGIN
                  << integer_tokens >> LOOP
                     l_int               := to_number(regexp_substr(
                                                     in_integers,
                                                     in_pattern,
                                                     1,
                                                     l_pos
                                        ));
                     EXIT integer_tokens WHEN l_int IS NULL;
                     l_result.extend;
                     l_result(l_pos)     := l_int;
                     l_pos               := l_pos + 1;
                  END LOOP integer_tokens;
                  RETURN l_result;
               END to_int_table;
            END math;
            /
        '''.toString.trim
        val actualPackageBody = getFormattedContent("package_body.pkb")
        Assert.assertEquals(expectedPackageBody, actualPackageBody)
 
        // query.sql
        val expectedQuery = '''
            SELECT d.department_name,
                   v.employee_id,
                   v.last_name
              FROM departments d CROSS APPLY (
               SELECT *
                 FROM employees e
                WHERE e.department_id = d.department_id
            ) v
             WHERE d.department_name IN ( 'Marketing',
                                          'Operations',
                                          'Public Relations' )
             ORDER BY d.department_name,
                      v.employee_id;
        '''.toString.trim
        val actualQuery = getFormattedContent("query.sql")
        Assert.assertEquals(expectedQuery, actualQuery)
    }

    @Test
    def void process_with_xml() {
        // run
        val actual = runCommand("tvdformat " + tempDir.toString() + " xml=" +
            Thread.currentThread().getContextClassLoader().getResource("advanced_format.xml").path)
        Assert.assertTrue(actual.contains("package_body.pkb"))
        Assert.assertTrue(actual.contains("query.sql"))

        // package_body.pkb
        val expectedPackageBody = '''
            CREATE OR REPLACE PACKAGE BODY the_api.math AS
               FUNCTION to_int_table (
                  in_integers  IN  VARCHAR2
                , in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
               ) RETURN sys.ora_mining_number_nt
                  DETERMINISTIC
                  ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
               IS
                  l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                  l_pos     INTEGER := 1;
                  l_int     INTEGER;
               BEGIN
                  <<integer_tokens>>
                  LOOP
                     l_int               := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                     EXIT integer_tokens WHEN l_int IS NULL;
                     l_result.extend;
                     l_result(l_pos)     := l_int;
                     l_pos               := l_pos + 1;
                  END LOOP integer_tokens;
                  RETURN l_result;
               END to_int_table;
            END math;
            /
        '''.toString.trim
        val actualPackageBody = getFormattedContent("package_body.pkb")
        Assert.assertEquals(expectedPackageBody, actualPackageBody)
 
        // query.sql
        val expectedQuery = '''
            SELECT d.department_name
                 , v.employee_id
                 , v.last_name
              FROM departments d CROSS APPLY (
                      SELECT *
                        FROM employees e
                       WHERE e.department_id = d.department_id
                   ) v
             WHERE d.department_name IN (
                      'Marketing'
                    , 'Operations'
                    , 'Public Relations'
                   )
             ORDER BY d.department_name
                    , v.employee_id;
        '''.toString.trim
        val actualQuery = getFormattedContent("query.sql")
        Assert.assertEquals(expectedQuery, actualQuery)
    }

    @Test
    def void process_with_default_xml_default_arbori() {
        // run
        val actual = runCommand("tvdformat " + tempDir.toString() + " xml=default" + " arbori=default")
        Assert.assertTrue(actual.contains("package_body.pkb"))
        Assert.assertTrue(actual.contains("query.sql"))

        // package_body.pkb
        val expectedPackageBody = '''
            CREATE OR REPLACE PACKAGE BODY the_api.math AS
            
                FUNCTION to_int_table (
                    in_integers  IN  VARCHAR2,
                    in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
                ) RETURN sys.ora_mining_number_nt
                    DETERMINISTIC
                    ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
                IS
            
                    l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
                    l_pos     INTEGER := 1;
                    l_int     INTEGER;
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
        '''.toString.trim
        val actualPackageBody = getFormattedContent("package_body.pkb")
        Assert.assertEquals(expectedPackageBody, actualPackageBody)
 
        // query.sql
        val expectedQuery = '''
            SELECT
                d.department_name,
                v.employee_id,
                v.last_name
            FROM
                departments  d CROSS APPLY (
                    SELECT
                        *
                    FROM
                        employees e
                    WHERE
                        e.department_id = d.department_id
                )            v
            WHERE
                d.department_name IN ( 'Marketing', 'Operations', 'Public Relations' )
            ORDER BY
                d.department_name,
                v.employee_id;
        '''.toString.trim
        val actualQuery = getFormattedContent("query.sql")
        Assert.assertEquals(expectedQuery, actualQuery)
    }

}
