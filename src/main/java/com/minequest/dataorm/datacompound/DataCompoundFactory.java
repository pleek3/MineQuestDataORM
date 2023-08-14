package com.minequest.dataorm.datacompound;

import com.minequest.dataorm.DataCompound;

public interface DataCompoundFactory {

    DataCompound createDataCompound();

    DataCompound createCustomDataCompound(String settingsName);
}
