package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import com.pepe.vehicleexpensesapplication.data.model.HistoryItemModel;

import java.util.List;

public interface HistoryContract {

    interface View {

        void startRefillActivity();

        void setHistoryFragmentToolbar();

        void setHistoryItems(List<HistoryItemModel> parsedItems);

    }

    interface Presenter {

        void onViewCreated();

        void onFloatingRefillButtonClicked();

        boolean checkIsAnonymous();
    }
}
