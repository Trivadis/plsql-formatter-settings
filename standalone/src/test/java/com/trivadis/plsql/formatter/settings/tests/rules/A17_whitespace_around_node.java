package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A17_whitespace_around_node extends ConfiguredTestFormatter {

    @Test
    public void and_or_add_spaces() throws IOException {
        var input = """
                select 1 from dual where ((1=1)and(2=2)or(3=3));
                """;
        var actual = formatter.format(input);
        var expected = """
                select 1 from dual where ((1 = 1) and (2 = 2) or (3 = 3));
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void and_or_keep_nl() throws IOException {
        var input = """
                select 1 from dual where ((1=1)
                and
                (2=2)
                or
                (3=3));
                """;
        var actual = formatter.format(input);
        var expected = """
                select 1
                  from dual
                 where ((1 = 1)
                          and
                          (2 = 2)
                          or
                          (3 = 3));
                """;
        assertEquals(expected, actual);
    }

}
