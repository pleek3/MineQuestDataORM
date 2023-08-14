package com.minequest.dataorm;

import com.minequest.dataorm.datasource.DataSourceSchemaRule;
import com.minequest.dataorm.datasource.DataSourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQLDialect;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

@AllArgsConstructor
@Data
public final class DataSourceSettings {

    private static final String CONFIG_NAME = "database.json";
    private final List<ServerAddress> serverAddresses;
    private final String db;
    private final String user;
    private final String password;
    private final boolean debug;
    private final boolean timings;
    private final DataSourceType dataSourceType;
    private final DataSourceSchemaRule dataSourceSchemaRule;
    private final int numThreads;

    public static DataSourceSettings createTemplate() {
        return new DataSourceSettings(Collections.singletonList(new ServerAddress("localhost", 5432)),
                "test", "user", "", true, false, DataSourceType.MYSQL, DataSourceSchemaRule.CREATE, 4);
    }

    public Properties buildHibernateProperties() {
        Properties properties = new Properties();

        properties.setProperty(AvailableSettings.DIALECT, determineHibernateDialect().getName());
        properties.setProperty(AvailableSettings.SHOW_SQL, String.valueOf(debug));
        properties.setProperty(AvailableSettings.HBM2DDL_AUTO, dataSourceSchemaRule.getRuleName());
        properties.setProperty("javax.persistence.jdbc.user", user);
        properties.setProperty("javax.persistence.jdbc.password", password);

        return properties;
    }

    private Class<? extends Dialect> determineHibernateDialect() {
        switch (dataSourceType) {
            case POSTGRES:
                return PostgreSQLDialect.class;
            case MYSQL:
                return MySQLDialect.class;
            default:
                throw new UnsupportedOperationException("Unsupported DataSourceType: " + dataSourceType);
        }
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

    @AllArgsConstructor
    @Data
    public static class ServerAddress {
        private final String host;
        private final int port;
    }
}
