package com.minequest.dataorm.dataservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minequest.dataorm.DataCompound;
import com.minequest.dataorm.DataSourceSettings;
import com.minequest.dataorm.DataSourceSettingsManager;
import com.minequest.dataorm.datacompound.DataCompoundFactory;
import com.minequest.dataorm.service.DataService;
import com.minequest.dataorm.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class DataServiceRegistry {
    private final DataCompoundFactory dataCompoundFactory;

    public DataServiceRegistry(DataCompoundFactory dataCompoundFactory) {
        this.dataCompoundFactory = dataCompoundFactory;

        DataSourceSettings defaultSettings = createDefaultDataSourceSettings();
        DataSourceSettingsManager.registerDataSourceSettings("default", defaultSettings);
    }

    public <E, ID> DataService<E, ID> registerDataService(Class<E> entityClass) {
        DataCompound dataCompound = dataCompoundFactory.createDataCompound();
        return new DataService<>(entityClass, dataCompound);
    }

    private DataSourceSettings createDefaultDataSourceSettings() {
        try {
            File configFile = FileUtils.getOrCreateFileInResourceFolder("database.json");

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(configFile, DataSourceSettings.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while retrieving DataSourceSettings from config file", e);
        }
    }

}
