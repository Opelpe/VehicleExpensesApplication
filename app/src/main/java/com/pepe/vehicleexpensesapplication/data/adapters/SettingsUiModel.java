package com.pepe.vehicleexpensesapplication.data.adapters;

public class SettingsUiModel {

    String title;
    Type type;

    public SettingsUiModel(String title, Type type) {
        this.title = title;
        this.type = type;
    }

    public enum Type {
        SectionHeader,
        NoDataHeader,
        WithDataHeader

    }
}
