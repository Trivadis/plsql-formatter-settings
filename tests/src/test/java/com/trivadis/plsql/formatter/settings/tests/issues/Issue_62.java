package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Issue_62 extends ConfiguredTestFormatter {

    @Test
    public void xquery_commas_after() {
        final String sql = 
            """
            create or replace function get_dep_cols (
               in_parse_tree  in  xmltype,
               in_column_pos  in  integer
            ) return xmltype is
               l_result xmltype;
            begin
               select xmlquery(q'{
                            ...
                         }'
                         passing in_parse_tree, in_column_pos as "columnPos"
                         returning content
                      )
                 into l_result
                 from dual;
               return l_result;
            end;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void xquery_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            create or replace function get_dep_cols (
               in_parse_tree  in  xmltype
             , in_column_pos  in  integer
            ) return xmltype is
               l_result xmltype;
            begin
               select xmlquery(q'{
                            ...
                         }'
                         passing in_parse_tree, in_column_pos as "columnPos"
                         returning content
                      )
                 into l_result
                 from dual;
               return l_result;
            end;
            /
            """;
        formatAndAssert(sql);

    }

}
