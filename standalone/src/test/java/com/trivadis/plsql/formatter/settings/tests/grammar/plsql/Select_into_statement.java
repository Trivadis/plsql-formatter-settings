package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Select_into_statement extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_3_25() throws IOException {
        var input = """
                BEGIN
                  SELECT salary * 0.10 INTO bonus
                  FROM employees
                  WHERE employee_id = 100;
                END;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   select salary * 0.10 into bonus
                     from employees
                    where employee_id = 100;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_3_16_tokenized() throws IOException {
        var input = """
                BEGIN
                  SELECT
                  employee_id
                  ,
                  last_name
                  BULK
                  COLLECT
                  INTO
                  enums
                  ,
                  names
                  FROM
                  employees
                  ORDER
                  BY
                  employee_id
                  ;
                END;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   select employee_id,
                          last_name
                     bulk collect
                     into enums,
                          names
                     from employees
                    order by employee_id;
                end;
                """;
        assertEquals(expected, actual);
    }
}
