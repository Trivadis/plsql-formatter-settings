package com.trivadis.plsql.formatter.settings.tests

import com.trivadis.plsql.formatter.settings.ConfiguredTestFormatter
import org.junit.Test

class Issue_26 extends ConfiguredTestFormatter {
    
    @Test
    def create_smallfile_tablespace_one_datafile() {
        '''
            CREATE TABLESPACE my_table_space
               DATAFILE 'my_tablespace_file.dbf' SIZE 50M
               AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED
               EXTENT MANAGEMENT LOCAL AUTOALLOCATE;
        '''.formatAndAssert
    }

    @Test
    def create_smallfile_tablespace_three_datafiles() {
        '''
            CREATE SMALLFILE TABLESPACE demo
               DATAFILE '/u01/app/oracle/oradata/ODB/demo1.dbf' SIZE 5M,
               '/u01/app/oracle/oradata/ODB/demp2.dbf' SIZE 5M,
               '/u01/app/oracle/oradata/ODB/demp3.dbf' SIZE 5M
               AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED
               EXTENT MANAGEMENT LOCAL AUTOALLOCATE;
        '''.formatAndAssert
    }

    @Test
    def drop_tablepace() {
        '''
            DROP TABLESPACE demo INCLUDING CONTENTS;
        '''.formatAndAssert
    }

    @Test
    def create_oracle_managed_tablepsace() {
        '''
            CREATE TABLESPACE demo
               DATAFILE
               AUTOEXTEND OFF;
        '''.formatAndAssert
    }

    @Test
    def create_bigfile_tablespace() {
        '''
            CREATE BIGFILE TABLESPACE bigtbs_01
               DATAFILE 'bigtbs_f1.dbf' SIZE 20M
               AUTOEXTEND ON;
        '''.formatAndAssert
    }

    @Test
    def create_undo_tablespace() {
        '''
            CREATE UNDO TABLESPACE undots1
               DATAFILE 'undotbs_1a.dbf' SIZE 10M
               AUTOEXTEND ON
               RETENTION GUARANTEE;
        '''.formatAndAssert
    }

    @Test
    def create_temp_tablespace() {
        '''
            CREATE TEMPORARY TABLESPACE tbs_temp_02
               TEMPFILE 'temp02.dbf' SIZE 5M
               AUTOEXTEND ON
               TABLESPACE GROUP tbs_grp_01;
        '''.formatAndAssert
    }

}
