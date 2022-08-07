package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FormatTest extends AbstractFormatTest {

    @Test
    public void process_dir() {
        process_dir(RunType.FormatJS);
    }

    @Test
    public void process_pkb_only() {
        process_pkb_only(RunType.FormatJS);
    }

    @Test
    public void process_with_default_arbori() {
        process_with_default_arbori(RunType.FormatJS);
    }

    @Test
    public void process_with_xml() {
        process_with_xml(RunType.FormatJS);
    }

    @Test
    public void process_with_default_xml_default_arbori() {
        process_with_default_xml_default_arbori(RunType.FormatJS);
    }

    @Test
    public void process_with_embedded_xml_default_arbori() {
        process_with_embedded_xml_default_arbori(RunType.FormatJS);
    }

    @Test
    public void process_markdown_only() {
        process_markdown_only(RunType.FormatJS);
    }

    @Test
    public void process_config_file_array() throws IOException {
        process_config_file_array(RunType.FormatJS);
    }

    @Test
    public void process_config_file_object() throws IOException {
        process_config_file_object(RunType.FormatJS);
    }

    @Test
    public void process_config_file_object_and_param() throws IOException {
        process_config_file_object_and_param(RunType.FormatJS);
    }

    @Test
    public void process_config_dir_array() throws IOException {
        process_config_dir_array(RunType.FormatJS);
    }

    @Test
    public void process_sql_txt_default() {
        process_sql_txt_default(RunType.FormatJS);
    }

    @Test
    public void process_sql_txt_force() {
        process_sql_txt_force(RunType.FormatJS);
    }

    @Test
    public void process_dir_all_errors() {
        process_dir_all_errors(RunType.FormatJS);
    }

    @Test
    public void process_dir_mext_errors() {
        process_dir_mext_errors(RunType.FormatJS);
    }

    @Test
    public void process_dir_ext_errors() {
        process_dir_ext_errors(RunType.FormatJS);
    }

    @Test
    public void process_dir_no_errors() {
        process_dir_no_errors(RunType.FormatJS);
    }

}
