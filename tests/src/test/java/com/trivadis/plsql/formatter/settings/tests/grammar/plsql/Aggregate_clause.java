package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Aggregate_clause extends ConfiguredTestFormatter {

    @Test
    public void standalone_single_line() {
        var sql = """
                create function spatialunion(x geometry) return geometry aggregate using spatialunionroutines;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void standalone_multi_line() {
        var sql = """
                create function spatialunion(
                   x geometry
                ) return geometry
                aggregate
                using
                spatialunionroutines;
                """;
        formatAndAssert(sql);
    }

}
