package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Datatype_attribute extends ConfiguredTestFormatter {

    @Test
    public void combined() throws IOException {
        var input = """
                declare
                t_coll
                t_collection_type;
                o_emp
                emp_ot;
                o_dept
                ref dept_ot;
                r_emp r_emp_type;
                c_cur1 sys_refcursor;
                c_cur2 ref cursor;
                r_dept dept%rowtype;
                l_1 boolean;
                l_2 pls_integer;
                l_3 binary_integer;
                l_4
                char
                (
                32767
                )
                ;
                l_5 nchar(32767);
                l_6 raw(32767);
                l_7 long raw(32767);
                l_8 blog;
                l_9 clob;
                l_10 nclob;
                l_11 binary_float;
                l_12 binary_double;
                l_13 simple_float;
                l_13 simple_double;
                l_14 number;
                l_15
                number
                (
                10
                )
                ;
                l_16
                number
                (
                10
                ,
                2
                )
                ;
                begin
                null;
                end;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   t_coll t_collection_type;
                   o_emp  emp_ot;
                   o_dept ref dept_ot;
                   r_emp  r_emp_type;
                   c_cur1 sys_refcursor;
                   c_cur2 ref cursor;
                   r_dept dept%rowtype;
                   l_1    boolean;
                   l_2    pls_integer;
                   l_3    binary_integer;
                   l_4    char(32767);
                   l_5    nchar(32767);
                   l_6    raw(32767);
                   l_7    long raw(32767);
                   l_8    blog;
                   l_9    clob;
                   l_10   nclob;
                   l_11   binary_float;
                   l_12   binary_double;
                   l_13   simple_float;
                   l_13   simple_double;
                   l_14   number;
                   l_15   number(10);
                   l_16   number(10, 2);
                begin
                   null;
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
