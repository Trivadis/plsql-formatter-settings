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

    @Test
    public void tokenized_indices_of_control() throws IOException {
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
                i
                in
                indices
                of
                t_calling_codes
                when
                i
                !
                =
                'US'
                loop
                dbms_output.put_line('country '
                || i
                || ' has country calling code: '
                || t_calling_codes(i));
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
                   for i in indices of t_calling_codes when i != 'US'
                   loop
                      dbms_output.put_line('country '
                         || i
                         || ' has country calling code: '
                         || t_calling_codes(i));
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_pairs_of_control() throws IOException {
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
                l_index
                varchar2(2),
                l_value
                integer
                in
                pairs
                of
                t_calling_codes
                when
                l_value
                >
                1
                loop
                dbms_output.put_line('country '
                || l_index
                || ' has calling code: '
                || l_value);
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
                   for l_index varchar2(2), l_value integer in pairs of t_calling_codes when l_value > 1
                   loop
                      dbms_output.put_line('country '
                         || l_index
                         || ' has calling code: '
                         || l_value);
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_cursor_iteration_control() throws IOException {
        var input = """
                begin
                for
                r
                in
                (
                select
                ename
                ,
                sal
                from
                emp
                )
                loop
                dbms_output.put_line('ename '
                || r.ename
                || ': '
                || r.sal);
                end loop;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   for r in
                   (
                      select ename,
                             sal
                        from emp
                   )
                   loop
                      dbms_output.put_line('ename '
                         || r.ename
                         || ': '
                         || r.sal);
                   end loop;
                end;
                """;
        assertEquals(expected, actual);
    }
}
