package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A13_single_line_for_short_comparison_conditions extends ConfiguredTestFormatter {

    @Test
    public void simple_comparison() throws IOException {
        var input = """
                select *
                  from dual
                 where (1, 2, 3) = (
                           select 1, 2, 3 from dual
                       );
                """;
        var expected = """
                select *
                  from dual
                 where (1, 2, 3) = (select 1, 2, 3 from dual);
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void group_comparison() throws IOException {
        var input = """
                select *
                  from dual
                 where dummy = any (
                           'A', 'B', 'C', 'X', 'Y', 'Z'
                       );
                """;
        var expected = """
                select *
                  from dual
                 where dummy = any ('A', 'B', 'C', 'X', 'Y', 'Z');
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

}
