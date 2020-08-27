package com.trivadis.plsql.formatter.sqlcl.tests

import oracle.dbtools.raptor.newscriptrunner.CommandRegistry
import oracle.dbtools.raptor.newscriptrunner.SQLCommand
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TvdFormatTest extends AbstractFormatTest {
    
    @Before
    def void register() {
        runScript("--register")
        byteArrayOutputStream.reset();
    }
    
    @Test
    def void duplicate_registration_using_mixed_case() {
        reset();
        val originalListeners = CommandRegistry.getListeners(null, ctx).get(
            SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        val expected = '''
            tvdformat registered as SQLcl command.
        '''
                
        // first registrations
        val actual1 = runScript("--RegisteR")
        Assert.assertEquals(expected, actual1)
        val listeners1 = CommandRegistry.getListeners(null, ctx).get(
            SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        Assert.assertEquals(originalListeners.size() + 1, listeners1.size())

        // second registration
        byteArrayOutputStream.reset
        val actual2 = runScript("-R")
        Assert.assertEquals(expected, actual2)
        val listeners2 = CommandRegistry.getListeners(null, ctx).get(
            SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        Assert.assertEquals(originalListeners.size() + 1, listeners2.size())
    }

    @Test
    def void process_dir() {
        process_dir(RunType.TvdFormatCommand)
    }

    @Test
    def void process_pkb_only() {
        process_pkb_only(RunType.TvdFormatCommand)
    }

    @Test
    def void process_with_original_arbori() {
        process_with_original_arbori(RunType.TvdFormatCommand)
    }

    @Test
    def void process_with_default_arbori() {
        process_with_default_arbori(RunType.TvdFormatCommand)
    }

    @Test
    def void process_with_xml() {
        process_with_xml(RunType.TvdFormatCommand)
    }

    @Test
    def void process_with_default_xml_default_arbori() {
        process_with_default_xml_default_arbori(RunType.TvdFormatCommand)
    }

    @Test
    def void process_with_embedded_xml_default_arbori() {
        process_with_embedded_xml_default_arbori(RunType.TvdFormatCommand)
    }

}
