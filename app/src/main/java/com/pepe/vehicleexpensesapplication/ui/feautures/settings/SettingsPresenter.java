package com.pepe.vehicleexpensesapplication.ui.feautures.settings;

import android.content.Context;

import com.pepe.vehicleexpensesapplication.data.adapters.SettingsUiModel;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class SettingsPresenter implements SettingsContract.Presenter{

    private SettingsContract.View view;

    private List<SettingsUiModel> settingsUiModelList = new ArrayList<>();

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    public SettingsPresenter(SettingsContract.View view, Context context){
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(context);
        firebaseHelper = FirebaseHelper.getInstance(context);
    }

    @Override
    public boolean getIsAnonymous() {
        return sharedPrefsHelper.getIsAnonymous();
    }

    @Override
    public void onViewCreated() {
        setSettingsList();
    }

    private void setSettingsList() {
        settingsUiModelList.add(new SettingsUiModel("SETTINGS", SettingsUiModel.Type.SectionHeader));
        settingsUiModelList.add(new SettingsUiModel("SHOP", SettingsUiModel.Type.NoDataHeader));
        settingsUiModelList.add(new SettingsUiModel("SYNCHRONIZATION", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("LANGUAGE", SettingsUiModel.Type.NoDataHeader));
        settingsUiModelList.add(new SettingsUiModel("CURRENCY", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("MILEAGE UNITS", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("CAPACITY UNITS", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("AVERAGE USAGE UNITS", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("DATE FORMAT", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("PRIVACY POLITICS", SettingsUiModel.Type.NoDataHeader));
        settingsUiModelList.add(new SettingsUiModel("CHECK ABOUT APP", SettingsUiModel.Type.NoDataHeader));
        settingsUiModelList.add(new SettingsUiModel("TEST", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("SCROLLTEST", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("SCROLLTEST2", SettingsUiModel.Type.WithDataHeader));
        settingsUiModelList.add(new SettingsUiModel("SCROLLTEST3", SettingsUiModel.Type.NoDataHeader));
    }

    @Override
    public List<SettingsUiModel> getRVItems() {
        return settingsUiModelList;
    }

    @Override
    public void checkSynchronization() {
        if (sharedPrefsHelper.getIsAnonymous()){
            view.startLoginActivity();
        }else {
            view.startLogOutActivity();
        }
    }
}
