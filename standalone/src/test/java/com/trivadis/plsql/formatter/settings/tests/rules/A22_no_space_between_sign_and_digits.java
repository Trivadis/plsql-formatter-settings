package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class A22_no_space_between_sign_and_digits extends ConfiguredTestFormatter {

    @Test
    public void insert_negative_number() {
        var sql = """
                insert into dept (deptno, dname, loc)
                values (-1, 'Testing', 'Winterthur');
                """;
        formatAndAssert(sql);
    }

    @Test
    public void insert_positive_number() {
        var sql = """
                insert into dept (deptno, dname, loc)
                values (+1, 'Testing', 'Winterthur');
                """;
        formatAndAssert(sql);
    }

    @Test
    public void insert_expression_with_negative_number() {
        var sql = """
                insert into dept (deptno, dname, loc)
                values ((a + b / 2) + -1, 'Testing', 'Winterthur');
                """;
        formatAndAssert(sql);
    }

    @Test
    public void assignment_with_negative_number() {
        var sql = """
                begin
                   a := b - 1;
                end;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void assignment_with_positive_number() {
        var sql = """
                begin
                   x := p1(1) + p1(2) + 17;
                end;
                """;
        formatAndAssert(sql);
    }
}
