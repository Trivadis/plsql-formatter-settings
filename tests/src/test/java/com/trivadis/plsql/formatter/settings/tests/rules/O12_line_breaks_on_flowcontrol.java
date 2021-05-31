package com.trivadis.plsql.formatter.settings.tests.rules;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import oracle.dbtools.app.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class O12_line_breaks_on_flowcontrol extends ConfiguredTestFormatter {

    @Nested
    class Indented_actions_inlined_conditions {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().flowControl, Format.FlowControl.IndentedActions);
        }

        @Test
        public void if_statement() throws IOException {
            var input = """
                    begin
                       if a = 1 then b; elsif a = 2 then c; else d; end if;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       if a = 1 then
                          b;
                       elsif a = 2 then
                          c;
                       else
                          d;
                       end if;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void searched_case_statement() throws IOException {
            var input = """
                    begin
                       case when a = 1 then b; when a = 2 then c; else d; end case;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       case
                          when a = 1 then
                             b;
                          when a = 2 then
                             c;
                          else
                             d;
                       end case;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void simple_case_expression() throws IOException {
            var input = """
                    begin
                       l_value := case a when 1 then b when 2 then c else d end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l_value := case a
                                     when 1 then
                                        b
                                     when 2 then
                                        c
                                     else
                                        d
                                  end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Terse {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().flowControl, Format.FlowControl.Terse);
        }

        @Test
        public void if_statement() throws IOException {
            var input = """
                    begin
                       if a = 1 then b; elsif a = 2 then c; else d; end if;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       if a = 1 then b;
                       elsif a = 2 then c;
                       else d;
                       end if;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void searched_case_statement() throws IOException {
            var input = """
                    begin
                       case when a = 1 then b; when a = 2 then c; else d; end case;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       case
                          when a = 1 then b;
                          when a = 2 then c;
                          else d;
                       end case;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void simple_case_expression() throws IOException {
            var input = """
                    begin
                       l_value := case a when 1 then b when 2 then c else d end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l_value := case a
                                     when 1 then b
                                     when 2 then c
                                     else d
                                  end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Line_breaks_after_conditions_and_actions {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().flowControl, Format.FlowControl.SeparateConditionsActions);
        }

        @Test
        public void if_statement() throws IOException {
            var input = """
                    begin
                       if a = 1 then b; elsif a = 2 then c; else d; end if;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       if a = 1
                       then b;
                       elsif a = 2
                       then c;
                       else d;
                       end if;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void searched_case_statement() throws IOException {
            var input = """
                    begin
                       case when a = 1 then b; when a = 2 then c; else d; end case;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       case
                          when a = 1
                          then b;
                          when a = 2
                          then c;
                          else d;
                       end case;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void simple_case_expression() throws IOException {
            var input = """
                    begin
                       l_value := case a when 1 then b when 2 then c else d end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l_value := case a
                                     when 1
                                     then b
                                     when 2
                                     then c
                                     else d
                                  end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }

    @Nested
    class Indented_conditions_and_actions {

        @BeforeEach
        public void setup() {
            getFormatter().options.put(getFormatter().flowControl, Format.FlowControl.IndentedConditionsActions);
        }

        @Test
        public void if_statement() throws IOException {
            var input = """
                    begin
                       if a = 1 then b; elsif a = 2 then c; else d; end if;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       if
                          a = 1
                       then
                          b;
                       elsif
                          a = 2
                       then
                          c;
                       else
                          d;
                       end if;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void searched_case_statement() throws IOException {
            var input = """
                    begin
                       case when a = 1 then b; when a = 2 then c; else d; end case;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       case
                          when
                             a = 1
                          then
                             b;
                          when
                             a = 2
                          then
                             c;
                          else
                             d;
                       end case;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }

        @Test
        public void simple_case_expression() throws IOException {
            var input = """
                    begin
                       l_value := case a when 1 then b when 2 then c else d end;
                    end;
                    /
                    """;
            var actual = formatter.format(input);
            var expected = """
                    begin
                       l_value := case a
                                     when
                                        1
                                     then
                                        b
                                     when
                                        2
                                     then
                                        c
                                     else
                                        d
                                  end;
                    end;
                    /
                    """;
            assertEquals(expected, actual);
        }
    }
}
