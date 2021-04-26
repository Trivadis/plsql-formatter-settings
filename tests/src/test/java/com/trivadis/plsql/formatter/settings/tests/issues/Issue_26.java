package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;

public class Issue_26 extends ConfiguredTestFormatter {

    @Test
    public void create_smallfile_tablespace_one_datafile() {
        final String sql = 
            """
            create tablespace my_table_space
               datafile 'my_tablespace_file.dbf' size 50m
               autoextend on next 10m maxsize unlimited
               extent management local autoallocate;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_smallfile_tablespace_three_datafiles() {
        final String sql = 
            """
            create smallfile tablespace demo
               datafile '/u01/app/oracle/oradata/ODB/demo1.dbf' size 5m,
               '/u01/app/oracle/oradata/ODB/demp2.dbf' size 5m,
               '/u01/app/oracle/oradata/ODB/demp3.dbf' size 5m
               autoextend on next 10m maxsize unlimited
               extent management local autoallocate;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void drop_tablepace() {
        final String sql = 
            """
            drop tablespace demo including contents;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_oracle_managed_tablepsace() {
        final String sql = 
            """
            create tablespace demo
               datafile
               autoextend off;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_bigfile_tablespace() {
        final String sql = 
            """
            create bigfile tablespace bigtbs_01
               datafile 'bigtbs_f1.dbf' size 20m
               autoextend on;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_undo_tablespace() {
        final String sql = 
            """
            create undo tablespace undots1
               datafile 'undotbs_1a.dbf' size 10m
               autoextend on
               retention guarantee;
            """;
        formatAndAssert(sql);
    }

    @Test
    public void create_temp_tablespace() {
        final String sql = 
            """
            create temporary tablespace tbs_temp_02
               tempfile 'temp02.dbf' size 5m
               autoextend on
               tablespace group tbs_grp_01;
            """;
        formatAndAssert(sql);
    }

}
