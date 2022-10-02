package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_139_xmltable extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup_non_trivadis_default_settings() {
        // General
        setOption(getFormatter().kwCase, Format.Case.UPPER);
        setOption(getFormatter().idCase, Format.Case.lower);
        // Alignment
        setOption(getFormatter().alignTabColAliases, true);
        setOption(getFormatter().alignAssignments, false);
        setOption(getFormatter().alignRight, false);
        // Indentation
        setOption("identSpaces", 4);
        // Line Breaks
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        setOption(getFormatter().commasPerLine, 5); // irrelevant
        setOption(getFormatter().breakAnsiiJoin, false);
        setOption(getFormatter().breakParenCondition, false);
        setOption(getFormatter().breakOnSubqueries, false);
        setOption(getFormatter().maxCharLineSize, 150);
        setOption(getFormatter().extraLinesAfterSignificantStatements, Format.BreaksX2.X1);
        setOption("breaksAfterSelect", true);
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
