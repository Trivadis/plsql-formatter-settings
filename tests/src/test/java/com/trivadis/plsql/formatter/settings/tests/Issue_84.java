package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_84 extends ConfiguredTestFormatter {

    @Test
    public void assignment_with_long_dotted_expression() {
        final String sql = 
            """
            BEGIN
               l_some_long_named_variable_rec.some_primary_item_id :=
                  p_input_obj.items(i).some_nested_element_array(j).some_value.some_other_value.some_primary_item_id;
            END;
            /
            """;
        formatAndAssert(sql);
    }

    @Test
    public void assignment_with_ultra_long_dotted_expression() {
        final String sql = 
            """
            BEGIN
               l_some_long_named_variable_rec.some_primary_item_id :=
                  p_input_obj.items(i).some_nested_element_array(j).some_value.some_other_value.some_primary_item_id1.some_primary_item_id2.
                  some_primary_item_id3.some_primary_item_id4.some_primary_item_id5.some_primary_item_id.some_primary_item_id6.some_primary_item_id7;
            END;
            /
            """;
        formatAndAssert(sql);
    }

}
