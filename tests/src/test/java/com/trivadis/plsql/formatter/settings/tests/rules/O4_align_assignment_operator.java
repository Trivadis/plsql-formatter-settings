package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O4_align_assignment_operator extends ConfiguredTestFormatter {

    @Nested
    class True {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().alignAssignments, true);
        }

        @Test
        public void body() throws IOException {
            var input = """
                    begin
                       a1234 := '1';
                       b2 := '2';
                       null;
                       c1234567 := '3';
                       if 1 = 1 then
                          d123 := '4';
                          e2 := '5';
                       else
                          case
                             when 1 = 1 then
                                f1 := '5';
                                g123456 := '6';
                             else
                                h123 := '7';
                                i := '8';
                          end case;
                       end if;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       a1234    := '1';
                       b2       := '2';
                       null;
                       c1234567 := '3';
                       if 1 = 1 then
                          d123 := '4';
                          e2   := '5';
                       else
                          case
                             when 1 = 1 then
                                f1      := '5';
                                g123456 := '6';
                             else
                                h123 := '7';
                                i    := '8';
                          end case;
                       end if;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void declare() throws IOException {
            var input = """
                    declare
                       a1234   varchar2(2) := '1';
                       b1      integer := 2;
                       c123456 varchar2(20) := '3';
                       d1      date default sysdate;
                       e123    pls_integer default 42;
                    begin
                       null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    declare
                       a1234   varchar2(2)  := '1';
                       b1      integer      := 2;
                       c123456 varchar2(20) := '3';
                       d1      date         default sysdate;
                       e123    pls_integer  default 42;
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
                    create package pkg is
                       function f (
                          p1 in varchar2 := '1',
                          p2 in date default sysdate,
                          p3 in integer := 3
                       ) return integer;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create package pkg is
                       function f (
                          p1 in varchar2 := '1',
                          p2 in date     default sysdate,
                          p3 in integer  := 3
                       ) return integer;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void single_line_function() throws IOException {
            var input = """
                    create package pkg is
                       function f (p1 in varchar2 := '1', p2 in date default sysdate, p3 in integer := 3) return integer;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create package pkg is
                       function f (p1 in varchar2 := '1', p2 in date default sysdate, p3 in integer := 3) return integer;
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
            getFormatter().options.put(getFormatter().alignAssignments, false);
        }

        @Test
        public void body() {
            var sql = """
                    begin
                       a1234 := '1';
                       b2 := '2';
                       null;
                       c1234567 := '3';
                       if 1 = 1 then
                          d123 := '4';
                          e2 := '5';
                       else
                          case
                             when 1 = 1 then
                                f1 := '5';
                                g123456 := '6';
                             else
                                h123 := '7';
                                i := '8';
                          end case;
                       end if;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void declare() {
            var sql = """
                    declare
                       a1234   varchar2(2) := '1';
                       b1      integer := 2;
                       c123456 varchar2(20) := '3';
                    begin
                       null;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void function() {
            var sql = """
                    create package pkg is
                       function f (
                          p1 in varchar2 := '1',
                          p2 in date default sysdate,
                          p3 in integer := 3
                       ) return integer;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }
    }
}
