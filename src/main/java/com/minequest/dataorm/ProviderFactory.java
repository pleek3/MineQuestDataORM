package com.minequest.dataorm;

import com.minequest.dataorm.provider.HibernateProviderImpl;
import com.minequest.dataorm.provider.Provider;

public class ProviderFactory<E, ID> {

    public Provider<E, ID> createDefaultProvider(EntityMetaDataStorage<E> metaDataStorage, DataCompound compound) {
        return new HibernateProviderImpl<>(metaDataStorage, compound);
    }

    //todo: implement method to create custom provider
}
