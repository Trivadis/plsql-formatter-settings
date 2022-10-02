package com.trivadis.plsql.formatter.settings.tests.grammar.plsql;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Call_specification extends ConfiguredTestFormatter {

    @Test
    public void c_declaration() {
        var sql = """
                create procedure utl_xml_parse_query(
                   in_current_userid in            number,
                   in_schema_name    in            varchar2,
                   in_query          in            clob,
                   in_result         in out nocopy clob
                ) is
                   language c
                   library sys.utl_xml_lib
                   name KUXPARSEQUERY
                   with context parameters (
                      context,
                      in_current_userid ocinumber,
                      in_current_userid indicator,
                      in_schema_name    ocistring,
                      in_schema_name    indicator,
                      in_query          ociloblocator,
                      in_query          indicator,
                      in_result         ociloblocator,
                      in_result         indicator
                   );
                """;
        formatAndAssert(sql);
    }

    @Test
    public void java_declaration() {
        var sql = """
                create or replace package base64helper is
                   procedure encode(p_source in     blob,
                                    p_target in out clob) is
                      language java
                      name 'Base64Helper.encode(oracle.sql.BLOB,oracle.sql.CLOB[])';
                                
                   procedure decode(p_source in     clob,
                                    p_target in out blob) is
                      language java
                      name 'Base64Helper.decode(oracle.sql.CLOB,oracle.sql.BLOB[])';
                end base64helper;
                /
                """;
        formatAndAssert(sql);
    }

}
