package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A15_line_break_before_node extends ConfiguredTestFormatter {

    @Test
    public void into_in_multi_table_insert() throws IOException {
        var input = """
                 insert all into t (c1) (select 1 as c1 from dual where 1 = 10);
                """;
        var actual = formatter.format(input);
        var expected = """
                insert all
                  into t (c1)
                (select 1 as c1 from dual where 1 = 10);
                """;
        assertEquals(expected, actual);
    }
}
