package framework.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.core.datacompound.DataCompound;
import framework.core.datasource.DataSourceSettings;
import framework.core.datasource.DataSourceSettingsManager;
import framework.core.datacompound.DataCompoundFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * A registry for managing and providing instances of {@link DataService}.
 * This class handles the registration of data services for entity classes,
 * as well as the initialization of default data source settings.
 */
public class DataServiceRegistry {
    private final DataCompoundFactory dataCompoundFactory;

    /**
     * Constructs a new instance of {@link DataServiceRegistry} with the provided {@link DataCompoundFactory}.
     *
     * @param dataCompoundFactory The factory for creating {@link DataCompound} instances.
     */
    public DataServiceRegistry(DataCompoundFactory dataCompoundFactory) {
        this.dataCompoundFactory = dataCompoundFactory;
        //todo: automatisch die DataServices registrieren

        DataSourceSettings defaultSettings = createDefaultDataSourceSettings();
        DataSourceSettingsManager.registerDataSourceSettings("default", defaultSettings);
    }

    /**
     * Registers a data service for the specified entity class.
     *
     * @param <E>         The type of the entity.
     * @param <ID>        The type of the entity's ID.
     * @param entityClass The entity class to register the data service for.
     * @return The registered data service.
     */
    public <E, ID> DataService<E, ID> registerDataService(Class<E> entityClass, DataCompound dataCompound) {
        return new DataService<>(entityClass, dataCompound);
    }

    /**
     * Creates default data source settings by reading from a configuration file.
     *
     * @return The default data source settings.
     * @throws RuntimeException If there is an error while retrieving the settings from the config file.
     */
    private DataSourceSettings createDefaultDataSourceSettings() {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, DataSourceSettings.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while retrieving DataSourceSettings from config file", e);
        }
    }

}
