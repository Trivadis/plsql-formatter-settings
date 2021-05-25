package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.Test;

public class Issue_33_split_arguments extends ConfiguredTestFormatter {

    @Test
    public void split_nested_args_commas_after() {
        var sql = """
                create procedure test_dedup_t_obj is
                   l_input     t_obj_type;
                   l_actual    t_obj_type;
                   l_expected  t_obj_type;
                begin
                   l_input    :=
                      t_obj_type(
                         obj_type('MY_OWNER', 'VIEW', 'MY_VIEW'),
                         obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE'),
                         obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                      );
                   l_expected :=
                      t_obj_type(
                         obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE'),
                         obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                      );
                   l_actual   := type_util.dedup(l_input);
                   ut.expect(l_actual.count).to_equal(2);
                   ut.expect(anydata.convertCollection(l_actual)).to_equal(anydata.convertCollection(l_expected)).unordered;
                end test_dedup_t_obj;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_args_commas_before() {
        getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
        var sql = """
                create procedure test_dedup_t_obj is
                   l_input     t_obj_type;
                   l_actual    t_obj_type;
                   l_expected  t_obj_type;
                begin
                   l_input    :=
                      t_obj_type(
                         obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                       , obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE')
                       , obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                      );
                   l_expected :=
                      t_obj_type(
                         obj_type('MY_OWNER', 'PACKAGE', 'MY_PACKAGE')
                       , obj_type('MY_OWNER', 'VIEW', 'MY_VIEW')
                      );
                   l_actual   := type_util.dedup(l_input);
                   ut.expect(l_actual.count).to_equal(2);
                   ut.expect(anydata.convertCollection(l_actual)).to_equal(anydata.convertCollection(l_expected)).unordered;
                end test_dedup_t_obj;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_functions() {
        var sql = """
                select some_quite_long_function_name(
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
                  from t;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_nested_functions() {
        var sql = """
                select some_quite_long_function_name(
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
                  from t;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_functions_with_named_parameters() {
        var sql = """
                select some_quite_long_function_name(
                          another_long_function_name(
                             a => first_Column_id
                                  || another_Column_id
                                  || third_Column_id
                                  || fourth_column_id
                                  || fifth_column
                                  || another_column,
                             b => 'another parameter'
                          )
                       )
                  from t;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_functions_with_named_parameters_only() {
        var sql = """
                select some_quite_long_function_name(
                          a => another_long_function_name(
                                  b => first_Column_id
                                       || another_Column_id
                                       || third_Column_id
                                       || fourth_column_id
                                       || fifth_column
                                       || another_column,
                                  c => 'another parameter'
                               )
                       )
                  from t;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void split_nested_nested_functions_with_named_parameters() {
        var sql = """
                select some_quite_long_function_name(
                          another_long_function_name(
                             yet_another_long_function_name(
                                a => first_Column_id
                                     || another_Column_id
                                     || third_Column_id
                                     || fourth_column_id
                                     || fifth_column
                                     || another_column,
                                b => 'another parameter'
                             )
                          )
                       )
                  from t;
                """;
        formatAndAssert(sql);
    }
}
