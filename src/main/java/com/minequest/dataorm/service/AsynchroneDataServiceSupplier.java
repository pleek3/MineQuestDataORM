package com.minequest.dataorm.service;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Getter
public class AsynchroneDataServiceSupplier<E, ID> {

    private final DataService<E, ID> dataService;

    public AsynchroneDataServiceSupplier(DataService<E, ID> service) {
        this.dataService = service;
    }

    public void performAsynchrone(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }

    public <T> CompletableFuture<T> supplyAsynchrone(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    public CompletableFuture<List<E>> findAll() {
        Supplier<List<E>> findEntitySupplier = () -> getDataService().getProvider().findAll();
        return supplyAsynchrone(findEntitySupplier);
    }

}
