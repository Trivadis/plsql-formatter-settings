package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Iterator extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void tokenized_stepped_control() throws IOException {
        var input = """
                begin
                for
                i
                immutable
                number
                (
                5
                ,
                2
                )
                in
                reverse
                1
                .
                .
                10
                by
                0.5
                loop
                dbms_output.put_line(i);
                end loop;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   for i immutable number(5, 2) in reverse 1..10 by 0.5
                   loop
                      dbms_output.put_line(i);
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_singe_expression_control() throws IOException {
        var input = """
                begin
                for
                i
                mutable
                pls_integer
                in
                1
                ,
                repeat
                i
                +
                1
                while
                i
                <
                10
                loop
                if i = 2 then
                i := 6;
                end if;
                dbms_output.put_line(i);
                end loop;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   for i mutable pls_integer in 1, repeat i + 1 while i < 10
                   loop
                      if i = 2 then
                         i := 6;
                      end if;
                      dbms_output.put_line(i);
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_values_of_control() throws IOException {
        var input = """
                declare
                type t_calling_code_type is table of integer index by varchar2(2);
                t_calling_codes t_calling_code_type;
                begin
                t_calling_codes := t_calling_code_type(
                'U' || 'S' => 1, 'DE' => 49, 'AT' => 43,
                'CH' => 41, 'DK' => 45, 'RO' => 40
                );
                for
                l_value
                in
                values
                of
                t_calling_codes
                when
                l_value
                >
                1
                loop
                dbms_output.put_line('country calling code: ' || l_value);
                end loop;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   type t_calling_code_type is table of integer index by varchar2(2);
                   t_calling_codes t_calling_code_type;
                begin
                   t_calling_codes := t_calling_code_type(
                                         'U' || 'S' => 1, 'DE' => 49, 'AT' => 43,
                                         'CH' => 41, 'DK' => 45, 'RO' => 40
                                      );
                   for l_value in values of t_calling_codes when l_value > 1
                   loop
                      dbms_output.put_line('country calling code: ' || l_value);
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }
}
