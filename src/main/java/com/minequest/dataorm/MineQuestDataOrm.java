package com.minequest.dataorm;

import com.minequest.dataorm.datacompound.DataCompoundFactory;
import com.minequest.dataorm.datacompound.DefaultDataCompoundFactory;
import com.minequest.dataorm.dataservice.DataServiceRegistry;
import com.minequest.dataorm.service.DataService;
import com.minequest.dataorm.test.TestEntity;
import com.minequest.dataorm.utils.ClassUtils;
import jakarta.persistence.Entity;

import java.io.IOException;
import java.util.Set;

public class MineQuestDataOrm {

    private static ProviderService providerService;

    public static ProviderService providerService() {
        if (providerService == null)
            providerService = new ProviderService();
        return providerService;
    }

    public static void main(String[] args) {
        MineQuestDataOrm mineQuestDataOrm = new MineQuestDataOrm("com.minequest.dataorm");
    }

    public MineQuestDataOrm(String basePackage) {
        Set<Class<?>> entityClasses = findEntityClasses(basePackage);

        DataCompoundFactory compoundFactory = new DefaultDataCompoundFactory(entityClasses);
        DataCompound compound = compoundFactory.createDataCompound();

        try {
            compound.setup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DataServiceRegistry dataServiceRegistry = new DataServiceRegistry(compoundFactory);
        DataService<TestEntity, Long> testEntityService = dataServiceRegistry.registerDataService(TestEntity.class);

        dataServiceRegistry.registerDataService(TestEntity.class);

    }

    private Set<Class<?>> findEntityClasses(String basePackage) {
        return ClassUtils.findAllAnnotatedClasses(basePackage, Entity.class);
    }
}
