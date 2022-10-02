package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_149_spool extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup_non_trivadis_default_settings() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void spool_and_plsql_block() throws IOException {
        // in SQLDev 21.2.1 DECLARE is part of the SPOOL command
        // As a result there is no way to format that correctly
        // Fixed in SQLDev 21.4.0
        var input = """
                SET SERVEROUTPUT ON
                SPOOL install_options.tmp
                DECLARE
                   PROCEDURE print (in_line IN VARCHAR2) IS
                   BEGIN
                      dbms_output.put_line(in_line);
                   END print;
                BEGIN
                   PRINT('HELLO WORLD');
                END;
                /
                """;
        var expected = """
                set serveroutput on
                spool install_options.tmp
                declare
                   procedure print(in_line in varchar2) is
                   begin
                      dbms_output.put_line(in_line);
                   end print;
                begin
                   print('HELLO WORLD');
                end;
                /
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void spool_and_labelled_plsql_block() throws IOException {
        // this is a workaround
        var input = """
                SET SERVEROUTPUT ON
                SPOOL install_options.tmp
                <<hello_world>>
                DECLARE
                   PROCEDURE print (in_line IN VARCHAR2) IS
                   BEGIN
                      dbms_output.put_line(in_line);
                   END print;
                BEGIN
                   PRINT('HELLO WORLD');
                END hello_world;
                /
                """;
        var expected = """
                set serveroutput on
                spool install_options.tmp
                <<hello_world>>
                declare
                   procedure print(in_line in varchar2) is
                   begin
                      dbms_output.put_line(in_line);
                   end print;
                begin
                   print('HELLO WORLD');
                end hello_world;
                /
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }

}
