package com.trivadis.plsql.formatter;

import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public abstract class AbstractTvdFormatTest {
    Path tempDir;

    @BeforeEach
    public void setup() {
        try {
            tempDir = Files.createTempDirectory("tvdformat-test-");
            var url = Thread.currentThread().getContextClassLoader().getResource("input");
            assert url != null;
            var resourceDir = Paths.get(url.getPath());
            var sources = Files.walk(resourceDir)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            for (Path source : sources) {
                var target = Paths.get(tempDir.toString() + File.separator + source.getFileName());
                Files.copy(source, target);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public String getExpectedContent(String fileName) {
        var url = Thread.currentThread().getContextClassLoader().getResource("expected");
        assert url != null;
        return getFileContent(Paths.get(url.getPath() + File.separator + fileName));
    }
}
