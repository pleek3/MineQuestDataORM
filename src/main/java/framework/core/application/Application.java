package framework.core.application;

import framework.annotations.Component;

@Component
public class Application {

    public static ConfigurableApplicationContext run() {
        ConfigurableApplicationContext context = new ConfigurableApplicationContext();
        return context;
    }
}
