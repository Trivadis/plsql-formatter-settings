package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Collection_method_invokation extends ConfiguredTestFormatter {

    @Test
    public void combined() throws IOException {
        var input = """
                set serveroutput on size unlimited
                declare
                   type number_type is table of number;
                   n_t number_type := number_type(1,2,3,4,5,6,7,8,9,10);
                begin
                   dbms_output.put_line(
                   n_t
                   .
                   count
                   );
                   n_t
                   .
                   delete
                   (
                   2
                   ,
                   6
                   )
                   ;
                   dbms_output.put_line(
                   case when n_t.exists(5) then 'true' else 'false' end
                   );
                   n_t
                   .
                   extend
                   (
                   5
                   ,
                   7
                   );
                   dbms_output.put_line(n_t.first || ' ' || n_t.last);
                   dbms_output.put_line('limit: ' || n_t.limit);
                   dbms_output.put_line('next: ' || n_t.next(1));
                   dbms_output.put_line('prior: ' || n_t.prior(7));
                   n_t.trim(4);
                   dbms_output.put_line('count: ' || n_t.count);
                   dbms_output.put_line(n_t(11));
                end;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                set serveroutput on size unlimited
                declare
                   type number_type is table of number;
                   n_t number_type := number_type(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
                begin
                   dbms_output.put_line(n_t.count);
                   n_t.delete(2, 6);
                   dbms_output.put_line(
                      case
                         when n_t.exists(5) then
                            'true'
                         else
                            'false'
                      end
                   );
                   n_t.extend(5, 7);
                   dbms_output.put_line(n_t.first
                      || ' '
                      || n_t.last);
                   dbms_output.put_line('limit: ' || n_t.limit);
                   dbms_output.put_line('next: ' || n_t.next(1));
                   dbms_output.put_line('prior: ' || n_t.prior(7));
                   n_t.trim(4);
                   dbms_output.put_line('count: ' || n_t.count);
                   dbms_output.put_line(n_t(11));
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
