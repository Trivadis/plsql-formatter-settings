package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Collection_variable_declaration extends ConfiguredTestFormatter {

    @Test
    public void assoc_array_type_def() throws IOException {
        var input = """
                declare
                   type
                   x_type
                   is
                   table
                   of
                   varchar2
                   (
                   100
                   )
                   not
                   null
                   index
                   by
                   varchar2
                   (
                   10
                   )
                   ;
                   t_x
                   x_type
                   :
                   =
                   x_type
                   (
                   '1'
                   =
                   >
                   'one'
                   ,
                   '2'
                   =>
                   'two'
                   ,
                   '3'
                   =>
                   'three'
                   )
                   ;
                begin
                   null;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type x_type is
                      table of varchar2(100) not null index by varchar2(10);
                   t_x x_type :=
                      x_type(
                         '1' => 'one',
                         '2' => 'two',
                         '3' => 'three'
                      );
                begin
                   null;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void varray_type_def() throws IOException {
        var input = """
                declare
                   type x_type is varray (10) of varchar2 (10) not null;
                   t_x x_type :=
                   x_type('one', 'two', 'three');
                begin
                   null;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type x_type is varray(10) of varchar2(10) not null;
                   t_x x_type :=
                      x_type('one', 'two', 'three');
                begin
                   null;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void nested_type_def() {
        var sql = """
                declare
                   type x_type is table of varchar2(10) not null;
                   t_x x_type := x_type('one', 'two', 'three');
                begin
                   null;
                end;
                """;
        formatAndAssert(sql);
    }

}
