package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import oracle.dbtools.app.Format
import org.junit.Before
import org.junit.Test

class Issue_5 extends ConfiguredTestFormatter {
    
    @Before
    def void setup() {
        formatter.options.put(formatter.kwCase, Format.Case.lower)
    }
    
    @Test
    def object_type_spec_overriding_member_function() {
        '''
            create or replace type ut_xunit_reporter under ut_junit_reporter (
               constructor function ut_xunit_reporter (
                  self in out nocopy ut_xunit_reporter
               ) return self as result,
               overriding member function get_description return varchar2
            ) not final
            /
        '''.formatAndAssert
    }

    @Test
    def object_type_spec_final_member_function() {
        '''
            create or replace type ut_xunit_reporter under ut_junit_reporter (
               constructor function ut_xunit_reporter (
                  self in out nocopy ut_xunit_reporter
               ) return self as result,
               final member function get_description return varchar2
            ) not final
            /
        '''.formatAndAssert
    }

    @Test
    def object_type_body_overriding_member_function() {
        '''
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
        '''.formatAndAssert
    }

    @Test
    def object_type_body_final_member_function() {
        '''
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
        '''.formatAndAssert
    }

}
