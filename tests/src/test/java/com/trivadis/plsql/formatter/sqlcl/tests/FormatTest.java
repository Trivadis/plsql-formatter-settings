package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.Test;

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
    public void process_with_original_arbori() {
        process_with_original_arbori(RunType.FormatJS);
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

}
