package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class A21_remove_non_ws_indent extends ConfiguredTestFormatter {

    @Test
    public void not_applicable() throws IOException {
        // don't know how to write a test case for this Arbori section
        // tested manually by faking original indent to see if the replacement works.
        // e.g. var indent = getIndent(pos) + "aaa bbb\tccc\nddd";
    }
}
