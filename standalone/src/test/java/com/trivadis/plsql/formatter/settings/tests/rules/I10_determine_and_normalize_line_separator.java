package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class I10_determine_and_normalize_line_separator extends ConfiguredTestFormatter {

    @Test
    public void mixed_to_crlf() throws IOException {
        var input = "select\n*\r\nfrom emp where deptno = 20\norder by sal;";
        var expected = "select *\r\n  from emp\r\n where deptno = 20\r\n order by sal;";
        var actual = formatter.format(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void lf_only() throws IOException {
        var input = "select\n*\nfrom emp where deptno = 20\norder by sal;";
        var expected = "select *\n  from emp\n where deptno = 20\n order by sal;";
        var actual = formatter.format(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void crlf_only() throws IOException {
        var input = "select\r\n*\r\nfrom emp where deptno = 20\r\norder by sal;";
        var expected = "select *\r\n  from emp\r\n where deptno = 20\r\n order by sal;";
        var actual = formatter.format(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void system_default() throws IOException {
        var input = "begin null; end;";
        var expected = "begin" + System.lineSeparator() + "   null;" + System.lineSeparator() + "end;";
        var actual = formatter.format(input);
        Assertions.assertEquals(expected, actual);
    }
}
