package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_57_xmltable extends ConfiguredTestFormatter {

    @Test
    public void xmltable_commas_after() {
        var sql = """
                select stg.payload_type,
                       xt_hdr.*
                  from stg,
                       xmltable(
                          '/XML/Header'
                          passing xmltype.createxml(stg.xml_payload)
                          columns source       varchar2(50) path 'Source',
                                  action_type  varchar2(50) path 'Action_Type',
                                  message_type varchar2(40) path 'Message_Type',
                                  company_id   number       path 'Company_ID'
                       ) hdr;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void xmltable_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                select stg.payload_type
                     , xt_hdr.*
                  from stg
                     , xmltable(
                          '/XML/Header'
                          passing xmltype.createxml(stg.xml_payload)
                          columns source       varchar2(50) path 'Source'
                                , action_type  varchar2(50) path 'Action_Type'
                                , message_type varchar2(40) path 'Message_Type'
                                , company_id   number       path 'Company_ID'
                       ) hdr;
                """;
        formatAndAssert(sql);
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
    }
}
