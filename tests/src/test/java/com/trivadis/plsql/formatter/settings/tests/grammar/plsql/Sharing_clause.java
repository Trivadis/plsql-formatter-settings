package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Sharing_clause extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized_metadata() throws IOException {
        var input = """
                CREATE
                VIEW v
                SHARING
                =
                METADATA
                AS
                SELECT
                *
                FROM
                t
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                create view v
                sharing = metadata
                as
                   select *
                     from t;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_none() throws IOException {
        var input = """
                CREATE
                VIEW v
                SHARING
                =
                NONE
                AS
                SELECT
                *
                FROM
                t
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                create view v
                sharing = none
                as
                   select *
                     from t;
                """;
        assertEquals(expected, actual);
    }
}
