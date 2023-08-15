package framework.provider;

import framework.core.datacompound.DataCompound;
import framework.entity.EntityMetaDataStorage;

public class ProviderFactory<E, ID> {

    public Provider<E, ID> createDefaultProvider(EntityMetaDataStorage<E> metaDataStorage, DataCompound compound) {
        return new HibernateProviderImpl<>(metaDataStorage, compound);
    }

    //todo: implement method to create custom provider
}
