package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class R7_right_align_keywords extends ConfiguredTestFormatter {

    @Nested
    class Select_commas_before {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.Before);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
            getFormatter().options.put(getFormatter().breakOnSubqueries, false);
        }

        @Test
        public void select_into_statement() throws IOException {
            var input = """
                    begin
                    select -- comment
                    all count(*)
                    into x
                    from t1
                    join t2 on t1.c1 = t2.c1
                    left join t3 on t3.c2 = t2.c2
                    cross join t4
                    where t1.c5 = t3.c5
                    and t4.c1 = 'hello'
                    group by t1.c7
                    having count(*) > 0
                    order by 1;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       select -- comment
                          all count(*)
                         into x
                         from t1
                         join t2
                           on t1.c1 = t2.c1
                         left join t3
                           on t3.c2 = t2.c2
                        cross join t4
                        where t1.c5 = t3.c5
                          and t4.c1 = 'hello'
                        group by t1.c7
                       having count(*) > 0
                        order by 1;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_into_statement_with_subquery_inline() throws IOException {
            var input = """
                    begin
                    select -- a comment
                    all count(*)
                    into x
                    from t1
                    join (select c1, c2, c3, c4
                    from t2) t2
                    on t1.c1 = t2.c1
                    left join t3 on t3.c2 = t2.c2
                    cross join t4
                    where t1.c5 = t3.c5
                    and t4.c1 = 'hello'
                    group by t1.c7
                    having count(*) > 0
                    order by 1;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       select -- a comment
                          all count(*)
                         into x
                         from t1
                         join (select c1, c2, c3, c4
                                 from t2) t2
                           on t1.c1 = t2.c1
                         left join t3
                           on t3.c2 = t2.c2
                        cross join t4
                        where t1.c5 = t3.c5
                          and t4.c1 = 'hello'
                        group by t1.c7
                       having count(*) > 0
                        order by 1;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_into_statement_with_subquery_keep_newline() {
            var sql = """
                    begin
                       select -- a comment
                          all count(*)
                         into x
                         from t1
                         join (
                                 select c1, c2, c3, c4
                                   from t2
                              ) t2
                           on t1.c1 = t2.c1
                         left join t3
                           on t3.c2 = t2.c2
                        cross join t4
                        where t1.c5 = t3.c5
                          and t4.c1 = 'hello'
                        group by t1.c7
                       having count(*) > 0
                        order by 1;
                    end;
                    /
                    """;
            formatAndAssert(sql);
        }

        @Test
        public void select_with_column_list() throws IOException {
            var input = """
                    create view v as
                    select a
                    ,b
                    ,c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create view v as
                    select a
                         , b
                         , c
                      from t;
                    """;
            assertEquals(expected, actual);
        }


    }

    @Nested
    class Select_commas_after {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void select_with_column_list() throws IOException {
            var input = """
                    create view v as
                    select a,
                    b,
                    c
                    from t;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    create view v as
                    select a,
                           b,
                           c
                      from t;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_boolean() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where a = 2 and b = 3 or c = 4;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where a = 2
                       and b = 3
                        or c = 4;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_case_in_boolean() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where case when a = 2 and b = 3 or c = 4 then 1 end = 1;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where case
                              when a = 2
                                 and b = 3
                                 or c = 4
                              then
                                 1
                           end = 1;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_boolean_in_parenthesis() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where a = 2 and (b = 3 or c = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where a = 2
                       and (b = 3 or c = 4);
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void select_with_boolean_in_parenthesis_and_line_break() throws IOException {
            var input = """
                    select a,
                    b,
                    c
                    from t where a = 2 and (b = 3
                    or c = 4);
                    """;
            var actual = formatter.format(input);
            var expected = """
                    select a,
                           b,
                           c
                      from t
                     where a = 2
                       and (b = 3 
                           or c = 4);
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Insert {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void single_table() throws IOException {
            var input = """
                    begin
                    insert
                    into t
                    values ('a', 'b');
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       insert
                         into t
                       values ('a', 'b');
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void multi_table() throws IOException {
            var input = """
                    insert
                    all
                    into t1 (c1, c2) values (c1, c2)
                    into t2 (c1, c2) values (c1, c2)
                    into t3 (c1, c2) values (c1, c2)
                    select c1, c2
                    from t4;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    insert
                       all
                      into t1 (c1, c2) values (c1, c2)
                      into t2 (c1, c2) values (c1, c2)
                      into t3 (c1, c2) values (c1, c2)
                    select c1, c2
                      from t4;
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void subselect() throws IOException {
            var input = """
                    begin
                    insert
                    into phs1 (c1)
                              (
                                 select 1 as c1
                    from dual
                              );
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       insert
                         into phs1 (c1)
                              (
                                 select 1 as c1
                                   from dual
                              );
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Update {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void update() throws IOException {
            var input = """
                    update employees
                    set job_id = 'SA_MAN'
                    ,salary = salary + 1000
                    ,department_id = 120
                    where first_name = 'Douglas'
                    and last_name = 'Grant';
                    """;
            var actual = formatter.format(input);
            var expected = """
                    update employees
                       set job_id = 'SA_MAN',
                           salary = salary + 1000,
                           department_id = 120
                     where first_name = 'Douglas'
                           and last_name = 'Grant';
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Delete {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().breaksComma, Format.Breaks.After);
            getFormatter().options.put(getFormatter().spaceAfterCommas, true);
        }

        @Test
        public void delete() throws IOException {
            var input = """
                    delete
                    from t
                    where c1 = 1
                    and c2 = 2;
                    """;
            var actual = formatter.format(input);
            var expected = """
                    delete
                      from t
                     where c1 = 1
                           and c2 = 2;
                    """;
            assertEquals(expected, actual);
        }
    }
}
