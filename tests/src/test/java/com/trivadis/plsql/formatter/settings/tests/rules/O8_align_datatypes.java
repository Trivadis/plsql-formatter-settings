package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O8_align_datatypes extends ConfiguredTestFormatter {

    @Nested
    class True {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().alignTypeDecl, true);
            getFormatter().options.put(getFormatter().alignAssignments, true);
        }

        @Test
        public void variables() throws IOException {
            var input = """
                    declare l_a date;l_bbbbbbb varchar2(30);l_ccc pls_integer;begin null;end;/
                    """;
            var actual = formatter.format(input);
            var expected = """
                    declare
                       l_a       date;
                       l_bbbbbbb varchar2(30);
                       l_ccc     pls_integer;
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void constants() throws IOException {
            var input = """
                    declare co_a date := sysdate;co_bbbbbbb varchar2(30) default 'a';
                    co_ccc pls_integer :=10;begin null;end;/
                    """;
            var actual = formatter.format(input);
            var expected = """
                    declare
                       co_a       date         := sysdate;
                       co_bbbbbbb varchar2(30) default 'a';
                       co_ccc     pls_integer  := 10;
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void records() throws IOException {
            var input = """
                    declare
                       type dept_rec_typ is record (
                          dept_id number(4) not null := 10,
                          dept_name varchar2(30) not null := 'Administration',
                          mgr_id number(6) := 200,
                          loc_id number(4) := 1700
                       );
                       dept_rec dept_rec_typ;
                       co_x integer;
                    begin
                       null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    declare
                       type dept_rec_typ is record (
                          dept_id   number(4) not null    := 10,
                          dept_name varchar2(30) not null := 'Administration',
                          mgr_id    number(6)             := 200,
                          loc_id    number(4)             := 1700
                       );
                       dept_rec dept_rec_typ;
                       co_x     integer;
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void package_body() throws IOException {
            var input = """
                    create or replace package body ABC as
                       co_pkg_name constant varchar2(32) := 'ABC';
                       g_xyz integer;
                    
                       procedure p(
                          p1 in integer,
                          p222 in varchar2
                       ) is
                          l_var1234 integer;
                          l_var2 integer;
                          procedure p2 (
                             p3333 in varchar2,
                             p44 in date
                          ) is
                             l_var1234 integer;
                             l_var12 integer;
                          begin
                             null;
                          end;
                       begin
                          something(p1, p2);
                       exception
                          when others then
                             null;
                       end;
                       
                       function f (
                          p1111 in boolean,
                          p2 in out pls_integer,
                          p333  date
                       ) return varchar2 is
                          l_1 integer default 3;
                          l_2222222 integer default 4;
                       begin
                          return 1;
                       end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package body ABC as
                       co_pkg_name constant varchar2(32) := 'ABC';
                       g_xyz       integer;
                    
                       procedure p(
                          p1   in integer,
                          p222 in varchar2
                       ) is
                          l_var1234 integer;
                          l_var2    integer;
                          procedure p2(
                             p3333 in varchar2,
                             p44   in date
                          ) is
                             l_var1234 integer;
                             l_var12   integer;
                          begin
                             null;
                          end;
                       begin
                          something(p1, p2);
                       exception
                          when others then
                             null;
                       end;
                    
                       function f(
                          p1111 in     boolean,
                          p2    in out pls_integer,
                          p333         date
                       ) return varchar2 is
                          l_1       integer default 3;
                          l_2222222 integer default 4;
                       begin
                          return 1;
                       end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class False {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().alignTypeDecl, false);
            getFormatter().options.put(getFormatter().alignAssignments, false);
        }

        @Test
        public void variables() {
            var sql = """
                    declare
                       l_a date;
                       l_bbbbbbb varchar2(30);
                       l_ccc pls_integer;
                    begin
                       null;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void constants() {
            var sql = """
                    declare
                       co_a date := sysdate;
                       co_bbbbbbb varchar2(30) default 'a';
                       co_ccc pls_integer := 10;
                    begin
                       null;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void records() {
            var sql = """
                    declare
                       type dept_rec_typ is record (
                          dept_id number(4) not null := 10,
                          dept_name varchar2(30) not null := 'Administration',
                          mgr_id number(6) := 200,
                          loc_id number(4) := 1700
                       );
                       dept_rec dept_rec_typ;
                       co_x integer;
                    begin
                       null;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void package_body() {
            var sql = """
                    create or replace package body ABC as
                       co_pkg_name constant varchar2(32) := 'ABC';
                       g_xyz integer;
                    
                       procedure p(
                          p1 in integer,
                          p222 in varchar2
                       ) is
                          l_var1234 integer;
                          l_var2 integer;
                          procedure p2(
                             p3333 in varchar2,
                             p44 in date
                          ) is
                             l_var1234 integer;
                             l_var12 integer;
                          begin
                             null;
                          end;
                       begin
                          something(p1, p2);
                       exception
                          when others then
                             null;
                       end;
                       
                       function f(
                          p1111 in boolean,
                          p2 in out pls_integer,
                          p333 date
                       ) return varchar2 is
                          l_1 integer default 3;
                          l_2222222 integer default 4;
                       begin
                          return 1;
                       end;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }
    }
}
