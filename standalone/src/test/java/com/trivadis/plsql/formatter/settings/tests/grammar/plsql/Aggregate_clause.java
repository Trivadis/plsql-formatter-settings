package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Aggregate_clause extends ConfiguredTestFormatter {

    @Test
    public void standalone_single_line() {
        var sql = """
                create function spatialunion(x geometry) return geometry aggregate using spatialunionroutines;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void standalone_multi_line() throws IOException {
        var input = """
                create function spatialunion(
                   x geometry
                ) return geometry
                aggregate
                using
                spatialunionroutines;
                """;
        var expected = """
                create function spatialunion(
                   x geometry
                ) return geometry
                   aggregate using spatialunionroutines;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

}
