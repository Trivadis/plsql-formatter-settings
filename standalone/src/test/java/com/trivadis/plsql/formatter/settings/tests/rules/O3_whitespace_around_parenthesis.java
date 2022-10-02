package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class O3_whitespace_around_parenthesis extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().breaksAroundLogicalConjunctions, Format.Breaks.None);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Default {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().spaceAroundBrackets, Format.Space.Default);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count (*) from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in (1, 2, 3, 4, 5) and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in (
                              1, 2, 3, 4, 5
                           ) and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void function_definition() throws IOException {
            var input = """
                    create or replace function f  (
                       p1 in integer
                    ) return integer is
                    begin
                       return 1;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace function f(
                       p1 in integer
                    ) return integer is
                    begin
                       return 1;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void package_function_declaration() throws IOException {
            var input = """
                    create or replace package pkg is
                       function f   (
                          p1 in integer
                       ) return integer;
                    end;
                    /
                                        
                    create or replace package body pkg is
                       function f   (
                          p1 in integer
                       ) return integer;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package pkg is
                       function f(
                          p1 in integer
                       ) return integer;
                    end;
                    /
                                        
                    create or replace package body pkg is
                       function f(
                          p1 in integer
                       ) return integer;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void package_function_defintion() throws IOException {
            var input = """
                    create or replace package body pkg is
                       function f  (
                          p1 in integer
                       ) return integer is
                       begin
                          return 1;
                       end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package body pkg is
                       function f(
                          p1 in integer
                       ) return integer is
                       begin
                          return 1;
                       end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void procedure_definition() throws IOException {
            var input = """
                    create or replace procedure p   (
                       p1 in integer
                    ) is
                    begin
                       null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace procedure p(
                       p1 in integer
                    ) is
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void package_procedure_declaration() throws IOException {
            var input = """
                    create or replace package pkg is
                       procedure p (
                          p1 in integer
                       );
                    end;
                    /
                                        
                    create or replace package body pkg is
                       procedure p (
                          p1 in integer
                       );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package pkg is
                       procedure p(
                          p1 in integer
                       );
                    end;
                    /
                                        
                    create or replace package body pkg is
                       procedure p(
                          p1 in integer
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void package_procedure_definition() throws IOException {
            var input = """
                    create or replace package body pkg is
                       procedure p (
                          p1 in integer
                       ) is
                       begin
                          null;
                       end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create or replace package body pkg is
                       procedure p(
                          p1 in integer
                       ) is
                       begin
                          null;
                       end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void function_call() throws IOException {
            var input = """
                    select f (
                              'a1234567890',
                              'b1234567890',
                              'c1234567890',
                              'd1234567890',
                              'e1234567890'
                           )
                      from dual;
                                        
                    begin
                       l_result := f (
                                      'a1234567890',
                                      'b1234567890',
                                      'c1234567890',
                                      'd1234567890',
                                      'e1234567890'
                                   );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select f(
                              'a1234567890',
                              'b1234567890',
                              'c1234567890',
                              'd1234567890',
                              'e1234567890'
                           )
                      from dual;
                                        
                    begin
                       l_result := f(
                                      'a1234567890',
                                      'b1234567890',
                                      'c1234567890',
                                      'd1234567890',
                                      'e1234567890'
                                   );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void procedure_call() throws IOException {
            var input = """
                    begin
                       p (
                          'a1234567890',
                          'b1234567890',
                          'c1234567890',
                          'd1234567890',
                          'e1234567890'
                       );
                       p2 (
                          a => 1,
                          b => 2
                       );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       p(
                          'a1234567890',
                          'b1234567890',
                          'c1234567890',
                          'd1234567890',
                          'e1234567890'
                       );
                       p2(
                          a => 1,
                          b => 2
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void cursor_declaration() throws IOException {
            var input = """
                    declare
                       cursor c1 (
                          p1 in varchar2
                       ) is
                          select col1
                            from t
                           where col2 = p1;
                    begin
                       null;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    declare
                       cursor c1(
                          p1 in varchar2
                       ) is
                          select col1
                            from t
                           where col2 = p1;
                    begin
                       null;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void cursor_usage() throws IOException {
            var input = """
                    begin
                       open c2 (
                          'a1234567890',
                          'b1234567890',
                          'c1234567890',
                          'd1234567890',
                          'e1234567890'
                       );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       open c2(
                          'a1234567890',
                          'b1234567890',
                          'c1234567890',
                          'd1234567890',
                          'e1234567890'
                       );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Inside {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().spaceAroundBrackets, Format.Space.Inside);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count( * )from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in( 1, 2, 3, 4, 5 )and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in(
                              1, 2, 3, 4, 5
                           )and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Outside {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().spaceAroundBrackets, Format.Space.Outside);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count (*) from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in (1, 2, 3, 4, 5) and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in (
                              1, 2, 3, 4, 5
                           ) and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class NoSpace {

        @BeforeAll
        public void setup() {
            setOption(getFormatter().spaceAroundBrackets, Format.Space.NoSpace);
        }

        @Test
        public void count() throws IOException {
            var input = """
                    select count  (   *   )  from dual;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select count(*)from dual;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_single_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (  1, 2, 3, 4, 5  )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in(1, 2, 3, 4, 5)and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void in_multi_line() throws IOException {
            var input = """
                    select *
                      from t
                     where c1 in  (
                              1, 2, 3, 4, 5
                           )  and 1 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select *
                      from t
                     where c1 in(
                              1, 2, 3, 4, 5
                           )and 1 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }
}
