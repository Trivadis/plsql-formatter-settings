package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
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
            var actual = formatter.format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        public void all_allowed_chars() throws IOException {
            var input = """
                    select "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_$#ÄÖÜ" from "T";
                    """;
            var expected = """
                    select abcdefghijklmnopqrstuvwxyz0123456789_$#äöü from t;
                    """.trim();
            var actual = formatter.format(input);
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
            var actual = formatter.format(input);
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
            var actual = formatter.format(input);
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
            var actual = formatter.format(input);
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
            var actual = formatter.format(input);
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
            var actual = formatter.format(input);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        @Disabled("Java source leads to parse error. Formatter will ignore them by default.")
        public void skip_java_strings() throws IOException {
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
            var expected = """
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
                    """.trim();
            var actual = formatter.format(input);
            Assertions.assertEquals(expected, actual);
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
            var actual = formatter.format(input);
            Assertions.assertEquals(expected, actual);
        }
    }
}
