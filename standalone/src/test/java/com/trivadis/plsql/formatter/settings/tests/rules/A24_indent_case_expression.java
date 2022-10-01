package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class A24_indent_case_expression extends ConfiguredTestFormatter {

    @Test
    public void order_by_case_expr() {
        var sql = """
                select o.owner,
                       o.object_type,
                       o.object_name
                  from dba_objects o
                 where o.owner = coalesce(in_obj.owner, in_parse_user, 'PUBLIC')
                   and o.object_name = in_obj.object_name
                 order by case o.owner
                             when in_obj.owner then
                                1
                             when in_parse_user then
                                2
                             else
                                3
                          end,
                       case o.object_type
                          when in_obj.object_type then
                             1
                          when 'SYNONYM' then
                             3
                          else
                             2
                       end;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void order_by_case_expr_in_parenthesis() {
        var sql = """
                select o.owner,
                       o.object_type,
                       o.object_name
                  from dba_objects o
                 where o.owner = coalesce(in_obj.owner, in_parse_user, 'PUBLIC')
                   and o.object_name = in_obj.object_name
                 order by (
                       case o.owner
                          when in_obj.owner then
                             1
                          when in_parse_user then
                             2
                          else
                             3
                       end),
                       case o.object_type
                          when in_obj.object_type then
                             1
                          when 'SYNONYM' then
                             3
                          else
                             2
                       end;
                """;
        formatAndAssert(sql);
    }
}
