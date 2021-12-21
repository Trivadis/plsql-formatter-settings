package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_152_extract extends ConfiguredTestFormatter {

    @Test
    public void extract_from() {
        var sql = """
                select e.last_name, e.first_name, d.department_name
                  from employees e
                  join departments d
                    on e.department_id = d.department_id
                 where extract(month from e.hire_date) = extract(month from sysdate);
                """;
        formatAndAssert(sql);
    }
}
