package com.trivadis.plsql.formatter.sandbox;

import oracle.dbtools.app.Format;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SandboxedFormatter {
    public static Format copy(Format original) {
        Format formatter = new Format();
        formatter.options.putAll(original.options);
        return formatter;
    }

    public static String format(Format formatter, String original, Integer timeout) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Format newFormatter = formatter;
        if (formatter.options.containsKey("timeout")) {
            // it is costly to create a new formatter, so we do that only when needed.
            formatter.options.remove("timeout");
            newFormatter = copy(formatter);
        }
        Future<String> future = executor.submit(new SandboxedFormatterTask(newFormatter, original));
        String result;
        try {
            result = future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);
            System.out.print("timeout... ");
            formatter.options.put("timeout", true);
            result = original;
        }
        executor.shutdown();
        return result;
    }
}
