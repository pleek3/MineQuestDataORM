package com.minequest.dataorm.olrd.api.datasource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DataSourceSchemaRule {
  VALIDATE("validate"),
  UPDATE("update"),
  CREATE("create"),
  CREATE_DROP("create-drop"),
  NONE("none");

  private final String ruleName;
}
