package com.minequest.dataorm;

import com.minequest.serviceregistry.api.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Service
public class EntityManagerService {

  private final Map<String, EntityManager> createdEntityManagers = new HashMap<>();

  public EntityManager getEntityManager(String context) {
    EntityManager entityManager = createdEntityManagers.get(context);
    if (entityManager == null) {
      throw new RuntimeException("EntityManager " + context + " was not created!");
    }
    return entityManager;
  }

  public EntityManager createEntityManager(String context, DataSourceSettings settings) {
    EntityManager entityManager = createdEntityManagers.get(context);
    if (entityManager != null) {
      return entityManager;
    }

    EntityManagerFactory factory = settings.buildEntityManagerFactory();
    entityManager = factory.createEntityManager();
    createdEntityManagers.put(context, entityManager);
    return entityManager;
  }

}
