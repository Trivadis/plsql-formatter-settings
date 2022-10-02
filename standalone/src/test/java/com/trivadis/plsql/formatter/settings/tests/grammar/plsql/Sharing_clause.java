package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Sharing_clause extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
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
