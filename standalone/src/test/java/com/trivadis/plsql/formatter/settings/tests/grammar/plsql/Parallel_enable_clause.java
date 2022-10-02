package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Parallel_enable_clause extends ConfiguredTestFormatter {

    @Test
    public void tokenized_parallel_enable() throws IOException {
        var input = """
                create
                function
                f
                (
                in_cursor
                in
                sys_refcursor
                )
                return
                some_collection_type
                pipelined
                parallel_enable
                is
                begin
                return;
                end;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create function f(
                   in_cursor in sys_refcursor
                )
                   return some_collection_type
                   pipelined
                   parallel_enable
                is
                begin
                   return;
                end;
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void tokenized_parallel_enable_partition_by_any() throws IOException {
        var input = """
                create
                function
                f
                (
                in_cursor
                in
                sys_refcursor
                )
                return
                some_collection_type
                pipelined
                parallel_enable
                (
                partition
                in_cursor
                by
                any
                )
                is
                begin
                return;
                end;
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create function f(
                   in_cursor in sys_refcursor
                )
                   return some_collection_type
                   pipelined
                   parallel_enable (partition in_cursor by any)
                is
                begin
                   return;
                end;
                """;
        assertEquals(expected, actual);
    }
}
