package com.minequest.dataorm;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.util.Set;

;

public class DataCompound {

    private final DataSourceSettings settings;
    private final Set<Class<?>> entityClasses;
    private SessionFactory sessionFactory;

    public DataCompound(DataSourceSettings settings, Set<Class<?>> entityClasses) {
        this.settings = settings;
        this.entityClasses = entityClasses;
    }

    public void setup() throws IOException {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(settings.buildHibernateProperties())
                .build();

        MetadataSources metadataSources = new MetadataSources(registry);

        for (Class<?> entityClass : entityClasses) {
            metadataSources.addAnnotatedClass(entityClass);
            System.out.println("Added " + entityClass.getSimpleName() + " class to Hibernate metadata");
        }

        sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
