package com.trivadis.plsql.formatter;

import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTvdFormatTest {
    Path tempDir;

    @BeforeEach
    public void setup() {
        try {
            tempDir = Files.createTempDirectory("tvdformat-test-");
            final URL url = Thread.currentThread().getContextClassLoader().getResource("input");
            assert url != null;
            final Path resourceDir = Paths.get(url.getPath());
            final List<Path> sources = Files.walk(resourceDir)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            for (Path source : sources) {
                final Path target = Paths.get(tempDir.toString() + File.separator + source.getFileName());
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
        Path file = Paths.get(tempDir.toString() + File.separator + fileName);
        return getFileContent(file);
    }

    public String getExpectedContent(String fileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource("expected");
        assert url != null;
        return getFileContent(Paths.get(url.getPath() + File.separator + fileName));
    }
}
