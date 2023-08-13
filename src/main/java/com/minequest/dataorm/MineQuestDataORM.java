package com.minequest.dataorm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minequest.dataorm.annotations.DataEntity;
import com.minequest.dataorm.annotations.DataRepository;
import com.minequest.dataorm.utils.ClassUtils;
import com.minequest.dataorm.utils.FileUtils;
import com.minequest.serviceregistry.api.ServiceWrapper;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class MineQuestDataORM {
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final String basePackagePath;
  private final ServiceWrapper serviceWrapper = new ServiceWrapper();

  private DataSourceSettings dataSourceSettings;

  public MineQuestDataORM() {
    this("");

    this.dataSourceSettings = DataSourceSettings.createTemplate();
  }

  public MineQuestDataORM(String basePath) {
    this.basePackagePath = basePath;
    this.dataSourceSettings = createDataSourceSettings("test", "helloworld", "database.json");


    /*
    getDataService().
     */

    registerServices();
  }

  private void registerServices() {
    this.serviceWrapper.registerService(EntityManagerService.class);
  }

  private void scanPackagesForDataEntityClasses() {
    Set<Class<?>> classes = ClassUtils.findAllAnnotatedClasses(this.basePackagePath, DataEntity.class);

    if (classes.isEmpty()) {
      return;
    }

    //todo: register entity classes
  }

  private void scanPackagesForRepositoryClasses() {
    Set<Class<?>> classes = ClassUtils.findAllAnnotatedClasses(this.basePackagePath, DataRepository.class);

    if (classes.isEmpty()) {
      return;
    }



  }


  private DataSourceSettings createDataSourceSettings(String... parts) {
    DataSourceSettings dataSourceSettings;

    try {
      File file = FileUtils.getOrCreateFile(parts);
      dataSourceSettings = this.objectMapper.readValue(file, DataSourceSettings.class);

    } catch (IOException e) {
      dataSourceSettings = DataSourceSettings.createTemplate();
      e.printStackTrace();
    }


    return dataSourceSettings;
  }


}
