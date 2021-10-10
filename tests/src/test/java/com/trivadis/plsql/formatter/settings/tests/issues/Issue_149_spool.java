package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_149_spool extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup_non_trivadis_default_settings() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    @Disabled("SQLDev 21.2.1 bug")
    public void spool_and_plsql_block() throws IOException {
        // in SQLDev 21.2.1 DECLARE is part of the SPOOL command
        // As a result there is no way to format that correctly
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
        var actual = formatter.format(input);
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
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

}
