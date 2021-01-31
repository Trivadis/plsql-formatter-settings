package com.trivadis.plsql.formatter.settings.tests;

import org.junit.Test;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;

import oracle.dbtools.app.Format;

public class Select_list extends ConfiguredTestFormatter {

    @Test
    public void select_list_comma_after() {
        final String sql = 
            """
            SELECT CASE a
                      WHEN 1  THEN
                         'one'
                      WHEN 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a2,
                   b,
                   c,
                   to_char(d, 'YYYY-MM') AS d2,
                   CASE
                      WHEN a = 1  THEN
                         'one'
                      WHEN a = 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a3
              FROM t2;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void select_list_comma_before() {
        formatter.options.put(formatter.breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            SELECT CASE a
                      WHEN 1  THEN
                         'one'
                      WHEN 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a2
                 , b
                 , c
                 , to_char(d, 'YYYY-MM') AS d2
                 , CASE
                      WHEN a = 1  THEN
                         'one'
                      WHEN a = 2  THEN
                         'two'
                      ELSE
                         'else'
                   END a3
              FROM t2;
            """;

        formatAndAssert(sql, true);
    }

}
