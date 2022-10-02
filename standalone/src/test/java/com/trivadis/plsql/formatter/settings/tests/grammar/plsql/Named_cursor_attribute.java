package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Named_cursor_attribute extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @ParameterizedTest
    @ValueSource(strings = {"isopen", "found", "notfound", "rowcount"})
    public void tokenized_attr(String attr) throws IOException {
        var input = """
                begin
                if
                not
                c1
                %
                #ATTR#
                then
                null;
                end
                if
                ;
                end
                ;
                """.replace("#ATTR#", attr);
        var actual = getFormatter().format(input);
        var expected = """
                begin
                   if not c1%#ATTR# then
                      null;
                   end if;
                end;
                """.replace("#ATTR#", attr);
        assertEquals(expected, actual);
    }
}
