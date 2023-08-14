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

    @Override
    public DataCompound createDataCompound(Set<Class<?>> entityClasses) {
        DataSourceSettings settings = DataSourceSettingsManager.getDataSourceSettings("default");
        return new DataCompound(settings, entityClasses);
    }

    @Override
    public DataCompound createCustomDataCompound(String settingsName, Set<Class<?>> entityClasses) {
        DataSourceSettings settings = DataSourceSettingsManager.getDataSourceSettings(settingsName);
        return new DataCompound(settings, entityClasses);
    }
}
