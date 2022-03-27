package com.pepe.vehicleexpensesapplication.ui.feautures.settings;

import com.pepe.vehicleexpensesapplication.data.adapters.SettingsUiModel;

import java.util.List;

public interface SettingsContract {
    interface View{

        void startLogOutActivity();

        void startLoginActivity();
    }

    interface Presenter{

        void onViewCreated();

        List<SettingsUiModel> getRVItems();

        boolean getIsAnonymous();

        void checkSynchronization();
    }
}
