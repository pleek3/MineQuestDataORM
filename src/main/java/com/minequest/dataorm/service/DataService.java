package com.minequest.dataorm.service;

import com.minequest.dataorm.DataCompound;
import com.minequest.dataorm.EntityMetaDataStorage;
import com.minequest.dataorm.MineQuestDataOrm;
import com.minequest.dataorm.ProviderFactory;
import com.minequest.dataorm.datacompound.DataCompoundFactory;
import com.minequest.dataorm.datacompound.DefaultDataCompoundFactory;
import com.minequest.dataorm.provider.Provider;

public class DataService<E, ID> {

    private final Class<E> entityClass;
    private final EntityMetaDataStorage<E> entityMetaDataStorage;
    private final Provider<E, ID> provider;

    public DataService(Class<E> entityClass) {
        //todo: Instance von DefaultDataCompoundFactory ist halt bullshit. xd
        this(entityClass, new DefaultDataCompoundFactory().createDataCompound(null));

        //todo: EntityCollection() -> Beim Start werden alle @Entities in die Collection geladen. Danach kann man nach belieben darauf zugreifen => Kein Set mehr hin und her schieben => Besser für DataService
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
