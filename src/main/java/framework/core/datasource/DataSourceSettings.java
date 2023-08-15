package framework.core.datasource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQLDialect;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@AllArgsConstructor
@NoArgsConstructor
@Data
public final class DataSourceSettings implements Serializable {

    @JsonIgnore
    private static final String CONFIG_NAME = "database.json";

    @JsonProperty(value = "serverAddresses")
    private List<ServerAddress> serverAddresses;

    @JsonProperty(value = "db")
    private String db;

    @JsonProperty(value = "user")
    private String user;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "debug")
    private boolean debug;

    @JsonProperty(value = "timings")
    private boolean timings;

    @JsonProperty(value = "dataSourceType")
    private DataSourceType dataSourceType;

    @JsonProperty(value = "dataSourceSchemaRule")
    private DataSourceSchemaRule dataSourceSchemaRule;

    @JsonProperty(value = "numThreads")
    private int numThreads;

    @JsonIgnore
    public static DataSourceSettings createTemplate() {
        return new DataSourceSettings(Collections.singletonList(new ServerAddress("localhost", 5432)),
                "test", "user", "", true, false, DataSourceType.MYSQL, DataSourceSchemaRule.CREATE, 4);
    }

    @JsonIgnore
    public Properties buildHibernateProperties() {
        Properties properties = new Properties();

        properties.setProperty(AvailableSettings.DIALECT, determineHibernateDialect().getName());
        properties.setProperty(AvailableSettings.SHOW_SQL, String.valueOf(debug));
        properties.setProperty(AvailableSettings.HBM2DDL_AUTO, dataSourceSchemaRule.getRuleName());
        properties.setProperty("javax.persistence.jdbc.user", user);
        properties.setProperty("javax.persistence.jdbc.password", password);

        return properties;
    }

    @JsonIgnore
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

    @JsonIgnore
    public String getConfigName() {
        return CONFIG_NAME;
    }

    @JsonIgnore
    public String getFormattedServerAddresses(String separator) {
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
    @NoArgsConstructor
    @Data
    public static class ServerAddress implements Serializable {
        @JsonProperty(value = "host")
        private String host;
        @JsonProperty(value = "port")
        private int port;
    }
}
