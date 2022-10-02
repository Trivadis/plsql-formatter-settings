package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Qualified_expression extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_6_11() throws IOException {
        // Assigning Values to Associative Array Type Variables Using Qualified Expressions
        var input = """
                DECLARE
                  TYPE t_aa IS TABLE OF BOOLEAN INDEX BY PLS_INTEGER;
                  v_aa1 t_aa := t_aa(1=>FALSE,
                                     2=>TRUE,
                                     3=>NULL);
                BEGIN
                  DBMS_OUTPUT.PUT_LINE(print_bool(v_aa1(1)));
                  DBMS_OUTPUT.PUT_LINE(print_bool(v_aa1(2)));
                  DBMS_OUTPUT.PUT_LINE(print_bool(v_aa1(3)));
                END;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   type t_aa is table of boolean index by pls_integer;
                   v_aa1 t_aa := t_aa(1 => false,
                                      2 => true,
                                      3 => null);
                begin
                   dbms_output.put_line(print_bool(v_aa1(1)));
                   dbms_output.put_line(print_bool(v_aa1(2)));
                   dbms_output.put_line(print_bool(v_aa1(3)));
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_6_8() throws IOException {
        // Iterator Choice Association in Qualified Expressions
        var input = """
                BEGIN
                result := vec_t (FOR i IN 1..n => fib(i));
                result := vec_t (FOR i IN 1..n => 2*i);
                END;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   result := vec_t(for i in 1..n => fib(i));
                   result := vec_t(for i in 1..n => 2 * i);
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_6_9() throws IOException {
        // Index Iterator Choice Association in Qualified Expressions
        var input = """
                BEGIN
                result := vec_t (FOR I,j IN PAIRS OF vec INDEX I => j+n);
                result := vec_t (FOR i IN 2..n BY 2 INDEX i/2 => i);
                END;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   result := vec_t(for i, j in pairs of vec index i => j + n);
                   result := vec_t(for i in 2..n by 2 index i / 2 => i);
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_6_10() throws IOException {
        // Index Iterator Choice Association in Qualified Expressions
        var input = """
                BEGIN
                result := vec_t (FOR v IN VALUES OF v1,
                                          REVERSE VALUES OF v2
                                   SEQUENCE => v);
                result := vec_t (FOR i IN 1..n WHEN is_prime(i)
                                   SEQUENCE => i);
                END;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   result := vec_t(for v in values of v1, reverse values of v2 sequence => v);
                   result := vec_t(for i in 1..n when is_prime(i) sequence => i);
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_5_26() throws IOException {
        // Using Dynamic SQL As An Iteration Control In a Qualified Expression
        var input = """
                v := vec_rec_t( FOR r rec_t IN (EXECUTE IMMEDIATE query_var) SEQUENCE => r);
                """;
        var actual = getFormatter().format(input);
        var expected = """
                v := vec_rec_t(for r rec_t in (execute immediate query_var) sequence => r);
                """;
        assertEquals(expected, actual);
    }

}
