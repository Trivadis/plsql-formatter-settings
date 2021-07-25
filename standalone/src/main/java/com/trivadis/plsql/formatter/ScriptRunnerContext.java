package com.trivadis.plsql.formatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ScriptRunnerContext {
    private OutputStream outputStream;

    ScriptRunnerContext() {
        super();
    }

    @SuppressWarnings("unused")
    public Object getProperty(String name) {
        if (name.equals("script.runner.cd_command")) {
            return Paths.get("").toAbsolutePath().toString();
        }
        return null;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    @SuppressWarnings("unused")
    public void write(String string) throws IOException {
        outputStream.write(string.getBytes(StandardCharsets.UTF_8));
    }
}
