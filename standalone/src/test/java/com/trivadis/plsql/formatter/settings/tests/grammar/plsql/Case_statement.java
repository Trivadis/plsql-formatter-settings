package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Case_statement extends ConfiguredTestFormatter {

    @Test
    public void simple() throws IOException {
        var input = """
                declare
                grade
                char
                (
                1
                )
                ;
                begin
                grade
                :
                =
                'B'
                ;
                                
                <
                <
                simple
                >
                >
                case
                grade
                when
                'A'
                then
                dbms_output
                .
                put_line
                (
                'Excellent'
                )
                ;
                when
                'B'
                then
                dbms_output
                .
                put_line
                (
                'Very Good'
                )
                ;
                when
                'C'
                then
                dbms_output
                .
                put_line
                (
                'Good'
                )
                ;
                when
                'D'
                then
                dbms_output
                .
                put_line
                (
                'Fair'
                )
                ;
                when
                'F'
                then
                dbms_output
                .
                put_line
                (
                'Poor'
                )
                ;
                else
                dbms_output
                .
                put_line
                (
                'No such grade'
                )
                ;
                end
                case
                simple
                ;
                end
                ;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   grade char(1);
                begin
                   grade :=
                      'B';
                                
                   <<simple>>
                   case grade
                      when 'A'
                      then
                         dbms_output.put_line('Excellent');
                      when 'B'
                      then
                         dbms_output.put_line('Very Good');
                      when 'C'
                      then
                         dbms_output.put_line('Good');
                      when 'D'
                      then
                         dbms_output.put_line('Fair');
                      when 'F'
                      then
                         dbms_output.put_line('Poor');
                      else
                         dbms_output.put_line('No such grade');
                   end case simple;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void searched() {
        var sql = """
                declare
                   grade char(1);
                begin
                   grade := 'B';
                       
                   <<searched>>
                   case
                      when grade = 'A' then
                         dbms_output.put_line('Excellent');
                      when grade = 'B' then
                         dbms_output.put_line('Very Good');
                      when grade = 'C' then
                         dbms_output.put_line('Good');
                      when grade = 'D' then
                         dbms_output.put_line('Fair');
                      when grade = 'F' then
                         dbms_output.put_line('Poor');
                      else
                         dbms_output.put_line('No such grade');
                   end case searched;
                end;
                /
                """;
        formatAndAssert(sql);
    }

}
