package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Sql_macro_clause extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void scalar_macro() throws IOException {
        var input = """
                CREATE FUNCTION date_string(dat DATE)\s
                                    RETURN VARCHAR2 SQL_MACRO(SCALAR) IS
                BEGIN
                   RETURN q'{
                             TO_CHAR(dat, 'YYYY-MM-DD')
                          }';
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                create function date_string(dat date)
                   return varchar2 sql_macro(scalar) is
                begin
                   return q'{
                             TO_CHAR(dat, 'YYYY-MM-DD')
                          }';
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void default_macro() throws IOException {
        var input = """
                CREATE FUNCTION take (n NUMBER, t DBMS_TF.table_t)
                                      RETURN VARCHAR2 SQL_MACRO IS
                BEGIN
                   RETURN 'SELECT * FROM t FETCH FIRST take.n ROWS ONLY';
                END;
                """;
        var actual = formatter.format(input);
        var expected = """
                create function take(n number,
                                     t dbms_tf.table_t)
                   return varchar2 sql_macro is
                begin
                   return 'SELECT * FROM t FETCH FIRST take.n ROWS ONLY';
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void table_macro_tokenized() throws IOException {
        var input = """
                CREATE FUNCTION take (n NUMBER, t DBMS_TF.table_t)
                RETURN
                VARCHAR2
                SQL_MACRO
                (
                TYPE
                =
                >
                TABLE
                )
                IS
                BEGIN
                   RETURN 'SELECT * FROM t FETCH FIRST take.n ROWS ONLY';
                END;
                """;
        var actual = formatter.format(input);
        var expected = """
                create function take(n number,
                                     t dbms_tf.table_t)
                   return varchar2
                   sql_macro (type => table)
                is
                begin
                   return 'SELECT * FROM t FETCH FIRST take.n ROWS ONLY';
                end;
                """;
        assertEquals(expected, actual);
    }
}
