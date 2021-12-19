package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Result_cache_clause extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_9_38_simplified() throws IOException {
        var input = """
                CREATE OR REPLACE FUNCTION get_value
                  (p_param VARCHAR2,
                   p_app_id  NUMBER,
                   p_role_id NUMBER
                  )
                  RETURN VARCHAR2
                  RESULT_CACHE
                  AUTHID DEFINER
                IS
                  answer VARCHAR2(20);
                BEGIN
                  RETURN NULL; -- simplified
                END;
                """;
        var actual = formatter.format(input);
        var expected = """
                create or replace function get_value(p_param   varchar2,
                                                     p_app_id  number,
                                                     p_role_id number
                )
                   return varchar2
                   result_cache
                   authid definer
                is
                   answer varchar2(20);
                begin
                   return null; -- simplified
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized() throws IOException {
        var input = """
                CREATE
                FUNCTION
                get_value
                (
                p_param VARCHAR2,
                p_app_id  NUMBER,
                p_role_id NUMBER
                )
                RETURN VARCHAR2
                RESULT_CACHE
                RELIES_ON
                (
                A
                ,
                B
                ,
                C
                )
                AUTHID
                DEFINER
                IS
                BEGIN
                RETURN
                NULL
                ;
                END
                ;
                """;
        var actual = formatter.format(input);
        var expected = """
                create function get_value(
                   p_param   varchar2,
                   p_app_id  number,
                   p_role_id number
                )
                   return varchar2
                   result_cache relies_on (a, b, c)
                   authid definer
                is
                begin
                   return null;
                end;
                """;
        assertEquals(expected, actual);
    }
}
