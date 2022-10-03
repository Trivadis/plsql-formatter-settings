package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import oracle.dbtools.parser.plsql.SyntaxError;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class I11_enforce_unquoted_identifiers extends ConfiguredTestFormatter {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ReplaceQuotedIdentifiers {

        @BeforeAll
        public void setup() {
            setOption("keepQuotedIdentifiers", false);
            setOption(getFormatter().idCase, Format.Case.lower);
        }

        @Test
        public void unquoting_allowed() throws IOException {
            var input = """
                    select "DUMMY" from "SYS"."DUAL";
                    """;
            var expected = """
                    select dummy from sys.dual;
                    """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void all_allowed_chars() throws IOException {
            var input = """
                    select "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_$#" from "T";
                    """;
            var expected = """
                    select abcdefghijklmnopqrstuvwxyz0123456789_$# from t;
                    """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void skip_formatter_off_on_sections() throws IOException {
            var input = """
                    select "DUMMY" -- @formatter:off
                      from "SYS"."DUAL" -- @formatter:on
                     where "DUMMY" is not null;
                    """;
            var expected = """
                    select dummy -- @formatter:off
                      from "SYS"."DUAL" -- @formatter:on
                     where dummy is not null;
                    """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void skip_conditional_compilation_blocks() throws IOException {
            var input = """
                create or replace package body pkg is
                   $IF FALSE $THEN
                      procedure p1 is
                         l_dummy "SYS"."DUAL"."DUMMY"%type;
                      begin
                         select "DUMMY"
                           into l_dummy
                           from "SYS"."DUAL";
                      end p1;
                   $END
                
                   procedure p2 is
                      l_dummy "SYS"."DUAL"."DUMMY"%type;
                   begin
                      select "DUMMY"
                        into l_dummy
                        from "SYS"."DUAL";
                   end p2;
                end;
                /
                """;
            var expected = """
                create or replace package body pkg is
                   $IF FALSE $THEN
                      procedure p1 is
                         l_dummy "SYS"."DUAL"."DUMMY"%type;
                      begin
                         select "DUMMY"
                           into l_dummy
                           from "SYS"."DUAL";
                      end p1;
                   $END
                
                   procedure p2 is
                      l_dummy sys.dual.dummy%type;
                   begin
                      select dummy
                        into l_dummy
                        from sys.dual;
                   end p2;
                end;
                /
                """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void skip_keywords() throws IOException {
            var input = """
                    create table "TABLE" (
                       "COLUMN" integer primary key,
                       "FROM"   date,
                       "TO"     date,
                       "VALUE"  varchar2(50 char)
                    );
                                        
                    select "COLUMN", "FROM", "TO", "VALUE"
                      from "TABLE"
                     where "COLUMN" = 1;
                    """;
            var expected = """
                    create table "TABLE" (
                       "COLUMN" integer primary key,
                       "FROM"   date,
                       "TO"     date,
                       value  varchar2(50 char)
                    );
                                        
                    select "COLUMN", "FROM", "TO", value
                      from "TABLE"
                     where "COLUMN" = 1;
                    """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void skip_comments() throws IOException {
            var input = """
                    begin
                       -- select "DUMMY" from "SYS"."DUAL";
                       /*
                          select "DUMMY" from "SYS"."DUAL";
                       */
                       select "DUMMY" from "SYS"."DUAL";
                    end;
                    /
                    """;
            var expected = """
                    begin
                       -- select "DUMMY" from "SYS"."DUAL";
                       /*
                          select "DUMMY" from "SYS"."DUAL";
                       */
                       select dummy from sys.dual;
                    end;
                    /
                    """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void skip_when_quoting_is_necessary() throws IOException {
            var input = """
                    begin
                       -- lowercase
                       select "value" from "keystore";
                       -- spaces
                       select "MY VALUE" from "MY TABLE";
                       -- unsupported characters
                       select "VALUE£" from "MONEY_IN_£";
                       -- first char is not a letter
                       select "$1" from "DOLLARS$";
                    end;
                    /
                    """;
            var expected = """
                    begin
                       -- lowercase
                       select "value" from "keystore";
                       -- spaces
                       select "MY VALUE" from "MY TABLE";
                       -- unsupported characters
                       select "VALUE£" from "MONEY_IN_£";
                       -- first char is not a letter
                       select "$1" from dollars$;
                    end;
                    /
                    """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void skip_java_strings() {
            // works only with "setOption(getFormatter().formatWhenSyntaxError, true);"
            // setting this option to false will most probably break the Java code to be formatted.
            // it's a good example that with syntax errors must not be formatted.
            var input = """
                    create or replace and resolve java source named "Welcome" as
                    public class Welcome {
                       public static String greet(String name) {
                          return "WELCOME" + " " + name + "!";
                       }
                    }
                    /
                                        
                    create or replace function greet(in_name in varchar2) return varchar2
                    as language java
                    name 'Welcome.greet(java.lang.String) return java.lang.String';
                    /
                                        
                    select greet('Scott') from dual;
                    """;
            SyntaxError thrown = Assertions.assertThrows (SyntaxError.class, () -> getFormatter().format(input), "Expected syntax error.");
            Assertions.assertTrue(thrown.getMessage().contains("Syntax Error at line 4, column 23"));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class KeepQuotedIdentifiers {

        @BeforeAll
        public void setup() {
            setOption("keepQuotedIdentifiers", true);
            setOption(getFormatter().idCase, Format.Case.lower);
        }

        @Test
        public void unquoting_allowed() throws IOException {
            var input = """
                    select "DUMMY" from "SYS"."DUAL";
                    """;
            var expected = """
                    select "DUMMY" from "SYS"."DUAL";
                    """.trim();
            var actual = getFormatter().format(input);
            Assertions.assertEquals(expected, actual);
        }
    }
}
