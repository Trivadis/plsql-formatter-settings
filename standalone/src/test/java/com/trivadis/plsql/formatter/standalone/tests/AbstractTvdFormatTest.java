package com.trivadis.plsql.formatter.standalone.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractTvdFormatTest {
    static final String[] TEST_FILES = new String[]
            {"unformatted/dont_format.txt", "unformatted/markdown.md", "unformatted/package_body.pkb",
                    "unformatted/query.sql", "unformatted/sql.txt", "unformatted/syntax_error.sql",
                    "trivadis_advanced_format.xml", "trivadis_custom_format.arbori"};
    static final PrintStream originalPrintStream = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    private Path tempDir;

    public String getTempDir() {
        return tempDir.toString().replace("\\", "/");
    }

    public Path getTempDirPath() {
        return tempDir;
    }

    @BeforeEach
    public void setup() throws IOException {
        tempDir = Files.createTempDirectory("tvdformat-test-");
        for (String fileName : TEST_FILES) {
            var url = Thread.currentThread().getContextClassLoader().getResource(fileName);
            assert url != null;
            var target = Paths.get(tempDir.toString() + File.separator + new File(url.getFile()).toPath().getFileName());
            Files.copy(url.openStream(), target);
        }
        System.setOut(printStream);
        outputStream.reset();
    }

    @AfterEach
    public void teardown() {
        System.clearProperty("tvdformat.standalone");
        System.clearProperty("polyglot.engine.WarnInterpreterOnly");
        System.setOut(originalPrintStream);
    }

    private String getFileContent(Path file) throws IOException {
        return new String(Files.readAllBytes(file));
    }

    public String getFormattedContent(String fileName) throws IOException {
        var file = Paths.get(tempDir.toString() + File.separator + fileName);
        return getFileContent(file);
    }

    public String getConsoleOutput() {
        return outputStream.toString();
    }

    public String getXML() {
        return getTempDir() + "/trivadis_advanced_format.xml";
    }

    public String getArbori() {
        return getTempDir() + "/trivadis_custom_format.arbori";
    }

}
