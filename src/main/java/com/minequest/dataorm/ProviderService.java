package com.minequest.dataorm;

import com.minequest.dataorm.provider.Provider;

import java.util.HashMap;
import java.util.Map;

public class ProviderService {

    private final Map<String, Provider<?, ?>> registeredProviders = new HashMap<>();

    public ProviderService() {

    }

    public void registerCustomProvider(String name, Provider<?, ?> provider) {
        this.registeredProviders.put(name, provider);
    }

    public <E, ID> Provider<E, ID> supplyDefaultProvider() {
        @SuppressWarnings("unchecked")
        Provider<E, ID> provider = (Provider<E, ID>) this.registeredProviders.get("default");

        if (provider == null) {
            throw new RuntimeException("No default provider registered");
        }

        return provider;
    }

}
