package com.trivadis.plsql.formatter.standalone.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractTvdFormatTest {
    static final PrintStream originalPrintStream = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    Path tempDir;

    @BeforeEach
    public void setup() {
        try {
            tempDir = Files.createTempDirectory("tvdformat-test-");
            var url = Thread.currentThread().getContextClassLoader().getResource("unformatted");
            assert url != null;
            var resourceDir = Paths.get(url.getPath());
            var sources = Files.walk(resourceDir)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            for (Path source : sources) {
                var target = Paths.get(tempDir.toString() + File.separator + source.getFileName());
                Files.copy(source, target);
            }
            System.setOut(printStream);
            outputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void teardown() {
        System.clearProperty("tvdformat.standalone");
        System.clearProperty("polyglot.engine.WarnInterpreterOnly");
        System.setOut(originalPrintStream);
    }

    private String getFileContent(Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFormattedContent(String fileName) {
        var file = Paths.get(tempDir.toString() + File.separator + fileName);
        return getFileContent(file);
    }

    public String getConsoleOutput() {
        return outputStream.toString();
    }
}
