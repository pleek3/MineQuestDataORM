package com.minequest.dataorm.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class TestProviderImpl<E, ID> implements Provider<E, ID> {

    public final List<E> entities = new ArrayList<>();

    @Override
    public List<E> findAll() {
        return entities;
    }

    @Override
    public Optional<E> save(E entity) {
        this.entities.add(entity);
        return Optional.of(entity);
    }
}
