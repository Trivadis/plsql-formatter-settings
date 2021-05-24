package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Issue_5_type_members extends ConfiguredTestFormatter {

    @BeforeEach
    public void setup() {
        getFormatter().options.put(getFormatter().kwCase, Format.Case.lower);
    }

    @Test
    public void object_type_spec_overriding_member_function() {
        var sql = """
                create or replace type ut_xunit_reporter under ut_junit_reporter (
                   constructor function ut_xunit_reporter (
                      self in out nocopy ut_xunit_reporter
                   ) return self as result,
                   overriding member function get_description return varchar2
                ) not final
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void object_type_spec_final_member_function() {
        var sql = """
                create or replace type ut_xunit_reporter under ut_junit_reporter (
                   constructor function ut_xunit_reporter (
                      self in out nocopy ut_xunit_reporter
                   ) return self as result,
                   final member function get_description return varchar2
                ) not final
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void object_type_body_overriding_member_function() {
        var sql = """
                create or replace type body ut_xunit_reporter is
                   constructor function ut_xunit_reporter (
                      self in out nocopy ut_xunit_reporter
                   ) return self as result is
                   begin
                      self.init($$plsql_unit);
                      return;
                   end;
                   overriding member function get_description return varchar2 as
                   begin
                      return 'Depracated reporter. Please use Junit.
                    Provides outcomes in a format conforming with JUnit 4 and above as defined in: https://gist.github.com/kuzuha/232902acab1344d6b578';
                   end;
                end;
                /
                """;
        formatAndAssert(sql);
    }

    @Test
    public void object_type_body_final_member_function() {
        var sql = """
                create or replace type body ut_xunit_reporter is
                   constructor function ut_xunit_reporter (
                      self in out nocopy ut_xunit_reporter
                   ) return self as result is
                   begin
                      self.init($$plsql_unit);
                      return;
                   end;
                   final member function get_description return varchar2 as
                   begin
                      return 'Depracated reporter. Please use Junit.
                    Provides outcomes in a format conforming with JUnit 4 and above as defined in: https://gist.github.com/kuzuha/232902acab1344d6b578';
                   end;
                end;
                /
                """;
        formatAndAssert(sql);
    }
}
