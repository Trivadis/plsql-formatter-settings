package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Issue_133_wrong_ws_removal extends ConfiguredTestFormatter {

    @Test
    public void simulate_sqldev_expand_logic() throws IOException {
        // 1. SQLDev expands the original statement in the background
        // and then calls the formatter, which crashes in 21.2.1 with
        //     java.lang.NullPointerException: Cannot invoke "oracle.dbtools.parser.ParseNode.children()" because "parent" is null
        // and leaves the Arbori program in an invalid state.
        // As a result the next formatter call uses the parse tree of this statement for Arbori queries.
        // See also discusson in https://community.oracle.com/tech/developers/discussion/comment/16807304 .
        var expanded = """
                SELECT CASE "A1"."JOB" WHEN 'ANALYST' THEN 3000 END  "CASEJOBWHEN'ANALYST'THEN3000END" FROM "SCOTT"."EMP" "A1"
                """;
        var expandedExpected = """
                select case "A1"."JOB"
                          when 'ANALYST' then
                             3000
                       end "CASEJOBWHEN'ANALYST'THEN3000END"
                  from "SCOTT"."EMP" "A1"
                """;
        try {
            var expandedActual = formatter.format(expanded);
            assertEquals(expandedExpected, expandedActual);
        } catch (NullPointerException e) {
            // ignore java.lang.NullPointerException: Cannot invoke "oracle.dbtools.parser.ParseNode.children()" because "parent" is null
        }

        // 2. User calls formatter for the original statement
        // When the first formatter call crashed then the result looks like this:
        //    select case jobwhen'ANALYST'
        //    then 3000 end
        //            from
        //    emp ;
        // The whitespace around "when" are removed because the Arbori query found a dot on lexer pos 3.
        // And the formatter replaced the whitespaces for its lexer pos 3, which is "when".
        // This is clearly a SQLDev bug. However, we can work around the problem by writing Arbori queries that do
        // not crash for expanded statements.
        var original = """
                select
                case job
                when 'ANALYST'
                then 3000
                end
                from emp;
                """;
        var originalExpected = """
                select case job
                          when 'ANALYST' then
                             3000
                       end
                  from emp;
                """;
        var originalActual = formatter.format(original);
        assertEquals(originalExpected, originalActual);
    }
}
