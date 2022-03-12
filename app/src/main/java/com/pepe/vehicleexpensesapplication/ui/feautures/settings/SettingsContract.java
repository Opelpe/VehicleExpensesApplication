package com.pepe.vehicleexpensesapplication.ui.feautures.settings;

import com.pepe.vehicleexpensesapplication.data.adapters.SettingsUiModel;

import java.util.List;

public interface SettingsContract {
    interface View{

    }

    interface Presenter{

        void onViewCreated();

        List<SettingsUiModel> getRVItems();
    }
}
