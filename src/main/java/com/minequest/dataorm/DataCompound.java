package com.minequest.dataorm;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@Getter
public class DataCompound {

  private final DataSourceSettings settings;
  private final Set<Class<?>> entityClasses;


  private SessionFactory sessionFactory;

  public DataCompound(DataSourceSettings settings, Set<Class<?>> entityClasses) {
    this.settings = settings;
    this.entityClasses = entityClasses;
  }

  public void setup() throws IOException {
    Configuration configuration = new Configuration();
    Properties properties = new Properties();

    properties.load(DataCompound.class.getResourceAsStream("/" + this.settings.getDataSourceType().getConfigName()));
    properties.put("hibernate.show_sql", this.settings.isDebug());

    properties.put("hibernate.hbm2ddl.auto", this.settings.getDataSourceSchemaRule().getRuleName());
    properties.put("hibernate.connection.username", this.settings.getUser());
    properties.put("hibernate.connection.password", this.settings.getPassword());

    switch (this.settings.getDataSourceType()) {
      case POSTGRES -> {
        String addressString = this.settings.buildAddressString(",");
        properties.put("hibernate.connection.url", "jdbc:postgresql://" + addressString + "/" + this.settings.getDb() +
            "?loadBalancePingTimeout=1500&amp;loadBalanceBlacklistTimeout=7000&amp;autoReconnect=true&amp;failOverReadOnly=false&amp;roundRobinLoadBalance=true");
      }

      case MYSQL -> {
        throw new RuntimeException("not implemented yet");
      }
    }

    for (Class<?> entityClass : this.entityClasses) {
      configuration.addAnnotatedClass(entityClass);
    }

    BootstrapServiceRegistry bootstrapServiceRegistry = new BootstrapServiceRegistryBuilder().applyIntegrator(new HibernateListenerIntegrator(this)).build();
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder(bootstrapServiceRegistry).applySettings(properties).build();
    this.sessionFactory = configuration.buildSessionFactory(registry);
  }


}
