package com.minequest.dataorm.service;

import com.minequest.dataorm.DataCompound;
import com.minequest.dataorm.EntityMetaDataStorage;
import com.minequest.dataorm.ProviderFactory;
import com.minequest.dataorm.datacompound.DefaultDataCompoundFactory;
import com.minequest.dataorm.provider.Provider;

public class DataService<E, ID> {
    private final Class<E> entityClass;
    private final EntityMetaDataStorage<E> entityMetaDataStorage;
    private final Provider<E, ID> provider;

    public DataService(Class<E> entityClass) {
        this(entityClass, DefaultDataCompoundFactory.getInstance().createDataCompound());
    }

    public DataService(Class<E> entityClass, DataCompound dataCompound) {
        this.entityClass = entityClass;
        this.entityMetaDataStorage = new EntityMetaDataStorage<>(entityClass);

        //todo: Hier evt. den ServiceRegistry benutzen?
        this.provider = new ProviderFactory<E, ID>().createDefaultProvider(entityMetaDataStorage, dataCompound);
    }

    public AsynchroneDataServiceSupplier<E, ID> createAsyncSupplier() {
        return new AsynchroneDataServiceSupplier<>(this);
    }

    public Provider<E, ID> getProvider() {
        return provider;
    }
}
