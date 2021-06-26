package com.trivadis.plsql.formatter.settings.tests.grammar;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        var actual = formatter.format(input);
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
