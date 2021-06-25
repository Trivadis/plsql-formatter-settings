package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Assignment_statement extends ConfiguredTestFormatter {

    @Test
    public void single_line_input() throws IOException {
        var input = """
                begin
                collection_variable  (  index  )  : =  1;
                variable := 2;
                : host_variable := 3;
                : host_variable : indicator := 4;
                object . attribute := 5;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   collection_variable(index) := 1;
                   variable                   := 2;
                   :host_variable             := 3;
                   :host_variable:indicator   := 4;
                   object.attribute           := 5;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void multi_line_input() throws IOException {
        var input = """
                begin
                collection_variable
                (
                index
                )
                :
                =
                1
                ;
                variable
                :
                =
                2
                ;
                :
                host_variable
                :
                =
                3
                ;
                :
                host_variable
                :
                indicator
                :=
                4
                ;
                object
                .
                attribute
                :
                =
                5
                ;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   collection_variable(index) :=
                      1;
                   variable :=
                      2;
                   :host_variable :=
                      3;
                   :host_variable:indicator :=
                      4;
                   object.attribute :=
                      5;
                end;
                """;
        assertEquals(expected, actual);
    }


}
