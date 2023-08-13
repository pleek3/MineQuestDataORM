package com.minequest.dataorm;

import com.minequest.dataorm.olrd.api.datasource.DataSourceSchemaRule;
import com.minequest.dataorm.olrd.api.datasource.DataSourceType;
import jakarta.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public record DataSourceSettings(List<ServerAddress> serverAddresses, String db,
                                 String user, String password, boolean debug, boolean timings,
                                 DataSourceType dataSourceType, DataSourceSchemaRule dataSourceSchemaRule,
                                 int numThreads) {

  private static final String CONFIG_NAME = "database.json";

  public DataSourceSettings(List<ServerAddress> serverAddresses, String db, String user, String password, boolean debug,
                            boolean timings, DataSourceType dataSourceType, DataSourceSchemaRule dataSourceSchemaRule, int numThreads) {
    this.serverAddresses = Collections.unmodifiableList(serverAddresses);
    this.db = db;
    this.user = user;
    this.password = password;
    this.debug = debug;
    this.timings = timings;
    this.dataSourceType = dataSourceType;
    this.dataSourceSchemaRule = dataSourceSchemaRule;
    this.numThreads = numThreads;
  }

  public static DataSourceSettings createTemplate() {
    return new DataSourceSettings(Collections.singletonList(new ServerAddress("localhost", 5432)),
        "legend", "user", "", true, false, DataSourceType.MYSQL, DataSourceSchemaRule.CREATE, 4);
  }

  public EntityManagerFactory buildEntityManagerFactory() {
    throw new RuntimeException("currently not implemented.");
  }

  public String getConfigName() {
    return CONFIG_NAME;
  }

  public String buildAddressString(String separator) {
    StringBuilder builder = new StringBuilder();

    int addresses = this.serverAddresses.size();
    if (addresses == 0) {
      throw new RuntimeException("No server addresses specified!");
    }

    for (ServerAddress address : this.serverAddresses) {
      builder.append(address.getHost()).append(":").append(address.getPort());
      if (addresses > 1) {
        builder.append(separator);
      }
    }

    return builder.toString();
  }

  public List<ServerAddress> getServerAddresses() {
    return this.serverAddresses;
  }

  public record ServerAddress(String host, int port) {

    public String getHost() {
      return host;
    }

    public int getPort() {
      return port;
    }
  }
}