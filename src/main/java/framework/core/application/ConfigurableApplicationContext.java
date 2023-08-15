package framework.core.application;

import framework.entity.EntityClassCollector;
import framework.core.datacompound.DataCompoundFactory;
import framework.core.datacompound.DefaultDataCompoundFactory;
import framework.service.DataServiceRegistry;
import lombok.Getter;

@Getter
public class ConfigurableApplicationContext implements ApplicationContext {

    private DataCompoundFactory dataCompoundFactory;
    private DataServiceRegistry dataServiceRegistry;
    private EntityClassCollector entityClassCollector;
    private String basePackage;


    //todo: add listeners
    public ConfigurableApplicationContext() {
        setBasePackagePath("");
        setCompoundFactory(new DefaultDataCompoundFactory());
        initializeDataServiceRegistry();
        initializeAndCollectEntityClasses();

        //todo: listeners.run();
    }

    private void initializeAndCollectEntityClasses() {
        this.entityClassCollector = new EntityClassCollector();
        this.entityClassCollector.findEntityClasses(this.basePackage);
    }

    private void initializeDataServiceRegistry() {
        this.dataServiceRegistry = new DataServiceRegistry(this.dataCompoundFactory);
    }

    public ConfigurableApplicationContext setBasePackagePath(String value) {
        this.basePackage = value;
        return this;
    }

    public ConfigurableApplicationContext setCompoundFactory(DataCompoundFactory dataCompoundFactory) {
        this.dataCompoundFactory = dataCompoundFactory;
        return this;
    }
}
