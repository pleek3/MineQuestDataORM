package framework.core.application;

import framework.entity.EntityClassCollector;
import framework.core.datacompound.DataCompoundFactory;
import framework.core.datacompound.DefaultDataCompoundFactory;
import framework.listener.DefaultEventManager;
import framework.listener.EventManager;
import framework.listener.model.StandardEvent;
import framework.service.DataServiceRegistry;
import lombok.Getter;

/**
 * A configurable application context that manages various components and services.
 * Allows customization and initialization of components within the application.
 */
@Getter
public class ConfigurableApplicationContext implements ApplicationContext {

    private DataCompoundFactory dataCompoundFactory;
    private DataServiceRegistry dataServiceRegistry;
    private EntityClassCollector entityClassCollector;
    private EventManager eventManager;
    private String basePackage;

    /**
     * Constructs a new ConfigurableApplicationContext with default settings.
     * Initializes the data compound factory, data service registry, entity class collector,
     * and dispatches the AFTER_CONTEXT_INITIALIZED event.
     */
    public ConfigurableApplicationContext() {
        setBasePackagePath("");
        setCompoundFactory(new DefaultDataCompoundFactory());
        setEventManager(new DefaultEventManager());
        initializeDataServiceRegistry();
        initializeAndCollectEntityClasses();

        getEventManager().dispatch(StandardEvent.AFTER_CONTEXT_INITIALIZED);
    }

    private void initializeAndCollectEntityClasses() {
        this.entityClassCollector = new EntityClassCollector();
        this.entityClassCollector.findEntityClasses(this.basePackage);
    }

    private void initializeDataServiceRegistry() {
        this.dataServiceRegistry = new DataServiceRegistry(this.dataCompoundFactory);
    }

    /**
     * Sets the base package path for scanning components.
     *
     * @param value The base package path to set.
     * @return The updated ConfigurableApplicationContext.
     */
    public ConfigurableApplicationContext setBasePackagePath(String value) {
        this.basePackage = value;
        return this;
    }

    /**
     * Sets the data compound factory.
     *
     * @param dataCompoundFactory The data compound factory to set.
     * @return The updated ConfigurableApplicationContext.
     */
    public ConfigurableApplicationContext setCompoundFactory(DataCompoundFactory dataCompoundFactory) {
        this.dataCompoundFactory = dataCompoundFactory;
        return this;
    }

    /**
     * Sets the event manager.
     *
     * @param eventManager The event manager to set.
     * @return The updated ConfigurableApplicationContext.
     */
    public ConfigurableApplicationContext setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }
}
