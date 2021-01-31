package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Test;

public class Issue_62 extends ConfiguredTestFormatter {

    @Test
    public void xquery_commas_after() {
        final String sql = 
            """
            CREATE OR REPLACE FUNCTION get_dep_cols (
               in_parse_tree  IN  XMLTYPE,
               in_column_pos  IN  INTEGER
            ) RETURN XMLTYPE IS
               l_result XMLTYPE;
            BEGIN
               SELECT XMLQUERY(q'{
                            ...
                         }'
                         PASSING in_parse_tree, in_column_pos AS "columnPos"
                         RETURNING CONTENT
                      )
                 INTO l_result
                 FROM dual;
               RETURN l_result;
            END;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void xquery_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            CREATE OR REPLACE FUNCTION get_dep_cols (
               in_parse_tree  IN  XMLTYPE
             , in_column_pos  IN  INTEGER
            ) RETURN XMLTYPE IS
               l_result XMLTYPE;
            BEGIN
               SELECT XMLQUERY(q'{
                            ...
                         }'
                         PASSING in_parse_tree, in_column_pos AS "columnPos"
                         RETURNING CONTENT
                      )
                 INTO l_result
                 FROM dual;
               RETURN l_result;
            END;
            /
            """;
        formatAndAssert(sql);

    }

}
