package com.sdtech.example;

import io.vavr.concurrent.Future;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class AsyncRequestsDemo {
    private static final Consumer<String> stdout = System.out::println;

    public static void main(String[] args) {
        new AsyncRequestsDemo().run();
    }

    private void run() {
        try {
            var requests = asList(
                    Future.of(this::cacheSession),
                    Future.of(this::persistSession)
            );

            var result = Future.firstCompletedOf(requests).get();

            stdout.accept(result + " completed first");
        } finally {
            ForkJoinPool.commonPool().awaitQuiescence(15, TimeUnit.SECONDS);
        }
    }

    private String cacheSession() {
        wait(1000);
        stdout.accept("Created cached result.");

        return "cached";
    }

    private String persistSession() {
        wait(2000);
        stdout.accept("Created persisted result.");

        return "persisted";
    }

    private void wait(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ignored) {
        }
    }
}
