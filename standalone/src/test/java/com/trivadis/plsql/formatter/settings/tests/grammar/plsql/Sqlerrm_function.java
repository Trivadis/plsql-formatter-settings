package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Sqlerrm_function extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
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
