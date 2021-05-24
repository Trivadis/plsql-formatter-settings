package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Select_list extends ConfiguredTestFormatter {

    @Test
    public void select_list_comma_after() {
        var sql = """
                select case a
                          when 1  then
                             'one'
                          when 2  then
                             'two'
                          else
                             'else'
                       end a2,
                       b,
                       c,
                       to_char(d, 'yyyy-mm') as d2,
                       case
                          when a = 1  then
                             'one'
                          when a = 2  then
                             'two'
                          else
                             'else'
                       end a3
                  from t2;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void select_list_comma_before() {
        formatter.options.put(formatter.breaksComma, Format.Breaks.Before);
        var sql = """
                select case a
                          when 1  then
                             'one'
                          when 2  then
                             'two'
                          else
                             'else'
                       end a2
                     , b
                     , c
                     , to_char(d, 'yyyy-mm') as d2
                     , case
                          when a = 1  then
                             'one'
                          when a = 2  then
                             'two'
                          else
                             'else'
                       end a3
                  from t2;
                """;
        formatAndAssert(sql, true);
    }
}
