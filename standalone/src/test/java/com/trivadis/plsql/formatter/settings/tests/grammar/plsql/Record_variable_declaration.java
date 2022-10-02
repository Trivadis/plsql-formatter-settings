package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Record_variable_declaration extends ConfiguredTestFormatter {

    @BeforeAll
    public void setup() {
        setOption(getFormatter().idCase, Format.Case.lower);
    }

    @Test
    public void example_6_41() throws IOException {
        // RECORD Type Definition and Variable Declaration
        // line break before record to show that "double" indentation is intentional
        var input = """
                DECLARE
                  TYPE DeptRecTyp IS
                  RECORD (
                    dept_id    NUMBER(4) NOT NULL := 10,
                    dept_name  VARCHAR2(30) NOT NULL := 'Administration',
                    mgr_id     NUMBER(6) := 200,
                    loc_id     NUMBER(4) := 1700
                  );
                  dept_rec DeptRecTyp;
                BEGIN
                  DBMS_OUTPUT.PUT_LINE('dept_id:   ' || dept_rec.dept_id);
                  DBMS_OUTPUT.PUT_LINE('dept_name: ' || dept_rec.dept_name);
                  DBMS_OUTPUT.PUT_LINE('mgr_id:    ' || dept_rec.mgr_id);
                  DBMS_OUTPUT.PUT_LINE('loc_id:    ' || dept_rec.loc_id);
                END;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   type deptrectyp is
                      record(
                         dept_id   number(4) not null    := 10,
                         dept_name varchar2(30) not null := 'Administration',
                         mgr_id    number(6)             := 200,
                         loc_id    number(4)             := 1700
                      );
                   dept_rec deptrectyp;
                begin
                   dbms_output.put_line('dept_id:   ' || dept_rec.dept_id);
                   dbms_output.put_line('dept_name: ' || dept_rec.dept_name);
                   dbms_output.put_line('mgr_id:    ' || dept_rec.mgr_id);
                   dbms_output.put_line('loc_id:    ' || dept_rec.loc_id);
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_6_42() throws IOException {
        // RECORD Type with RECORD Field (Nested Record)
        var input = """
                DECLARE
                  TYPE name_rec IS RECORD (
                    first  employees.first_name%TYPE,
                    last   employees.last_name%TYPE
                  );
                
                  TYPE contact IS RECORD (
                    name  name_rec,                    -- nested record
                    phone employees.phone_number%TYPE
                  );
                
                  friend contact;
                BEGIN
                  friend.name.first := 'John';
                  friend.name.last := 'Smith';
                  friend.phone := '1-650-555-1234';
                
                  DBMS_OUTPUT.PUT_LINE (
                    friend.name.first  || ' ' ||
                    friend.name.last   || ', ' ||
                    friend.phone
                  );
                END;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   type name_rec is record(
                         first employees.first_name%type,
                         last  employees.last_name%type
                      );
                                
                   type contact is record(
                         name  name_rec,                    -- nested record
                         phone employees.phone_number%type
                      );
                                
                   friend contact;
                begin
                   friend.name.first := 'John';
                   friend.name.last  := 'Smith';
                   friend.phone      := '1-650-555-1234';
                                
                   dbms_output.put_line(
                      friend.name.first
                      || ' '
                      || friend.name.last
                      || ', '
                      || friend.phone
                   );
                end;
                /
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void example_6_43() throws IOException {
        // RECORD Type with Varray Field
        var input = """
                DECLARE
                  TYPE full_name IS VARRAY(2) OF VARCHAR2(20);
                
                  TYPE contact IS RECORD (
                    name  full_name := full_name('John', 'Smith'),  -- varray field
                    phone employees.phone_number%TYPE
                  );
                
                  friend contact;
                BEGIN
                  friend.phone := '1-650-555-1234';
                
                  DBMS_OUTPUT.PUT_LINE (
                    friend.name(1) || ' ' ||
                    friend.name(2) || ', ' ||
                    friend.phone
                  );
                END;
                /
                """;
        var actual = getFormatter().format(input);
        var expected = """
                declare
                   type full_name is varray(2) of varchar2(20);
                                
                   type contact is record(
                         name  full_name := full_name('John', 'Smith'),  -- varray field
                         phone employees.phone_number%type
                      );
                                
                   friend contact;
                begin
                   friend.phone := '1-650-555-1234';
                                
                   dbms_output.put_line(
                      friend.name(1)
                      || ' '
                      || friend.name(2)
                      || ', '
                      || friend.phone
                   );
                end;
                /
                """;
        assertEquals(expected, actual);
    }
}
