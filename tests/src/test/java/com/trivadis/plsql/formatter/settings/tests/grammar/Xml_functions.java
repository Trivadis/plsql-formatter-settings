package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Xml_functions extends ConfiguredTestFormatter {

    @Test
    public void create_xml_doc() {
        var sql = """
                select xmlserialize(
                          document
                          xmlelement(
                             "doc",
                             xmlelement(
                                "depts",
                                xmlagg(
                                   xmlelement(
                                      "dept",
                                      xmlattributes(
                                         d.deptno as "id",
                                         d.dname as "name"
                                      ),
                                      xmlelement("deptno", d.deptno),
                                      xmlelement("dname", d.dname),
                                      xmlelement("loc", d.loc),
                                      xmlelement(
                                         "emps",
                                         (
                                            select xmlagg(
                                                      xmlelement(
                                                         "emp",
                                                         xmlattributes(
                                                            e.empno as "id",
                                                            e.ename as "name",
                                                            e.job as "job"
                                                         ),
                                                         xmlelement("empno", e.empno),
                                                         xmlelement("ename", e.ename),
                                                         xmlelement("job", e.job),
                                                         xmlelement("mgr", e.mgr),
                                                         xmlelement("hiredate", to_char(hiredate, 'YYYY-MM-DD')),
                                                         xmlelement("sal", e.sal),
                                                         xmlelement("comm", e.comm)
                                                      )
                                                      order by e.empno
                                                   )
                                              from emp e
                                             where e.deptno = d.deptno
                                         )
                                      )
                                   )
                                   order by d.deptno
                                )
                             )
                          )
                          as blob
                          encoding 'UTF-8'
                          version '1.2'
                          indent size = 3
                       )
                  from dept d;
                """;
        formatAndAssert(sql);
    }
}
