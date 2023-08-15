package framework.core.datasource;

import java.util.HashMap;
import java.util.Map;

public class DataSourceSettingsManager {
    private static final Map<String, DataSourceSettings> settingsMap = new HashMap<>();

    public static void registerDataSourceSettings(String key, DataSourceSettings settings) {
        settingsMap.put(key, settings);
    }

    public static DataSourceSettings getDataSourceSettings(String key) {
        DataSourceSettings settings = settingsMap.get(key);

        if (settings == null) {
            settings = settingsMap.get("default");
        }

        return settings;
    }
}