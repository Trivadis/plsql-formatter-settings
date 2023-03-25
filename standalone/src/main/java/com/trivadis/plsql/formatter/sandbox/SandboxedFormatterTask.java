package com.trivadis.plsql.formatter.sandbox;

import oracle.dbtools.app.Format;

import java.util.concurrent.Callable;

public class SandboxedFormatterTask implements Callable<String> {
    final Format formatter;
    final String original;

    public SandboxedFormatterTask(Format formatter, String original) {
        this.formatter = formatter;
        this.original = original;
    }

    @Override
    public String call() throws Exception {
        return formatter.format(original);
    }
}
