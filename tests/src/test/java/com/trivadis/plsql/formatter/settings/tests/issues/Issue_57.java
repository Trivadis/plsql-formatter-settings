package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Issue_57 extends ConfiguredTestFormatter {

    @Test
    public void xmltable_commas_after() {
        final String sql = 
            """
            select stg.payload_type,
                   xt_hdr.*
              from stg,
                   xmltable (
                      '/XML/Header'
                      passing xmltype.createxml(stg.xml_payload)
                      columns source        varchar2(50)     path 'Source',
                              action_type   varchar2(50)     path 'Action_Type',
                              message_type  varchar2(40)     path 'Message_Type',
                              company_id    number           path 'Company_ID'
                   ) hdr;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void xmltable_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            select stg.payload_type
                 , xt_hdr.*
              from stg
                 , xmltable (
                      '/XML/Header'
                      passing xmltype.createxml(stg.xml_payload)
                      columns source        varchar2(50)     path 'Source'
                            , action_type   varchar2(50)     path 'Action_Type'
                            , message_type  varchar2(40)     path 'Message_Type'
                            , company_id    number           path 'Company_ID'
                   ) hdr;
            """;
        formatAndAssert(sql);
    }

}
