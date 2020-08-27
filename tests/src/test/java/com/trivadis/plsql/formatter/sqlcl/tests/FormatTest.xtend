package com.trivadis.plsql.formatter.sqlcl.tests

import org.junit.Test

class FormatTest extends AbstractFormatTest {
    
    @Test
    def void process_dir() {
        process_dir(RunType.FormatJS)
    }

    @Test
    def void process_pkb_only() {
        process_pkb_only(RunType.FormatJS)
    }
    
    @Test
    def void process_with_original_arbori() {
        process_with_original_arbori(RunType.FormatJS)
    }

    @Test
    def void process_with_default_arbori() {
        process_with_default_arbori(RunType.FormatJS)
    }

    @Test
    def void process_with_xml() {
        process_with_xml(RunType.FormatJS)
    }

    @Test
    def void process_with_default_xml_default_arbori() {
        process_with_default_xml_default_arbori(RunType.FormatJS)
    }

    @Test
    def void process_with_embedded_xml_default_arbori() {
         process_with_embedded_xml_default_arbori(RunType.FormatJS)

    }

}
