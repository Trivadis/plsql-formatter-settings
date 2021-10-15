package com.trivadis.plsql.formatter.sqlcl.tests;

import oracle.dbtools.raptor.newscriptrunner.CommandListener;
import oracle.dbtools.raptor.newscriptrunner.CommandRegistry;
import oracle.dbtools.raptor.newscriptrunner.SQLCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TvdFormatTest extends AbstractFormatTest {

    @BeforeEach
    public void register() {
        runScript("--register");
        byteArrayOutputStream.reset();
    }

    @Test
    public void duplicate_registration_using_mixed_case() {
        reset();
        var originalListeners = CommandRegistry.getListeners(null, ctx).get(
                SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        var expected = """
                tvdformat registered as SQLcl command.
                """;

        // first registrations
        var actual1 = runScript("--RegisteR");
        Assertions.assertEquals(expected, actual1);
        final List<CommandListener> listeners1 = CommandRegistry.getListeners(null, ctx).get(
                SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        Assertions.assertEquals(originalListeners.size() + 1, listeners1.size());

        // second registration
        byteArrayOutputStream.reset();
        var actual2 = runScript("-R");
        Assertions.assertEquals(expected, actual2);
        final List<CommandListener> listeners2 = CommandRegistry.getListeners(null, ctx).get(
                SQLCommand.StmtSubType.G_S_FORALLSTMTS_STMTSUBTYPE);
        Assertions.assertEquals(originalListeners.size() + 1, listeners2.size());
    }

    @Test
    public void process_dir() {
        process_dir(RunType.TvdFormatCommand);
    }

    @Test
    public void process_pkb_only() {
        process_pkb_only(RunType.TvdFormatCommand);
    }

    @Test
    public void process_with_default_arbori() {
        process_with_default_arbori(RunType.TvdFormatCommand);
    }

    @Test
    public void process_with_xml() {
        process_with_xml(RunType.TvdFormatCommand);
    }

    @Test
    public void process_with_default_xml_default_arbori() {
        process_with_default_xml_default_arbori(RunType.TvdFormatCommand);
    }

    @Test
    public void process_with_embedded_xml_default_arbori() {
        process_with_embedded_xml_default_arbori(RunType.TvdFormatCommand);
    }

    @Test
    public void process_markdown_only() {
        process_markdown_only(RunType.TvdFormatCommand);
    }

    @Test
    public void process_config_file_array() throws IOException {
        process_config_file_array(RunType.TvdFormatCommand);
    }

    @Test
    public void process_config_file_object() throws IOException {
        process_config_file_object(RunType.TvdFormatCommand);
    }

    @Test
    public void process_config_file_object_and_param() throws IOException {
        process_config_file_object_and_param(RunType.TvdFormatCommand);
    }

    @Test
    public void process_config_dir_array() throws IOException {
        process_config_dir_array(RunType.TvdFormatCommand);
    }
}
