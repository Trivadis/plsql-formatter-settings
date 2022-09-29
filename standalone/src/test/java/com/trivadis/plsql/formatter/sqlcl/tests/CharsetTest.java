package com.trivadis.plsql.formatter.sqlcl.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CharsetTest extends AbstractSqlclTest {

    @SuppressWarnings({"ResultOfMethodCallIgnored", "resource"})
    @BeforeEach
    public void setup() {
        try {
            super.setup();
            Files.walk(tempDir).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
            var url = Thread.currentThread().getContextClassLoader().getResource("charset");
            assert url != null;
            var dir = new File(url.getFile()).toPath();
            var sources = Files.walk(dir).filter(Files::isRegularFile).toList();
            for (Path source : sources) {
                Path target = Paths.get(tempDir.toString() + File.separator + source.getFileName());
                Files.copy(source, target);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path sourceFile(String fileName) {
        var url = Thread.currentThread().getContextClassLoader().getResource("charset/" + fileName);
        assert url != null;
        return new File(url.getFile()).toPath();
    }

    @Nested
    class FormatJS {

        public boolean isSameAsOriginalContent(String fileName) {
            var sourceFile = sourceFile(fileName);
            var targetFile = Paths.get(tempDir.toString() + File.separator + fileName);
            try {
                return Files.mismatch(sourceFile, targetFile) == -1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        public void formatUtf8() {
            var actual = runScript(tempDir.toString() + File.separator + "utf8.sql");
            Assertions.assertNotNull(actual);
            Assertions.assertTrue(isSameAsOriginalContent("utf8.sql"));
        }

        @Test
        public void formatWindows1252() {
            var actual = runScript(tempDir.toString() + File.separator + "windows-1252.sql");
            Assertions.assertNotNull(actual);
            Assertions.assertTrue(isSameAsOriginalContent("windows-1252.sql"));
        }
    }

    @Nested
    class DetectCharsetJava {

        private Charset detectCharset(byte[] content) {
            // rudimentary solution since Apache Tika cannot be used in SQLcl
            // try default character set of the OS (can be overridden via -Dfile.encoding), then UTF-8, then windows-1252
            var defaultCharsetName = Charset.defaultCharset().name();
            var charsetNames = Arrays.asList("UTF-8", defaultCharsetName, "windows-1252");
            for (var charsetName : charsetNames) {
                var cs = Charset.forName(charsetName);
                try {
                    cs.newDecoder().decode(ByteBuffer.wrap(content));
                    return cs;
                } catch (CharacterCodingException e) {
                    // ignore exception
                }
            }
            return null;
        }

        @Test
        public void detect_windows_1252() throws IOException {
            var file = sourceFile("windows-1252.sql");
            var content = Files.readAllBytes(file);
            var actual = detectCharset(content);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals("windows-1252", actual.name());
        }

        @Test
        public void detect_utf_8() throws IOException {
            var file = sourceFile("utf8.sql");
            var content = Files.readAllBytes(file);
            var actual = detectCharset(content);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals("UTF-8", actual.name());
        }
    }
}
