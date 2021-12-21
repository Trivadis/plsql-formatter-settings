package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        var actual = formatter.format(input);
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
