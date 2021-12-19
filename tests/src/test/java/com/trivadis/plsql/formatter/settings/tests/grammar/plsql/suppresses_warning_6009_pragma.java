package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class suppresses_warning_6009_pragma extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                CREATE PROCEDURE p1
                AUTHID DEFINER
                IS
                PRAGMA
                SUPPRESSES_WARNING_6009
                (
                p1
                )
                ;
                BEGIN
                    RAISE_APPLICATION_ERROR(-20000, 'Unexpected error raised');
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create procedure p1
                   authid definer
                is
                   pragma suppresses_warning_6009 (p1);
                begin
                   raise_application_error(-20000, 'Unexpected error raised');
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
