package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Issue_167_leading_commas_and_comments extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup_non_trivadis_default_settings() {
        getFormatter().options.put(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void leading_commas() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
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
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
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
