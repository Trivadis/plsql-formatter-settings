package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.Breaks
import org.junit.Test

class Issue_62 extends ConfiguredTestFormatter {
    
    @Test
    def xquery_commas_after() {
        '''
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
        '''.formatAndAssert
    }

    @Test
    def xquery_commas_before() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
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
        '''.formatAndAssert
    }

}
