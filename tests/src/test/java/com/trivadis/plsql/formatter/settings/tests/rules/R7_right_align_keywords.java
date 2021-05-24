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
        }

        @Test
        public void select_into_statement() throws IOException {
            var input = """
                    begin
                    select
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
                       select
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
            assertEquals(expected, actual);
        }

        @Test
        public void select_into_statement_with_subquery_inline() throws IOException {
            var input = """
                    begin
                    select
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
                       select
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
            assertEquals(expected, actual);
        }

        @Test
        public void select_into_statement_with_subquery_keep_newline() {
            var sql = """
                    begin
                       select
                          all count(*)
                         into x
                         from t1
                         join (
                                 select c1, c2, c3, c4
                                   from t2
                              ) t2
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
    }
}