package com.minequest.dataorm.test;

import com.minequest.dataorm.DataCompound;
import com.minequest.dataorm.service.DataService;

public class TestEntityService extends DataService<TestEntity, Long> {

    public TestEntityService(Class<TestEntity> entityClass, DataCompound dataCompound) {
        super(entityClass, dataCompound);
    }
}
