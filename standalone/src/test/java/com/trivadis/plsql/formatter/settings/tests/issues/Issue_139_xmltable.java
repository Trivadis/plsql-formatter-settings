package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_139_xmltable extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup_non_trivadis_default_settings() {
        // General
        getFormatter().options.put(getFormatter().kwCase, Format.Case.UPPER);
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
        // Alignment
        getFormatter().options.put(getFormatter().alignTabColAliases, true);
        getFormatter().options.put(getFormatter().alignAssignments, false);
        getFormatter().options.put(getFormatter().alignRight, false);
        // Indentation
        getFormatter().options.put("identSpaces", 4);
        // Line Breaks
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        getFormatter().options.put(getFormatter().commasPerLine, 5); // irrelevant
        getFormatter().options.put(getFormatter().breakAnsiiJoin, false);
        getFormatter().options.put(getFormatter().breakParenCondition, false);
        getFormatter().options.put(getFormatter().breakOnSubqueries, false);
        getFormatter().options.put(getFormatter().maxCharLineSize, 150);
        getFormatter().options.put(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X1);
        getFormatter().options.put("breaksAfterSelect", true);
    }

    @Test
    public void first_formatter_call() throws IOException {
        var input = """
                SELECT
                    *
                FROM
                    XMLTABLE
                    (XMLNAMESPACES(DEFAULT 'urn:xxx'), '/xxxs/xxx/lines/line'
                        PASSING xmltype(p_xml_file)  COLUMNS
                        source_system VARCHAR2(50)PATH './../../source_system'
                      , item_no       VARCHAR(50) PATH 'item_no')lin
                ,XMLTABLE (XMLNAMESPACES(DEFAULT 'urn:xxx'), '/xxxs/xxx/lines/line/lots/lot'
                PASSING xmltype(p_xml_file)
                COLUMNS
                source_system VARCHAR2(50)PATH './../../../../source_system'
                ,item_no         VARCHAR(50) PATH './../../item_no'
                ,quantity        NUMBER      PATH 'quantity'
                ,uom             VARCHAR(50) PATH 'uom')lot
                WHERE
                lot.source_system = lin.source_system
                AND lot.item_no = lin.item_no;
                """;
        var expected = """
                SELECT
                    *
                FROM
                    XMLTABLE
                    (XMLNAMESPACES(DEFAULT 'urn:xxx'), '/xxxs/xxx/lines/line'
                        PASSING xmltype(p_xml_file) COLUMNS
                        source_system VARCHAR2(50)PATH './../../source_system'
                      , item_no       VARCHAR(50) PATH 'item_no') lin
                  , XMLTABLE (XMLNAMESPACES(DEFAULT 'urn:xxx'), '/xxxs/xxx/lines/line/lots/lot'
                        PASSING xmltype(p_xml_file)
                        COLUMNS
                        source_system VARCHAR2(50)PATH './../../../../source_system'
                      , item_no       VARCHAR(50) PATH './../../item_no'
                      , quantity      NUMBER      PATH 'quantity'
                      , uom           VARCHAR(50) PATH 'uom')     lot
                WHERE
                    lot.source_system = lin.source_system
                    AND lot.item_no = lin.item_no;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void keep_formatter_result() throws IOException {
        var input = """
                SELECT
                    *
                FROM
                    XMLTABLE
                    (XMLNAMESPACES(DEFAULT 'urn:xxx'), '/xxxs/xxx/lines/line'
                        PASSING xmltype(p_xml_file)  COLUMNS
                        source_system VARCHAR2(50)PATH './../../source_system'
                      , item_no       VARCHAR(50) PATH 'item_no')lin
                ,XMLTABLE (XMLNAMESPACES(DEFAULT 'urn:xxx'), '/xxxs/xxx/lines/line/lots/lot'
                PASSING xmltype(p_xml_file)
                COLUMNS
                source_system VARCHAR2(50)PATH './../../../../source_system'
                ,item_no         VARCHAR(50) PATH './../../item_no'
                ,quantity        NUMBER      PATH 'quantity'
                ,uom             VARCHAR(50) PATH 'uom')lot
                WHERE
                lot.source_system = lin.source_system
                AND lot.item_no = lin.item_no;
                """;
        // initial formatter call
        var expected = formatter.format(input);
        // second formatter call
        var actual = formatter.format(expected);
        assertEquals(expected, actual);
        // third formatter call
        actual = formatter.format(expected);
        assertEquals(expected, actual);
    }
}
