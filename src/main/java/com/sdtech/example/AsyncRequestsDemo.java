package com.sdtech.example;

import io.vavr.concurrent.Future;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class AsyncRequestsDemo {
    private static final Consumer<String> stdout = System.out::println;

    public static void main(String[] args) {
        try {
            new AsyncRequestsDemo().run();
            stdout.accept("Run complete.");
        } finally {
            ForkJoinPool.commonPool().awaitQuiescence(15, TimeUnit.SECONDS);
        }
    }

    private void run() {
        var requests = asList(
                Future.of(this::cacheSession),
                Future.of(this::persistSession)
        );

        var result = Future.firstCompletedOf(requests).get();

        stdout.accept(result + " result ready first.");
    }

    private String cacheSession() {
        wait(1000);
        stdout.accept("Created cached result.");

        return "Cached";
    }

    private String persistSession() {
        wait(2000);
        stdout.accept("Created persisted result.");

        return "Persisted";
    }

    private void wait(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ignored) {
        }
    }
}
