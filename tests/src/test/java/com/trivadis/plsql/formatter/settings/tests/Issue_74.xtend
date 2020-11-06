package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.BreaksX2
import org.junit.Assert
import org.junit.Test

class Issue_74 extends ConfiguredTestFormatter {
    var input = '''
        create or replace package body the_api.math as function to_int_table(in_integers
        in varchar2,in_pattern in varchar2 default '[0-9]+')return sys.ora_mining_number_nt deterministic accessible
        by(package the_api.math,package the_api.test_math)is l_result sys
        .ora_mining_number_nt:=sys.ora_mining_number_nt();l_pos integer:= 1;l_int integer;
        begin<<integer_tokens>>loop l_int:=to_number(regexp_substr(in_integers,in_pattern,1,l_pos));
        exit integer_tokens when l_int is null;l_result.extend;l_result(l_pos):= l_int;l_pos:=l_pos+1;
        end loop integer_tokens;return l_result;end to_int_table;end math;
        /
    '''
    var expected = '''
        CREATE OR REPLACE PACKAGE BODY the_api.math AS
           FUNCTION to_int_table (
              in_integers  IN  VARCHAR2,
              in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
           ) RETURN sys.ora_mining_number_nt
              DETERMINISTIC
              ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
           IS
              l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
              l_pos     INTEGER := 1;
              l_int     INTEGER;
           BEGIN
              <<integer_tokens>>
              LOOP
                 l_int               := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                 EXIT integer_tokens WHEN l_int IS NULL;
                 l_result.extend;
                 l_result(l_pos)     := l_int;
                 l_pos               := l_pos + 1;
              END LOOP integer_tokens;
              RETURN l_result;
           END to_int_table;
        END math;
        /
    '''.toString().trim()
    var expectedX2 = '''
        CREATE OR REPLACE PACKAGE BODY the_api.math AS
        
           FUNCTION to_int_table (
              in_integers  IN  VARCHAR2,
              in_pattern   IN  VARCHAR2 DEFAULT '[0-9]+'
           ) RETURN sys.ora_mining_number_nt
              DETERMINISTIC
              ACCESSIBLE BY ( PACKAGE the_api.math, PACKAGE the_api.test_math )
           IS
              l_result  sys.ora_mining_number_nt := sys.ora_mining_number_nt();
              l_pos     INTEGER := 1;
              l_int     INTEGER;
           BEGIN
              <<integer_tokens>>
              LOOP
                 l_int               := to_number(regexp_substr(in_integers, in_pattern, 1, l_pos));
                 EXIT integer_tokens WHEN l_int IS NULL;
                 l_result.extend;
                 l_result(l_pos)     := l_int;
                 l_pos               := l_pos + 1;
              END LOOP integer_tokens;
        
              RETURN l_result;
           END to_int_table;
        
        END math;
        /
    '''.toString().trim()

    @Test
    def min_one_line_break_after_with_breaksx2_keep() {
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, BreaksX2.Keep); // default
        var actual = formatter.format(input)
        Assert.assertEquals(expected, actual)
    }

    @Test
    def min_one_line_break_after_with_breaksx2_x1() {
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, BreaksX2.X1);
        var actual = formatter.format(input)
        Assert.assertEquals(expected, actual)
    }

    @Test
    def min_one_line_break_after_with_breaksx2_x2() {
        formatter.options.put(formatter.extraLinesAfterSignificantStatements, BreaksX2.X2);
        var actual = formatter.format(input)
        Assert.assertEquals(expectedX2, actual)
    }

}
