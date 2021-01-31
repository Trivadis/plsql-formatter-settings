package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Test;

public class Issue_57 extends ConfiguredTestFormatter {

    @Test
    public void xmltable_commas_after() {
        final String sql = 
            """
            SELECT stg.payload_type,
                   xt_hdr.*
              FROM stg,
                   XMLTABLE (
                      '/XML/Header'
                      PASSING xmltype.createxml(stg.xml_payload)
                      COLUMNS source        VARCHAR2(50)     PATH 'Source',
                              action_type   VARCHAR2(50)     PATH 'Action_Type',
                              message_type  VARCHAR2(40)     PATH 'Message_Type',
                              company_id    NUMBER           PATH 'Company_ID'
                   ) hdr;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void xmltable_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            SELECT stg.payload_type
                 , xt_hdr.*
              FROM stg
                 , XMLTABLE (
                      '/XML/Header'
                      PASSING xmltype.createxml(stg.xml_payload)
                      COLUMNS source        VARCHAR2(50)     PATH 'Source'
                            , action_type   VARCHAR2(50)     PATH 'Action_Type'
                            , message_type  VARCHAR2(40)     PATH 'Message_Type'
                            , company_id    NUMBER           PATH 'Company_ID'
                   ) hdr;
            """;
        formatAndAssert(sql);
    }

}
