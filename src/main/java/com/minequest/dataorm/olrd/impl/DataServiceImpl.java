package com.minequest.dataorm.olrd.impl;

import com.minequest.dataorm.olrd.api.DataService;

public class DataServiceImpl implements DataService {
  @Override
  public void performTransaction() {
    Runnable runnable = () -> System.out.println("hello world");
    runnable.run();
  }
}
