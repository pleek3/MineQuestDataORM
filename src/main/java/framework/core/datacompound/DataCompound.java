package framework.core.datacompound;

import framework.core.datasource.DataSourceSettings;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Set;

public class DataCompound {

    private final DataSourceSettings settings;
    private final Set<Class<?>> entityClasses;
    private SessionFactory sessionFactory;

    public DataCompound(DataSourceSettings settings, Set<Class<?>> entityClasses) {
        this.settings = settings;
        this.entityClasses = entityClasses;
    }

    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(this.settings.buildHibernateProperties())
                .build();

        MetadataSources metadataSources = new MetadataSources(registry);

        for (Class<?> entityClass : this.entityClasses) {
            metadataSources.addAnnotatedClass(entityClass);
            System.out.println("Added " + entityClass.getSimpleName() + " class to Hibernate metadata");
        }

        sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
