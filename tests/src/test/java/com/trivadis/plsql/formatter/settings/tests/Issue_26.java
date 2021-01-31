package com.trivadis.plsql.formatter.settings.tests;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.Test;

public class Issue_26 extends ConfiguredTestFormatter {

    @Test
    public void create_smallfile_tablespace_one_datafile() {
        final String sql = 
            """
            CREATE TABLESPACE my_table_space
               DATAFILE 'my_tablespace_file.dbf' SIZE 50M
               AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED
               EXTENT MANAGEMENT LOCAL AUTOALLOCATE;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_smallfile_tablespace_three_datafiles() {
        final String sql = 
            """
            CREATE SMALLFILE TABLESPACE demo
               DATAFILE '/u01/app/oracle/oradata/ODB/demo1.dbf' SIZE 5M,
               '/u01/app/oracle/oradata/ODB/demp2.dbf' SIZE 5M,
               '/u01/app/oracle/oradata/ODB/demp3.dbf' SIZE 5M
               AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED
               EXTENT MANAGEMENT LOCAL AUTOALLOCATE;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void drop_tablepace() {
        final String sql = 
            """
            DROP TABLESPACE demo INCLUDING CONTENTS;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_oracle_managed_tablepsace() {
        final String sql = 
            """
            CREATE TABLESPACE demo
               DATAFILE
               AUTOEXTEND OFF;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_bigfile_tablespace() {
        final String sql = 
            """
            CREATE BIGFILE TABLESPACE bigtbs_01
               DATAFILE 'bigtbs_f1.dbf' SIZE 20M
               AUTOEXTEND ON;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_undo_tablespace() {
        final String sql = 
            """
            CREATE UNDO TABLESPACE undots1
               DATAFILE 'undotbs_1a.dbf' SIZE 10M
               AUTOEXTEND ON
               RETENTION GUARANTEE;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_temp_tablespace() {
        final String sql = 
            """
            CREATE TEMPORARY TABLESPACE tbs_temp_02
               TEMPFILE 'temp02.dbf' SIZE 5M
               AUTOEXTEND ON
               TABLESPACE GROUP tbs_grp_01;
            """;
        formatAndAssert(sql);
    }

}
