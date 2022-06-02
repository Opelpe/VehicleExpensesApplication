package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;

import java.util.List;

public interface HistoryContract {

    interface View {

        void startRefillActivity();

        void setHistoryFragmentToolbar();

        void setHistoryItems(List<HistoryUIModel> parsedItems);

    }

    interface Presenter {

        void onViewCreated();

        void onFloatingRefillButtonClicked();

        boolean checkIsAnonymous();
    }
}
