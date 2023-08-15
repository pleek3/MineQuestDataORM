package framework.core.datacompound;

import framework.core.datasource.DataSourceSettings;
import framework.core.datasource.DataSourceSettingsManager;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(force = true)
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
