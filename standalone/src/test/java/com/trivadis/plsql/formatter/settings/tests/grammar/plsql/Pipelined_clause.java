package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Pipelined_clause extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized_pipelined() throws IOException {
        var input = """
                create
                function f
                return
                some_type
                pipelined
                is
                begin
                return;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                create function f
                   return some_type
                   pipelined
                is
                begin
                   return;
                end;
                """;
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"row", "table"})
    public void tokenized_pipelined_row_polymorphic(String row_or_table) throws IOException {
        var input = """
                create
                function f
                return
                some_type
                pipelined
                #ROW_OR_TABLE#
                polymorphic
                is
                begin
                return;
                end;
                """.replace("#ROW_OR_TABLE#", row_or_table);
        var actual = formatter.format(input);
        var expected = """
                create function f
                   return some_type
                   pipelined #ROW_OR_TABLE# polymorphic
                is
                begin
                   return;
                end;
                """.replace("#ROW_OR_TABLE#", row_or_table);
        assertEquals(expected, actual);
    }

    @Test
    public void exampele_13_36_skip_col() throws IOException {
        var input = """
                CREATE PACKAGE skip_col_pkg AS
                                
                  -- OVERLOAD 1: Skip by name --
                  FUNCTION skip_col(tab TABLE,
                                    col COLUMNS)
                           RETURN
                           TABLE
                           PIPELINED
                           ROW 
                           POLYMORPHIC 
                           USING
                           skip_col_pkg;
                                
                  FUNCTION describe(tab IN OUT DBMS_TF.TABLE_T,
                                    col        DBMS_TF.COLUMNS_T)
                           RETURN DBMS_TF.DESCRIBE_T;
                                
                  -- OVERLOAD 2: Skip by type --
                  FUNCTION skip_col(tab       TABLE,
                                    type_name VARCHAR2,
                                    flip      VARCHAR2 DEFAULT 'False')
                           RETURN
                           TABLE
                           PIPELINED
                           ROW
                           POLYMORPHIC
                           USING
                           skip_col_pkg;
                                
                  FUNCTION describe(tab       IN OUT DBMS_TF.TABLE_T,
                                    type_name        VARCHAR2,
                                    flip             VARCHAR2 DEFAULT 'False')
                           RETURN DBMS_TF.DESCRIBE_T;
                                
                END skip_col_pkg;
                """;
        var actual = formatter.format(input);
        var expected = """
                create package skip_col_pkg as
                                
                   -- OVERLOAD 1: Skip by name --
                   function skip_col(tab table,
                                     col columns)
                      return table
                      pipelined row polymorphic using skip_col_pkg;
                                
                   function describe(tab in out dbms_tf.table_t,
                                     col        dbms_tf.columns_t)
                      return dbms_tf.describe_t;
                                
                   -- OVERLOAD 2: Skip by type --
                   function skip_col(tab       table,
                                     type_name varchar2,
                                     flip      varchar2 default 'False')
                      return table
                      pipelined row polymorphic using skip_col_pkg;
                                
                   function describe(tab in out dbms_tf.table_t,
                                     type_name  varchar2,
                                     flip       varchar2 default 'False')
                      return dbms_tf.describe_t;
                                
                end skip_col_pkg;
                """;
        assertEquals(expected, actual);
    }


}
