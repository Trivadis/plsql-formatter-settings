package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_114_honor_no_space_after_commas_config extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().breaksComma, Format.Breaks.Before);
    }

    @Test
    public void no_space_after_comma() throws IOException {
        setOption(getFormatter().spaceAfterCommas, false);
        var input = """
                select empno
                     , nvl(mgr, 0) as mgr
                     , ename
                  from emp e;
                """;
        var expected = """
                select empno
                      ,nvl(mgr,0) as mgr
                      ,ename
                  from emp e;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

    @Test
    public void space_after_comma() throws IOException {
        setOption(getFormatter().spaceAfterCommas, true);
        var input = """
                select empno
                      ,nvl(mgr,0) as mgr
                      ,ename
                  from emp e;
                """;
        var expected = """
                select empno
                     , nvl(mgr, 0) as mgr
                     , ename
                  from emp e;
                """;
        var actual = formatter.format(input);
        assertEquals(expected, actual);
    }

}
