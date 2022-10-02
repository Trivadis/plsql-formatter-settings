package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Compile_clause extends ConfiguredTestFormatter {

    /* Alter statements are out of scope.
     * Code ist mostly kept as is.
     * In this case only some line breaks are eliminated.  */

    @Test
    public void alter_function() throws IOException {
        var input = """
                alter
                 function
                  f
                   compile
                    reuse
                     settings
                      ;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                alter
                 function f
                   compile
                    reuse
                     settings;
                """;
        assertEquals(expected, actual);
    }
}
