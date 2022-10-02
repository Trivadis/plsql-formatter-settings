package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A20_align_json_table_columns extends ConfiguredTestFormatter {

    @Test
    public void commas_before() throws IOException {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var input = """
                select jt.*
                  from j_purchaseorder
                       ,json_table(
                       po_document, '$.ShippingInstructions.Phone[*]'
                       columns(row_number for ordinality
                       ,phone_type varchar2(10) path '$.type'
                       ,phone_num varchar2(200) path '$.number')
                       ) as jt;
                """;
        var actual = formatter.format(input);
        var expected = """
                select jt.*
                  from j_purchaseorder
                     , json_table(
                          po_document, '$.ShippingInstructions.Phone[*]'
                          columns(row_number for ordinality
                                , phone_type varchar2(10)  path '$.type'
                                , phone_num  varchar2(200) path '$.number')
                       ) as jt;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void commas_after() throws IOException {
        setOption(getFormatter().breaksComma, Format.Breaks.After);
        var input = """
                select jt.*
                  from j_purchaseorder,
                       json_table(
                       po_document, '$.ShippingInstructions.Phone[*]'
                       columns (row_number for ordinality,
                       phone_type varchar2(10) path '$.type',
                       phone_num varchar2(200) path '$.number')
                       ) as jt;
                """;
        var actual = formatter.format(input);
        var expected = """
                select jt.*
                  from j_purchaseorder,
                       json_table(
                          po_document, '$.ShippingInstructions.Phone[*]'
                          columns (row_number for ordinality,
                                   phone_type varchar2(10)  path '$.type',
                                   phone_num  varchar2(200) path '$.number')
                       ) as jt;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void commas_after_break_after_open_paren() throws IOException {
        setOption(getFormatter().breaksComma, Format.Breaks.After);
        var input = """
                select jt.*
                  from j_purchaseorder,
                       json_table(
                       po_document, '$.ShippingInstructions.Phone[*]'
                       columns(
                       row_number for ordinality,
                       phone_type varchar2(10) path '$.type',
                       phone_num varchar2(200) path '$.number'
                       )
                       ) as jt;
                """;
        var actual = formatter.format(input);
        var expected = """
                select jt.*
                  from j_purchaseorder,
                       json_table(
                          po_document, '$.ShippingInstructions.Phone[*]'
                          columns(
                             row_number for ordinality,
                             phone_type varchar2(10)  path '$.type',
                             phone_num  varchar2(200) path '$.number'
                          )
                       ) as jt;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void commas_no_break() throws IOException {
        // should look like Format.Breaks.After, force breaks on columns
        setOption(getFormatter().breaksComma, Format.Breaks.None);
        var input = """
                select jt.*
                  from j_purchaseorder,
                       json_table(
                       po_document, '$.ShippingInstructions.Phone[*]'
                       columns (row_number for ordinality,phone_type varchar2(10) path '$.type',phone_num varchar2(200) path '$.number')
                       ) as jt;
                """;
        var actual = formatter.format(input);
        var expected = """
                select jt.*
                  from j_purchaseorder,
                       json_table(
                          po_document, '$.ShippingInstructions.Phone[*]'
                          columns (row_number for ordinality,
                                   phone_type varchar2(10)  path '$.type',
                                   phone_num  varchar2(200) path '$.number')
                       ) as jt;
                """;
        assertEquals(expected, actual);
    }
}
