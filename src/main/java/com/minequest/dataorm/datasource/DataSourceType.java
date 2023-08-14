package com.minequest.dataorm.datasource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DataSourceType {
  H2_MEM("hibernate_h2.properties"),
  H2_FILE("hibernate_h2.properties"),
  POSTGRES("hibernate_postgres.properties"),
  MYSQL("hibernate_mysql.properties"),
  ;

  private final String configName;


}
