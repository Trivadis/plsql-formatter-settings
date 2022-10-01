package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Comment extends ConfiguredTestFormatter {

    @Test
    public void single_line() throws IOException {
        var input = """
                begin
                -- singe line comment
                null;  -- another single line comment
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   -- singe line comment
                   null;  -- another single line comment
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void mutli_line() throws IOException {
        var input = """
                begin
                /* multi
                   line
                   comment */
                null  /* another multi line comment */;
                end;
                /
                """;
        var actual = formatter.format(input);
        var expected = """
                begin
                   /* multi
                      line
                      comment */
                   null  /* another multi line comment */;
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void within_loop() {
        var sql = """
                begin
                   for i in 1..10
                   loop
                      null;
                      -- single line comment
                      /* multi line comment */
                   end loop;
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void within_if() {
        var sql = """
                begin
                   if xyz = 1 then
                      null;
                      -- single line comment
                      /* multi line comment */
                   elsif xyz = 2 then
                      null;
                      -- single line comment
                      /* multi line comment */
                   else
                      null;
                      -- single line comment
                      /* multi line comment */
                   end if;
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void within_simple_case(){
        var sql = """
                begin
                   case xyz
                      when 1 then
                         null;
                         -- single line comment
                         /* multi line comment */
                      when 2 then
                         null;
                         -- single line comment
                         /* multi line comment */
                      else
                         null;
                         -- single line comment
                         /* multi line comment */
                   end case;
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void within_searched_case(){
        var sql = """
                begin
                   case
                      when xyz = 1 then
                         null;
                         -- single line comment
                         /* multi line comment */
                      when xyz = 2 then
                         null;
                         -- single line comment
                         /* multi line comment */
                      else
                         null;
                         -- single line comment
                         /* multi line comment */
                   end case;
                end;
                /
                """;
        formatAndAssert(sql);
    }
}
