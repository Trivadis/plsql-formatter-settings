package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_126_create_view_column_list extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put("identSpaces", 2);
        getFormatter().options.put("idCase", Format.Case.lower);
    }

    @Test
    public void dbms_metadata_view() throws IOException {
        getFormatter().options.put(getFormatter().spaceAfterCommas, false);
        var input = """
                  CREATE OR REPLACE FORCE EDITIONABLE VIEW "COVID_COVPN"."DUAL_V" ("DUMMY1", "DUMMY2", "DUMMY3", "DUMMY4", "DUMMY5", "DUMMY6", "DUMMY7", "DUMMY8", "DUMMY9", "DUMMY10", "DUMMY11", "DUMMY12", "DUMMY13", "DUMMY14", "DUMMY15", "DUMMY16", "DUMMY17", "DUMMY18", "DUMMY19", "DUMMY20", "DUMMY21", "DUMMY22", "DUMMY23", "DUMMY24", "DUMMY25", "DUMMY26", "DUMMY27", "DUMMY28", "DUMMY29", "DUMMY30", "DUMMY31", "DUMMY32", "DUMMY33", "DUMMY34", "DUMMY35", "DUMMY36", "DUMMY37", "DUMMY38", "DUMMY39", "DUMMY40", "DUMMY41", "DUMMY42", "DUMMY43", "DUMMY44", "DUMMY45", "DUMMY46", "DUMMY47", "DUMMY48", "DUMMY49", "DUMMY50") AS\s
                  select dummy dummy1,
                         dummy dummy2,
                         dummy dummy3,
                         dummy dummy4,
                         dummy dummy5,
                         dummy dummy6,
                         dummy dummy7,
                         dummy dummy8,
                         dummy dummy9,
                         dummy dummy10,
                         dummy dummy11,
                         dummy dummy12,
                         dummy dummy13,
                         dummy dummy14,
                         dummy dummy15,
                         dummy dummy16,
                         dummy dummy17,
                         dummy dummy18,
                         dummy dummy19,
                         dummy dummy20,
                         dummy dummy21,
                         dummy dummy22,
                         dummy dummy23,
                         dummy dummy24,
                         dummy dummy25,
                         dummy dummy26,
                         dummy dummy27,
                         dummy dummy28,
                         dummy dummy29,
                         dummy dummy30,
                         dummy dummy31,
                         dummy dummy32,
                         dummy dummy33,
                         dummy dummy34,
                         dummy dummy35,
                         dummy dummy36,
                         dummy dummy37,
                         dummy dummy38,
                         dummy dummy39,
                         dummy dummy40,
                         dummy dummy41,
                         dummy dummy42,
                         dummy dummy43,
                         dummy dummy44,
                         dummy dummy45,
                         dummy dummy46,
                         dummy dummy47,
                         dummy dummy48,
                         dummy dummy49,
                         dummy dummy50
                    from dual;
                """;
        var expected = """
                create or replace force editionable view "COVID_COVPN"."DUAL_V" (
                  "DUMMY1",
                  "DUMMY2",
                  "DUMMY3",
                  "DUMMY4",
                  "DUMMY5",
                  "DUMMY6",
                  "DUMMY7",
                  "DUMMY8",
                  "DUMMY9",
                  "DUMMY10",
                  "DUMMY11",
                  "DUMMY12",
                  "DUMMY13",
                  "DUMMY14",
                  "DUMMY15",
                  "DUMMY16",
                  "DUMMY17",
                  "DUMMY18",
                  "DUMMY19",
                  "DUMMY20",
                  "DUMMY21",
                  "DUMMY22",
                  "DUMMY23",
                  "DUMMY24",
                  "DUMMY25",
                  "DUMMY26",
                  "DUMMY27",
                  "DUMMY28",
                  "DUMMY29",
                  "DUMMY30",
                  "DUMMY31",
                  "DUMMY32",
                  "DUMMY33",
                  "DUMMY34",
                  "DUMMY35",
                  "DUMMY36",
                  "DUMMY37",
                  "DUMMY38",
                  "DUMMY39",
                  "DUMMY40",
                  "DUMMY41",
                  "DUMMY42",
                  "DUMMY43",
                  "DUMMY44",
                  "DUMMY45",
                  "DUMMY46",
                  "DUMMY47",
                  "DUMMY48",
                  "DUMMY49",
                  "DUMMY50"
                ) as
                  select dummy dummy1,
                         dummy dummy2,
                         dummy dummy3,
                         dummy dummy4,
                         dummy dummy5,
                         dummy dummy6,
                         dummy dummy7,
                         dummy dummy8,
                         dummy dummy9,
                         dummy dummy10,
                         dummy dummy11,
                         dummy dummy12,
                         dummy dummy13,
                         dummy dummy14,
                         dummy dummy15,
                         dummy dummy16,
                         dummy dummy17,
                         dummy dummy18,
                         dummy dummy19,
                         dummy dummy20,
                         dummy dummy21,
                         dummy dummy22,
                         dummy dummy23,
                         dummy dummy24,
                         dummy dummy25,
                         dummy dummy26,
                         dummy dummy27,
                         dummy dummy28,
                         dummy dummy29,
                         dummy dummy30,
                         dummy dummy31,
                         dummy dummy32,
                         dummy dummy33,
                         dummy dummy34,
                         dummy dummy35,
                         dummy dummy36,
                         dummy dummy37,
                         dummy dummy38,
                         dummy dummy39,
                         dummy dummy40,
                         dummy dummy41,
                         dummy dummy42,
                         dummy dummy43,
                         dummy dummy44,
                         dummy dummy45,
                         dummy dummy46,
                         dummy dummy47,
                         dummy dummy48,
                         dummy dummy49,
                         dummy dummy50
                    from dual;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }
}
