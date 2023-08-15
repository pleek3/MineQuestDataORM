package framework.core.datacompound;

import java.util.Set;

public interface DataCompoundFactory {

    DataCompound createDataCompound(Set<Class<?>> entityClasses);

    DataCompound createCustomDataCompound(String settingsName, Set<Class<?>> entityClasses);
}
