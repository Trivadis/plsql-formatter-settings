package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class R2_indentation extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        getFormatter().options.put(getFormatter().spaceAfterCommas, false);
        getFormatter().options.put(getFormatter().alignRight, false);
        getFormatter().options.put(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.None);
        getFormatter().options.put(getFormatter().breakOnSubqueries, false);
    }

    @Nested
    class Anonymous_plsql_block {

        @Test
        public void simple() throws IOException {
            var input = """
                    begin
                    null;
                    delete from t;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       null;
                       delete from t;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_exception_handler() throws IOException {
            var input = """
                    begin
                    null;
                    delete from t;
                    exception
                    when e_ex1 then
                    p1;
                    when others then
                    raise;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       null;
                       delete from t;
                    exception
                       when e_ex1 then
                          p1;
                       when others then
                          raise;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void nested() throws IOException {
            var input = """
                    begin
                    begin
                    begin
                    null;
                    end;
                    delete from t;
                    end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       begin
                          begin
                             null;
                          end;
                          delete from t;
                       end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_declare_section() throws IOException {
            var input = """
                    declare
                    l_a integer;
                    l_b varchar2(20);
                    cursor l_c is
                    select a, b, c, d
                    from t
                    where a = b
                    order by a, b;
                    begin
                    null;
                    delete from t;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    declare
                       l_a integer;
                       l_b varchar2(20);
                       cursor l_c is
                          select a, b, c, d
                          from t
                          where a = b
                          order by a, b;
                    begin
                       null;
                       delete from t;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class plsql {

        @Test
        public void procedure_arguments() throws IOException {
            var input = """
                    begin
                    p1(
                    a => a123456789 * b12345789 * c1234567
                    ,b => a123456789 * b12345789 * c1234567
                    );
                    p2 (
                    a123456789 * b12345789 * c1234567
                    , a123456789 * b12345789 * c1234567
                    );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       p1(
                          a => a123456789 * b12345789 * c1234567
                         ,b => a123456789 * b12345789 * c1234567
                       );
                       p2(
                          a123456789 * b12345789 * c1234567
                         ,a123456789 * b12345789 * c1234567
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void function_arguments() throws IOException {
            var input = """
                    begin
                    l1 := f1(
                    a => a123456789 * b12345789 * c1234567
                    ,b => a123456789 * b12345789 * c1234567
                    );
                    l2 := f2 (
                    a123456789 * b12345789 * c1234567
                    ,a123456789 * b12345789 * c1234567
                    );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l1 := f1(
                                a => a123456789 * b12345789 * c1234567
                               ,b => a123456789 * b12345789 * c1234567
                             );
                       l2 := f2(
                                a123456789 * b12345789 * c1234567
                               ,a123456789 * b12345789 * c1234567
                             );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void record_fields() throws IOException {
            var input = """
                    declare
                    type t_xyz is record (
                    c1 integer
                    ,c2 varchar2(20)
                    ,c3 date
                    );
                    begin
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    declare
                       type t_xyz is record (
                          c1 integer
                         ,c2 varchar2(20)
                         ,c3 date
                       );
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void if_statement() throws IOException {
            var input = """
                    begin
                    if a=b then
                    p1a;
                    p1b;
                    elsif a=c then
                    p2a;
                    p2b;
                    else
                    p3a;
                    p3b;
                    end if;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       if a = b then
                          p1a;
                          p1b;
                       elsif a = c then
                          p2a;
                          p2b;
                       else
                          p3a;
                          p3b;
                       end if;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void simple_case_statement() throws IOException {
            var input = """
                    begin
                    case a
                    when b then
                    p1a;
                    p1b;
                    when c then
                    p2a;
                    p2b;
                    else
                    p3a;
                    p3b;
                    end case;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       case a
                          when b then
                             p1a;
                             p1b;
                          when c then
                             p2a;
                             p2b;
                          else
                             p3a;
                             p3b;
                       end case;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void searched_case_statement() throws IOException {
            var input = """
                    begin
                    case
                    when a=b then
                    p1a;
                    p1b;
                    when a=c then
                    p2a;
                    p2b;
                    else
                    p3a;
                    p3b;
                    end case;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       case
                          when a = b then
                             p1a;
                             p1b;
                          when a = c then
                             p2a;
                             p2b;
                          else
                             p3a;
                             p3b;
                       end case;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void basic_loop_statement() throws IOException {
            var input = """
                    begin
                    loop
                    exit when a=b;
                    p1;
                    p2;
                    end loop;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       loop
                          exit when a = b;
                          p1;
                          p2;
                       end loop;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void for_loop_statement() throws IOException {
            var input = """
                    begin
                    for i in 1..10
                    loop
                    p1(i);
                    p2;
                    end loop;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       for i in 1..10
                       loop
                          p1(i);
                          p2;
                       end loop;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void for_loop_statement_separated() throws IOException {
            var input = """
                    begin
                    for i in 1..10
                    loop
                    p1(i);
                    p2;
                    end loop;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       for i in 1..10
                       loop
                          p1(i);
                          p2;
                       end loop;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void while_loop_statement() throws IOException {
            var input = """
                    begin
                    while a != b
                    loop
                    p1;
                    p2;
                    end loop;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       while a != b
                       loop
                          p1;
                          p2;
                       end loop;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void while_loop_statement_separated() throws IOException {
            var input = """
                    begin
                    while a != b
                    loop
                    p1;
                    p2;
                    end loop;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       while a != b
                       loop
                          p1;
                          p2;
                       end loop;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void labelled_statements() throws IOException {
            var input = """
                    <<outer_block>>
                    begin
                    <<inner_block>>
                    begin
                    <<while_loop>>
                    while a != b
                    loop
                    <<p1_stmt>>
                    p1;
                    <<p2_stmt>>
                    p2;
                    end loop;
                    end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    <<outer_block>>
                    begin
                       <<inner_block>>
                       begin
                          <<while_loop>>
                          while a != b
                          loop
                             <<p1_stmt>>
                             p1;
                             <<p2_stmt>>
                             p2;
                          end loop;
                       end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void searched_case_expr_plsql_assignment() throws IOException {
            var input = """
                    begin
                    l_filter := case
                    when l_value is null then
                    'abc'
                    when l_value is not null then
                    'def'
                    else
                    'xyz'
                    end
                    || ' another value'
                    || ' another value';
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l_filter := case
                                      when l_value is null then
                                         'abc'
                                      when l_value is not null then
                                         'def'
                                      else
                                         'xyz'
                                   end
                                   || ' another value'
                                   || ' another value';
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void simple_case_expr_plsql_assignment() throws IOException {
            var input = """
                    begin
                    l_filter := case l_value
                    when 1 then
                    'abc'
                    when 2 then
                    'def'
                    else
                    'xyz'
                    end
                    || ' another value'
                    || ' another value';
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l_filter := case l_value
                                      when 1 then
                                         'abc'
                                      when 2 then
                                         'def'
                                      else
                                         'xyz'
                                   end
                                   || ' another value'
                                   || ' another value';
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class standalone {

        @Test
        public void procedure() throws IOException {
            var input = """
                    create procedure p (
                    p1 in varchar2
                    ,p2 in date
                    ) is
                    l_var integer;
                    begin
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create procedure p(
                       p1 in varchar2
                      ,p2 in date
                    ) is
                       l_var integer;
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void function() throws IOException {
            var input = """
                    create function f (
                    p1 in varchar2
                    ,p2 in date
                    ) return boolean is
                    l_var integer;
                    begin
                    return true;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create function f(
                       p1 in varchar2
                      ,p2 in date
                    ) return boolean is
                       l_var integer;
                    begin
                       return true;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class type {

        @Test
        public void object_spec() throws IOException {
            var input = """
                    create or replace type obj_type as object (
                    a integer
                    ,b varchar2(20)
                    ,static function f (
                    p1 in varchar2
                    ,p2 in integer
                    ) return varchar2
                    ,member function to_string return varchar2
                    );
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace type obj_type as object (
                       a integer
                      ,b varchar2(20)
                      ,static function f(
                          p1 in varchar2
                         ,p2 in integer
                       ) return varchar2
                      ,member function to_string return varchar2
                    );
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void object_body() throws IOException {
            var input = """
                    create or replace type body obj_type as
                                        
                    static function f (
                    p1 in varchar2
                    ,p2 in integer
                    ) return varchar2 is
                    l_var integer;
                    begin
                    return p1 || p2;
                    end;
                                        
                    member function to_string return varchar2 is
                    begin
                    return null;
                    end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace type body obj_type as
                                        
                       static function f(
                          p1 in varchar2
                         ,p2 in integer
                       ) return varchar2 is
                          l_var integer;
                       begin
                          return p1 || p2;
                       end;
                                        
                       member function to_string return varchar2 is
                       begin
                          return null;
                       end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class package_ {

        @Test
        public void spec() throws IOException {
            var input = """
                    create or replace package abc as
                    g_pkgname constant varchar2(32) := 'ABC';
                            
                    procedure p (
                    p1 in integer
                    ,p2 in varchar2
                    );
                            
                    function f return varchar2;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package abc as
                       g_pkgname constant varchar2(32) := 'ABC';
                            
                       procedure p(
                          p1 in integer
                         ,p2 in varchar2
                       );
                            
                       function f return varchar2;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void body() throws IOException {
            var input = """
                    create or replace package body abc as
                    g_pkgname constant varchar2(32) := 'ABC';
                            
                    procedure p (
                    p1 in integer
                    ,p2 in varchar2
                    ) is
                    l_var integer;
                    begin
                    something(p1, p2);
                    exception
                    when others then
                    null;
                    end;
                            
                    function f return varchar2 is
                    l_var integer;
                    begin
                    return '1';
                    end;
                            
                    begin
                    null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package body abc as
                       g_pkgname constant varchar2(32) := 'ABC';
                            
                       procedure p(
                          p1 in integer
                         ,p2 in varchar2
                       ) is
                          l_var integer;
                       begin
                          something(p1, p2);
                       exception
                          when others then
                             null;
                       end;
                            
                       function f return varchar2 is
                          l_var integer;
                       begin
                          return '1';
                       end;
                            
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void formatted_spec_with_comments() {
            var sql = """
                    create or replace package pkg as
                       -- private constants
                       co_name varchar2(20) := 'Name';
                       
                       /**
                        * Some comments
                        *
                        */
                       procedure p;
                    end pkg;
                    /
                    """;
            formatAndAssert(sql);
        }
    }

    @Nested
    class view {

        @Test
        public void simple() throws IOException {
            var input = """
                    create or replace view v as
                    select a, b, c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace view v as
                       select a, b, c
                       from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_clause() throws IOException {
            var input = """
                    create or replace view v as
                    with
                    a as (
                    select a, b, c
                    from t1
                    )
                    ,b as (
                    select a, b, c
                    from t2
                    )
                    select *
                    from a
                    minus
                    select *
                    from b;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace view v as
                       with
                          a as (
                             select a, b, c
                             from t1
                          )
                         ,b as (
                             select a, b, c
                             from t2
                          )
                       select *
                       from a
                       minus
                       select *
                       from b;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void with_column_aliases() throws IOException {
            var input = """
                    create view emp_sal (
                    emp_id
                    ,last_name
                    ,email unique rely disable novalidate
                    ,constraint id_pk primary key (emp_id) rely disable novalidate
                    )
                    as select employee_id, last_name, email
                    from employees
                    with read only;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create view emp_sal (
                       emp_id
                      ,last_name
                      ,email unique rely disable novalidate
                      ,constraint id_pk primary key (emp_id) rely disable novalidate
                    )
                    as
                       select employee_id, last_name, email
                       from employees
                    with read only;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class select {

        @Test
        public void select_list() throws IOException {
            var input = """
                    select a, sum(
                    a123456789 * b12345789 * c123456789 * d123456789 * e123456789
                    ) as sum_of_a
                    from t
                    group by a;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a, sum(
                              a123456789 * b12345789 * c123456789 * d123456789 * e123456789
                           ) as sum_of_a
                    from t
                    group by a;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void from() throws IOException {
            var input = """
                    select a
                    from t, (
                    select * from t
                    ) t2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                    from t, (
                            select * from t
                         ) t2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void where() throws IOException {
            var input = """
                    select a
                    from t1
                    where exists (
                    select * from t2 where c1 is not null
                    );
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a
                    from t1
                    where exists (
                             select * from t2 where c1 is not null
                          );
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void connect_by() throws IOException {
            var input = """
                    select level,
                    ename
                    from emp
                    connect by
                    prior empno = mgr;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select level
                          ,ename
                    from emp
                    connect by prior empno = mgr;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void start_with() throws IOException {
            var input = """
                    select level
                    ,ename
                    from emp
                    start with
                    mgr is null
                    connect by
                    prior empno = mgr;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select level
                          ,ename
                    from emp
                    start with mgr is null
                    connect by prior empno = mgr;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void group_by() throws IOException {
            var input = """
                    select deptno, count(*)
                    from emp
                    group
                    by
                    deptno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select deptno, count(*)
                    from emp
                    group by deptno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void having() throws IOException {
            var input = """
                    select deptno, count(*)
                    from emp
                    group
                    by
                    deptno
                    having
                    count(*) > 4;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select deptno, count(*)
                    from emp
                    group by deptno
                    having count(*) > 4;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void order_by() throws IOException {
            var input = """
                    select empno, ename
                    from emp
                    order
                    by
                    ename
                    ,empno;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select empno, ename
                    from emp
                    order by ename
                         ,empno;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        void table_function() throws IOException {
            var input = """
                    select value(p) as col
                    from table(
                    cast(
                    lineage_util.get_dep_cols_from_query(
                    in_parse_user => r_insert.owner,
                    in_query => l_query,
                    in_column_pos => l_column_id,
                    in_recursive => in_recursive
                    ) as xyz
                    )
                    ) p;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select value(p) as col
                    from table(
                            cast(
                               lineage_util.get_dep_cols_from_query(
                                  in_parse_user => r_insert.owner
                                 ,in_query      => l_query
                                 ,in_column_pos => l_column_id
                                 ,in_recursive  => in_recursive
                               ) as xyz
                            )
                         ) p;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        void select_with_oracle_join_and_nested_ansi_join() throws IOException {
            var input = """
                    select *
                    from emp, dual d1, dual d2 where (1 = 2 or 3 = 4) and
                    0  = 1 + 2 and exists(select 1, 2, 3
                    from wsh_new_deliveries wnd
                    join wsh_delivery_assignments wda
                    on wnd.delivery_id = wda.delivery_id
                    join hz_locations hl
                    on hps.location_id = hl.location_id
                    );
                    """;
            // whole from clause is indented, that's correct, right-align moves keywords to the left
            var expected = """
                    select *
                    from emp, dual d1, dual d2
                    where (1 = 2 or 3 = 4) and
                          0 = 1 + 2 and exists(select 1, 2, 3
                                               from wsh_new_deliveries wnd
                                                    join wsh_delivery_assignments wda
                                                    on wnd.delivery_id = wda.delivery_id
                                                    join hz_locations hl
                                                    on hps.location_id = hl.location_id
                          );
                    """;
            var actual = formatter.format(input);
            assertEquals(expected, actual);
        }

        @Test
        void nested_query() throws IOException {
            var input = """
                    select (select count(*)
                    from dept
                    ) as dept_count,
                    e.empno as emp_no
                    from (select *
                    from (select *
                    from (
                    select *
                    from emp
                    where sal > 1000
                    )
                    )
                    ) e;
                    """;
            var expected = """
                    select (select count(*)
                            from dept
                           ) as dept_count
                          ,e.empno as emp_no
                    from (select *
                          from (select *
                                from (
                                        select *
                                        from emp
                                        where sal > 1000
                                     )
                               )
                         ) e;
                    """;
            var actual = formatter.format(input);
            assertEquals(expected, actual);
        }

        @Test
        void analytic_function() throws IOException {
            var input = """
                    select last_name
                    ,salary
                    ,count(
                    employees.salary
                    ) over (
                    order by salary
                    range between 50 preceding and 150 following
                    ) as mov_count
                    from employees;
                    """;
            var expected = """
                    select last_name
                          ,salary
                          ,count(
                              employees.salary
                           ) over (
                              order by salary
                              range between 50 preceding and 150 following
                           ) as mov_count
                    from employees;
                    """;
            var actual = formatter.format(input);
            assertEquals(expected, actual);
        }

        @Test
        void distinct() throws IOException {
            var input = """
                    select distinct
                    owner
                    ,object_type
                    ,object_name
                    from t;
                    """;
            var expected = """
                    select distinct
                           owner
                          ,object_type
                          ,object_name
                    from t;
                    """;
            var actual = formatter.format(input);
            assertEquals(expected, actual);
        }

    }

    @Nested
    class insert {

        @Test
        public void singletable_column_list_values() throws IOException {
            var input = """
                    insert into t (
                    c1
                    ,c2
                    ,c3
                    )
                    values (
                    '1'
                    ,'2'
                    ,'3'
                    );
                    """;
            var actual = formatter.format(input);
            var expected = """
                    insert into t (
                              c1
                             ,c2
                             ,c3
                           )
                    values (
                              '1'
                             ,'2'
                             ,'3'
                           );
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void multitable_insert() throws IOException {
            var input = """
                    insert all
                    into t1 (
                    c1
                    ,c2
                    ,c3
                    )
                    into t2 (c1, c2, c3) (select 1 as c1, 2 as c2, 3 as c3
                    from dual
                    where dummy = 'X');
                    """;
            var actual = formatter.format(input);
            var expected = """
                    insert all
                    into t1 (
                            c1
                           ,c2
                           ,c3
                         )
                    into t2 (c1, c2, c3)
                    (select 1 as c1, 2 as c2, 3 as c3
                     from dual
                     where dummy = 'X');
                    """;
            assertEquals(expected, actual);
        }
    }
}
