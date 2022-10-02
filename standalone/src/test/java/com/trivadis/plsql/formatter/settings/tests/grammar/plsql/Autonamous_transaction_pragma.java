package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Autonamous_transaction_pragma extends ConfiguredTestFormatter {

    @Test
    public void package_procedure() throws IOException {
        var input = """
                create package body pkg is
                procedure p is
                pragma
                autonomous_transaction
                ;
                begin
                commit;
                end p;
                end pkg;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                create package body pkg is
                   procedure p is
                      pragma autonomous_transaction;
                   begin
                      commit;
                   end p;
                end pkg;
                /
                """;
        assertEquals(expected, actual);
    }
}
