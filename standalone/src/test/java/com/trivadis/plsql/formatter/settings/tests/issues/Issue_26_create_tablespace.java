package com.trivadis.plsql.formatter.settings.tests.issues;

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Issue_26_create_tablespace extends ConfiguredTestFormatter {

    @Test
    public void create_smallfile_tablespace_one_datafile() {
        var sql = """
                create tablespace my_table_space
                   datafile 'my_tablespace_file.dbf' size 50m
                   autoextend on next 10m maxsize unlimited
                   extent management local autoallocate;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void create_smallfile_tablespace_three_datafiles() {
        var sql = """
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
        var sql = """
                drop tablespace demo including contents;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void create_oracle_managed_tablepsace() {
        var sql = """
                create tablespace demo
                   datafile
                   autoextend off;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void create_bigfile_tablespace() {
        var sql = """
                create bigfile tablespace bigtbs_01
                   datafile 'bigtbs_f1.dbf' size 20m
                   autoextend on;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void create_undo_tablespace() {
        var sql = """
                create undo tablespace undots1
                   datafile 'undotbs_1a.dbf' size 10m
                   autoextend on
                   retention guarantee;
                """;
        formatAndAssert(sql);
    }

    @Test
    public void create_temp_tablespace() {
        var sql = """
                create temporary tablespace tbs_temp_02
                   tempfile 'temp02.dbf' size 5m
                   autoextend on
                   tablespace group tbs_grp_01;
                """;
        formatAndAssert(sql);
    }
}
