package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import com.pepe.vehicleexpensesapplication.data.adapters.HistoryAdapter;
import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;

import java.util.List;

public interface HistoryContract {

    interface View {

        void startRefillActivity();

        void setHistoryFragmentToolbar();

        void setHistoryItems(List<HistoryUIModel> parsedItems, List<HistoryItemModel> items, HistoryAdapter.DeleteItemListener deleteItemListener);

        void showDeleteItemDialog();

        void showToast(String toastMsg);
    }

    interface Presenter {

        void onViewCreated();

        void onFloatingRefillButtonClicked();

        boolean checkIsAnonymous();

        void deleteItemConfirmed();

    }
}
