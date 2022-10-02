package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A9_align_xmltable_columns extends ConfiguredTestFormatter {

    @Test
    public void commas_before() throws IOException {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var input = """
                select stg.payload_type
                ,xt_hdr.*
                from stg
                ,xmltable(
                '/XML/Header'
                passing xmltype.createxml(stg.xml_payload)
                columns    source        varchar2(50)     path 'Source'
                ,action_type   varchar2(50)     path 'Action_Type'
                ,message_type  varchar2(40)     path 'Message_Type'
                ,company_id    number           path 'Company_ID'
                ) hdr;
                """;
        var actual = formatter.format(input);
        var expected = """
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
        assertEquals(expected, actual);
    }

    @Test
    public void commas_after() throws IOException {
        setOption(getFormatter().breaksComma, Format.Breaks.After);
        var input = """
                select stg.payload_type,
                xt_hdr.*
                from stg,
                xmltable(
                '/XML/Header'
                passing xmltype.createxml(stg.xml_payload)
                columns    source        varchar2(50)     path 'Source',
                action_type   varchar2(50)     path 'Action_Type',
                message_type  varchar2(40)     path 'Message_Type',
                company_id    number           path 'Company_ID'
                ) hdr;
                """;
        var actual = formatter.format(input);
        var expected = """
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
        assertEquals(expected, actual);
    }

    @Test
    public void commas_no_break() throws IOException {
        // should look like Format.Breaks.After, force breaks on columns
        setOption(getFormatter().breaksComma, Format.Breaks.None);
        var input = """
                select stg.payload_type, xt_hdr.*
                from stg, xmltable(
                '/XML/Header'
                passing xmltype.createxml(stg.xml_payload)
                columns    source        varchar2(50)     path 'Source',  action_type   varchar2(50)     path 'Action_Type',  message_type  varchar2(40)     path 'Message_Type', company_id    number           path 'Company_ID'
                ) hdr;
                """;
        var actual = formatter.format(input);
        var expected = """
                select stg.payload_type, xt_hdr.*
                  from stg, xmltable(
                          '/XML/Header'
                          passing xmltype.createxml(stg.xml_payload)
                          columns source       varchar2(50) path 'Source',
                                  action_type  varchar2(50) path 'Action_Type',
                                  message_type varchar2(40) path 'Message_Type',
                                  company_id   number       path 'Company_ID'
                       ) hdr;
                """;
        assertEquals(expected, actual);
    }
}
