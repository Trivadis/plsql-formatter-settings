package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Rule_2_3_space_indentation extends ConfiguredTestFormatter {

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
            var expected =
                    """
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
                    a => 1
                    ,b => 2
                    );
                    p2 (
                    1
                    , 2
                    );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       p1(
                          a => 1
                         ,b => 2
                       );
                       p2 (
                          1
                        , 2
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
                    a => 1
                    ,b => 2
                    );
                    l2 := f2 (
                    1
                    ,2
                    );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l1 := f1(
                          a => 1
                         ,b => 2
                       );
                       l2 := f2 (
                          1
                         ,2
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
                          exit when a=b;
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
                      ,static function f (
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
        
                       procedure p (
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
            assertEquals(expected, actual);
        }

        @Test
        public void spec_with_long_line() throws IOException {
            var input = """
                    create or replace package pkg as
                    procedure very_long_line (p_a_12345678901234567890 in integer, p_b_12345678901234567890 in integer, p_c_12345678901234567890 in integer, p_d_12345678901234567890 in integer, p_e_12345678901234567890 in integer, p_f_12345678901234567890 in integer, p_g_12345678901234567890 in integer);
                    procedure short_line (p_a in integer);
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package pkg as
                       procedure very_long_line (p_a_12345678901234567890 in integer, p_b_12345678901234567890 in integer, p_c_12345678901234567890
                          in integer, p_d_12345678901234567890 in integer, p_e_12345678901234567890 in integer, p_f_12345678901234567890 in integer,
                          p_g_12345678901234567890 in integer);
                       procedure short_line (p_a in integer);
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

    }

}