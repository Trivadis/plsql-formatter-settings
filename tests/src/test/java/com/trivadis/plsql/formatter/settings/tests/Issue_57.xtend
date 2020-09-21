package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format.Breaks
import org.junit.Test

class Issue_57 extends ConfiguredTestFormatter {
    
    @Test
    def xmltable_commas_after() {
        '''
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
        '''.formatAndAssert
    }

    @Test
    def xmltable_commas_before() {
        formatter.options.put(formatter.breaksComma, Breaks.Before);
        '''
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
        '''.formatAndAssert
    }

}
