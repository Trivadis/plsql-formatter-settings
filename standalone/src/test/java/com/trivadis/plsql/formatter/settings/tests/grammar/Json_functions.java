package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Json_functions extends ConfiguredTestFormatter {

    @Test
    public void create_json_doc() {
        var sql = """
                select json_serialize(
                          json_object(
                             'depts' : json_arrayagg(
                                json_object(
                                   'deptno' : d.deptno,
                                   'dname' : d.dname,
                                   'loc' : d.loc,
                                   'emps' :
                                   (
                                      select json_arrayagg(
                                                json_object(
                                                   'empno' : e.empno,
                                                   'ename' : e.ename,
                                                   'job' : e.job,
                                                   'mgr' : e.mgr,
                                                   'hiredate' : to_char(e.hiredate, 'YYYY-MM-DD'),
                                                   'sal' : e.sal,
                                                   'comm' : e.comm absent on null
                                                   returning clob
                                                )
                                             )
                                        from emp e
                                       where e.deptno = d.deptno
                                   )
                                   returning clob
                                )
                             )
                             returning clob
                          )
                          returning clob pretty
                       ) as my_json
                  from dept d;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void json_object_entry_on_same_line() throws IOException {
        var input = """
                select json_object(
                          'deptno'
                          :
                          d.deptno,
                          key
                          'dname'
                          value
                          d.dname,
                          'loc'
                          value
                          d.loc
                       )
                  from dept d;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select json_object(
                          'deptno' : d.deptno,
                          key 'dname' value d.dname,
                          'loc' value d.loc
                       )
                  from dept d;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void json_value_on_same_line() throws IOException {
        var input = """
                select json_value(
                          '{firstname:"John"}',
                          '$.lastname'
                          default
                          'No last name found'
                          on error
                       ) as "Last Name"
                  from dual;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                select json_value(
                          '{firstname:"John"}',
                          '$.lastname'
                          default 'No last name found' on error
                       ) as "Last Name"
                  from dual;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void json_dot_notation() {
        var sql = """
                select *
                  from j_purchaseorder
                       nested po_document.lineitems[*]
                       columns(itemnumber,
                               quantity number);
                """;
        formatAndAssert(sql);
    }


}
