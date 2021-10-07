package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Fetch_statement extends ConfiguredTestFormatter {

    @Test
    public void example_6_6_fetch_inside_loop() throws IOException {
        var input = """
                begin
                   open c1;
                   loop
                      fetch
                      c1
                      into
                      v_lastname
                      ,
                      v_jobid
                      ;
                      exit when c1%notfound;
                   end loop;
                   close c1;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1;
                   loop
                      fetch c1
                         into v_lastname,
                              v_jobid;
                      exit when c1%notfound;
                   end loop;
                   close c1;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void bulk_collect() throws IOException {
        var input = """
                begin
                   open c1;
                   fetch
                   c1
                   bulk
                   collect into
                   a
                   ,
                   b
                   ,
                   c
                   limit 10
                   ;
                   close c1;
                end;
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   open c1;
                   fetch c1
                      bulk
                      collect into a,
                                   b,
                                   c
                      limit 10;
                   close c1;
                end;
                """;
        assertEquals(expected, actual);
    }

}
