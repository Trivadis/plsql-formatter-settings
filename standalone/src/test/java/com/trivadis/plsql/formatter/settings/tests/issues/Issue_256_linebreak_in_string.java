package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("Bug in SQLDev 23.1, SQLcl 23.3")
public class Issue_256_linebreak_in_string extends ConfiguredTestFormatter {
    @Test
    public void escaped_entity_in_string() throws IOException {
        var input = """
                begin
                   dbms_output.put_line('&lt;/');
                end;
                /
                """;
        var expected = """
                begin
                   dbms_output.put_line('&lt;/');
                end;
                /
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void semi_slash_in_string() throws IOException {
        var input = """
                begin
                   dbms_output.put_line(';/');
                end;
                /
                """;
        var expected = """
                begin
                   dbms_output.put_line(';/');
                end;
                /
                """;
        var actual = getFormatter().format(input);
        assertEquals(expected, actual);
    }

}
