package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.adapters.HistoryAdapter;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.mapper.HistoryItemMapper;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter {

    private static final String HISTORY_FR_PRESENTER_TAG = "HISTORY_FR_PRESENTER_TAG";

    private HistoryContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    private long itemId;

    private List<HistoryUIModel> parsedItems;
    private final FirebaseHelper.FirebaseHistoryListener firebaseHistoryListener = new FirebaseHelper.FirebaseHistoryListener() {
        @Override
        public void onHistoryItemsLoaded(List<HistoryItemModel> items) {

            parsedItems = new ArrayList<>();
            view.setHistoryItems(parsedItems, items, historyDeleteListener);
            for (int i = 0; i < items.size(); i++) {

                if (i != items.size() - 1) {
                    Log.w(HISTORY_FR_PRESENTER_TAG, "striong builder DATE: ");
                    parsedItems.add(HistoryItemMapper.mapToUiModel(items.get(i), items.get(i + 1).currentMileage));
                    Log.w(HISTORY_FR_PRESENTER_TAG, "parsed Items  >   1 SIZE: " + parsedItems.size());
                    view.setHistoryItems(parsedItems, items, historyDeleteListener);
                } else {
                    Log.w(HISTORY_FR_PRESENTER_TAG, "striong builder DATE: ");
                    Log.w(HISTORY_FR_PRESENTER_TAG, "BEFORE ADD parsed Items < 2 SIZE: " + parsedItems.size());

                    parsedItems.add(HistoryItemMapper.mapToUiModel(items.get(i)));

                    view.setHistoryItems(parsedItems, items, historyDeleteListener);
                }
            }

        }
    };

    private HistoryAdapter.DeleteItemListener historyDeleteListener = new HistoryAdapter.DeleteItemListener() {
        @Override
        public void onClick(long itemID) {
            showDeleteDialog();
            itemId = itemID;
        }
    };

    private FirebaseHelper.FirebaseSuccessListener firebaseSuccessListener = new FirebaseHelper.FirebaseSuccessListener() {
        @Override
        public void successStatus(boolean success) {
            if (success) {
                view.showToast("Delete succeed!");
            } else {
                view.showToast("Something goes wrong!");
            }
        }
    };

    private void showDeleteDialog() {
        view.showDeleteItemDialog();
    }

    @Override
    public void deleteItemConfirmed() {
        deleteItem();
    }

    private void deleteItem() {
        if (itemId != 0) {
            String historyItemID = String.valueOf(itemId);
            firebaseHelper.deleteHistoryItem(historyItemID);
        } else {
            view.showToast("Something goes wrong!");
        }
    }

    public HistoryPresenter(HistoryContract.View view, SharedPrefsHelper prefsHelper) {
        this.view = view;
        sharedPrefsHelper = prefsHelper;
        firebaseHelper = FirebaseHelper.getInstance();
    }

    @Override
    public void onViewCreated() {
        try {
            view.setHistoryFragmentToolbar();
            firebaseHelper.setFirebaseListener(firebaseHistoryListener);
            firebaseHelper.setFirebaseSuccessListener(firebaseSuccessListener);
            firebaseHelper.getHistoryItems();

        } catch (Exception e) {
            Log.w(HISTORY_FR_PRESENTER_TAG, "On View Created EXCEPTION CAPTURED: " + e);
        }
    }

    @Override
    public void onFloatingRefillButtonClicked() {
        view.startRefillActivity();
    }

    @Override
    public boolean checkIsAnonymous() {
        return sharedPrefsHelper.getIsAnonymous();
    }
}

