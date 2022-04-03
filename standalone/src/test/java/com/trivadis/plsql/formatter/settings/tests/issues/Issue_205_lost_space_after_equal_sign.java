package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_205_lost_space_after_equal_sign extends ConfiguredTestFormatter {

    @Test
    public void line_break_after_label() {
        var sql = """
                declare
                   co_no_data_found constant integer := -1;
                begin
                   my_package.some_processing(); -- some code which raises an exception
                exception
                   when too_many_rows then
                      my_package.some_further_processing();
                   when others then
                      if sqlcode = co_no_data_found then
                         null;
                      end if;
                end;
                /
                """;
        formatAndAssert(sql);
    }
}
