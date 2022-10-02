package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class Issue_167_leading_commas_and_comments extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @AfterEach
    public void teardown() {
        resetOptions();
    }

    @Test
    public void leading_commas() {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                create or replace package test_pkg authid definer as
                                
                   procedure proc(
                      p_segment1 in         varchar2
                    -- comment 1
                    , p_segment2 in         varchar2
                    , p_segment3 in         varchar2
                    /* comment 2 */
                    , x_status   out nocopy varchar2
                   );
                                
                end test_pkg;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void trailing_commas() {
        setOption(getFormatter().breaksComma, Format.Breaks.After);
        var sql = """
                create or replace package test_pkg authid definer as
                                
                   procedure proc(
                      p_segment1 in         varchar2,
                      -- comment 1
                      p_segment2 in         varchar2,
                      p_segment3 in         varchar2,
                      /* comment 2 */
                      x_status   out nocopy varchar2
                   );
                                
                end test_pkg;
                """;
        formatAndAssert(sql);
    }
}
