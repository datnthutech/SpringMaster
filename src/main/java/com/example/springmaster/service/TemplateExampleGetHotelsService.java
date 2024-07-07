package com.example.springmaster.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateExampleGetHotelsService {
    public List<String> getAll() {
        try {
            return getHotels();
        } catch(Exception e) {
            return List.of();
        }
    }

    public List<String> getHotels() throws InterruptedException, ExecutionException {
        List<String> hotels = tryGetHotels(this::getHotelsFromSource1, 300)
                .thenCompose(result -> {
                    if (result.isEmpty()) {
                        return tryGetHotels(this::getHotelsFromSource2, 300);
                    } else {
                        return CompletableFuture.completedFuture(result);
                    }
                })
                .thenCompose(result -> {
                    if (result.isEmpty()) {
                        return tryGetHotels(this::getHotelsFromSource3, 300);
                    } else {
                        return CompletableFuture.completedFuture(result);
                    }
                })
                .get();

        return hotels;
    }

    private CompletableFuture<List<String>> tryGetHotels(Supplier<CompletableFuture<List<String>>> supplier, int timeout) {
        return supplier.get()
                .completeOnTimeout(List.of(), timeout, TimeUnit.MILLISECONDS);
    }

    private CompletableFuture<List<String>> getHotelsFromSource1() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Giả lập thời gian xử lý
                Thread.sleep(200);
                return Arrays.asList("Hotel1", "Hotel2");
            } catch (InterruptedException e) {
                return List.of();
            }
        });
    }

    private CompletableFuture<List<String>> getHotelsFromSource2() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Giả lập thời gian xử lý
                Thread.sleep(400);
                return Arrays.asList("Hotel3", "Hotel4");
            } catch (InterruptedException e) {
                return List.of();
            }
        });
    }

    private CompletableFuture<List<String>> getHotelsFromSource3() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Giả lập thời gian xử lý
                Thread.sleep(100);
                return Arrays.asList("Hotel5", "Hotel6");
            } catch (InterruptedException e) {
                return List.of();
            }
        });
    }
}
