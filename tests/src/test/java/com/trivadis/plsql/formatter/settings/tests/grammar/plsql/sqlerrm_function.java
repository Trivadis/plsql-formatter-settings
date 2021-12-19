package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class sqlerrm_function extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                BEGIN
                  DBMS_OUTPUT.PUT_LINE('SQLERRM(-6511): ' || TO_CHAR(
                  SQLERRM
                  (
                  -6511
                  )
                  ));
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   dbms_output.put_line('SQLERRM(-6511): ' || to_char(sqlerrm(-6511)));
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
