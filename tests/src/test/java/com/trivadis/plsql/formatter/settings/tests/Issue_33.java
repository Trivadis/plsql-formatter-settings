package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.Test;

public class Issue_33 extends ConfiguredTestFormatter {

    @Test
    public void split_nested_args_commas_after() {
        final String sql = 
            """
            CREATE PROCEDURE test_dedup_t_obj IS
               l_input     t_obj_type;
               l_actual    t_obj_type;
               l_expected  t_obj_type;
            BEGIN
               l_input     :=
                  t_obj_type(
                     obj_type('MY_OWNER', 'VIEW', 'MY_VIEW'),
                     obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE'),
                     obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                  );
               l_expected  :=
                  t_obj_type(
                     obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE'),
                     obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                  );
               l_actual    := type_util.dedup(l_input);
               ut.expect(l_actual.count).to_equal(2);
               ut.expect(anydata.convertCollection(l_actual)).to_equal(anydata.convertCollection(l_expected)).unordered;
            END test_dedup_t_obj;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_args_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        final String sql = 
            """
            CREATE PROCEDURE test_dedup_t_obj IS
               l_input     t_obj_type;
               l_actual    t_obj_type;
               l_expected  t_obj_type;
            BEGIN
               l_input     :=
                  t_obj_type(
                     obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                   , obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE')
                   , obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                  );
               l_expected  :=
                  t_obj_type(
                     obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE')
                   , obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                  );
               l_actual    := type_util.dedup(l_input);
               ut.expect(l_actual.count).to_equal(2);
               ut.expect(anydata.convertCollection(l_actual)).to_equal(anydata.convertCollection(l_expected)).unordered;
            END test_dedup_t_obj;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_functions() {
        final String sql = 
            """
            SELECT some_quite_long_function_name(
                      another_long_function_name(
                         first_Column_id
                         || another_Column_id
                         || third_Column_id
                         || fourth_column_id
                         || fifth_column
                         || another_column,
                         'another parameter'
                      )
                   )
              FROM t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_nested_functions() {
        final String sql = 
            """
            SELECT some_quite_long_function_name(
                      another_long_function_name(
                         yet_another_long_function_name(
                            first_Column_id
                            || another_Column_id
                            || third_Column_id
                            || fourth_column_id
                            || fifth_column
                            || another_column,
                            'another parameter'
                         )
                      )
                   )
              FROM t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_functions_with_named_parameters() {
        final String sql = 
            """
            SELECT some_quite_long_function_name(
                      another_long_function_name(
                         a  => first_Column_id
                              || another_Column_id
                              || third_Column_id
                              || fourth_column_id
                              || fifth_column
                              || another_column,
                         b  => 'another parameter'
                      )
                   )
              FROM t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_functions_with_named_parameters_only() {
        final String sql = 
            """
            SELECT some_quite_long_function_name(
                      a => another_long_function_name(
                              b  => first_Column_id
                                   || another_Column_id
                                   || third_Column_id
                                   || fourth_column_id
                                   || fifth_column
                                   || another_column,
                              c  => 'another parameter'
                           )
                   )
              FROM t;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_nested_functions_with_named_parameters() {
        final String sql = 
            """
            SELECT some_quite_long_function_name(
                      another_long_function_name(
                         yet_another_long_function_name(
                            a  => first_Column_id
                                 || another_Column_id
                                 || third_Column_id
                                 || fourth_column_id
                                 || fifth_column
                                 || another_column,
                            b  => 'another parameter'
                         )
                      )
                   )
              FROM t;
            """;
        formatAndAssert(sql);

    }

}
