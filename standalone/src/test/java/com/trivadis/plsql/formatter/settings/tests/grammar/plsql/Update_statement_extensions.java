package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Update_statement_extensions extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_6_61_tokenized() throws IOException {
        var input = """
                DECLARE
                  default_week  schedule%ROWTYPE;
                BEGIN
                  default_week.Mon := 'Day Off';
                  default_week.Tue := '0900-1800';
                  default_week.Wed := '0900-1800';
                  default_week.Thu := '0900-1800';
                  default_week.Fri := '0900-1800';
                  default_week.Sat := '0900-1800';
                  default_week.Sun := 'Day Off';
                
                  FOR i IN 1..3 LOOP
                    default_week.week    := i;
                
                    UPDATE schedule
                    SET
                    ROW
                    =
                    default_week
                    WHERE week = i;
                  END LOOP;
                END;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                declare
                   default_week schedule%rowtype;
                begin
                   default_week.mon := 'Day Off';
                   default_week.tue := '0900-1800';
                   default_week.wed := '0900-1800';
                   default_week.thu := '0900-1800';
                   default_week.fri := '0900-1800';
                   default_week.sat := '0900-1800';
                   default_week.sun := 'Day Off';
                                
                   for i in 1..3
                   loop
                      default_week.week := i;
                                
                      update schedule
                         set row = default_week
                       where week = i;
                   end loop;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

}
