package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_74 extends ConfiguredTestFormatter {
    private final String input =
        """
        create or replace package body the_api.math as function to_int_table(in_integers
        in varchar2,in_pattern in varchar2 default '[0-9]+')return sys.ora_mining_number_nt deterministic accessible
        by(package the_api.math,package the_api.test_math)is l_result sys
        .ora_mining_number_nt:=sys.ora_mining_number_nt();l_pos integer:= 1;l_int integer;
        begin<<integer_tokens>>loop l_int:=to_number(regexp_substr(in_integers,in_pattern,1,l_pos));
        exit integer_tokens when l_int is null;l_result.extend;l_result(l_pos):= l_int;l_pos:=l_pos+1;
        end loop integer_tokens;return l_result;end to_int_table;end math;
        /
        """;

    private final String expected =
        """
        create or replace package body the_api.math as
           function to_int_table (
              in_integers  in  varchar2,
              in_pattern   in  varchar2 default '[0-9]+'
           ) return sys.ora_mining_number_nt
              deterministic
              accessible by ( package the_api.math, package the_api.test_math )
           is
              l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
              l_pos     integer := 1;
              l_int     integer;
           begin
              <<integer_tokens>>
              loop
                 l_int               := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                 exit integer_tokens when l_int is null;
                 l_result.extend;
                 l_result(l_pos)     := l_int;
                 l_pos               := l_pos + 1;
              end loop integer_tokens;
              return l_result;
           end to_int_table;
        end math;
        /
        """.trim();

    private final String expectedX2 =
        """
        create or replace package body the_api.math as
        
           function to_int_table (
              in_integers  in  varchar2,
              in_pattern   in  varchar2 default '[0-9]+'
           ) return sys.ora_mining_number_nt
              deterministic
              accessible by ( package the_api.math, package the_api.test_math )
           is
              l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
              l_pos     integer := 1;
              l_int     integer;
           begin
              <<integer_tokens>>
              loop
                 l_int               := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                 exit integer_tokens when l_int is null;
                 l_result.extend;
                 l_result(l_pos)     := l_int;
                 l_pos               := l_pos + 1;
              end loop integer_tokens;
        
              return l_result;
           end to_int_table;
        
        end math;
        /
        """.trim();

    @Test
    @Disabled("LightweightFormatter")
    public void min_one_line_break_after_with_breaksx2_keep() throws IOException {
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.Keep);
        String actual = getFormatter().format(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Disabled("LightweightFormatter")
    public void min_one_line_break_after_with_breaksx2_x1() throws IOException {
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X1);
        String actual = getFormatter().format(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Disabled("LightweightFormatter")
    public void min_one_line_break_after_with_breaksx2_x2() throws IOException {
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X2);
        String actual = getFormatter().format(input);
        Assertions.assertEquals(expectedX2, actual);
    }

}
