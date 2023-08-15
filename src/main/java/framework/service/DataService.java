package framework.service;

import framework.core.datacompound.DataCompound;
import framework.entity.EntityMetaDataStorage;
import framework.provider.ProviderFactory;
import framework.provider.Provider;

public class DataService<E, ID> {

    private final Class<E> entityClass;
    private final EntityMetaDataStorage<E> entityMetaDataStorage;
    private final Provider<E, ID> provider;

    public DataService(Class<E> entityClass, DataCompound dataCompound) {
        this.entityClass = entityClass;
        this.entityMetaDataStorage = new EntityMetaDataStorage<>(entityClass);
        this.provider = new ProviderFactory<E, ID>().createDefaultProvider(entityMetaDataStorage, dataCompound);
    }

    public AsynchroneDataServiceSupplier<E, ID> createAsyncSupplier() {
        return new AsynchroneDataServiceSupplier<>(this);
    }

    public Provider<E, ID> getProvider() {
        return provider;
    }
}
