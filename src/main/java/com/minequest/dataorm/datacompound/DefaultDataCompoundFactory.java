package com.minequest.dataorm.datacompound;

import com.minequest.dataorm.DataCompound;
import com.minequest.dataorm.DataSourceSettings;
import com.minequest.dataorm.DataSourceSettingsManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class DefaultDataCompoundFactory implements DataCompoundFactory {

    private final Set<Class<?>> entityClasses;

    // Singleton instance
    private static final DefaultDataCompoundFactory INSTANCE = new DefaultDataCompoundFactory();

    public static DefaultDataCompoundFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public DataCompound createDataCompound() {
        DataSourceSettings settings = DataSourceSettingsManager.getDataSourceSettings("default");
        return new DataCompound(settings, this.entityClasses);
    }

    @Override
    public DataCompound createCustomDataCompound(String settingsName) {
        DataSourceSettings settings = DataSourceSettingsManager.getDataSourceSettings(settingsName);
        return new DataCompound(settings, this.entityClasses);
    }
}
