package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_265_wrong_indent_in_insert extends ConfiguredTestFormatter {
    @Test
    public void insert_from_subquery() {
        var input = """
                insert into t1
                select object_type, count(*)
                  from (
                          select 'table' as object_type, table_name as object_name
                            from user_tables
                          union all
                          select 'view', view_name
                            from user_views
                          union all
                          select 'sequence', sequence_name
                            from user_sequences
                       )
                 group by object_type;
                """;
        formatAndAssert(input);
    }

    @Test
    public void insert_from_with_clause() {
        var input = """
                insert into t2
                with
                   objects as (
                      select 'table' as object_type, table_name as object_name
                        from user_tables
                      union all
                      select 'view', view_name
                        from user_views
                      union all
                      select 'sequence', sequence_name
                        from user_sequences
                   )
                select object_type, count(*)
                  from objects
                 group by object_type;
                /
                """;
        formatAndAssert(input);
    }

    @Test
    public void insert_from_with_clause_with_subquery() {
        var input = """
                insert into t3
                with
                   combined as (
                      select object_type, count(*)
                        from (
                                select 'table' as object_type, table_name as object_name
                                  from user_tables
                                union all
                                select 'view', view_name
                                  from user_views
                                union all
                                select 'sequence', sequence_name
                                  from user_sequences
                             )
                       group by object_type
                   )
                select *
                  from combined;
                /
                """;
        formatAndAssert(input);
    }

}
