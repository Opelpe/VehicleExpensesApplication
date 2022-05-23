package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.pepe.vehicleexpensesapplication.data.model.HistoryItemModel;

import java.util.List;

public interface MainFragmentContract {
    interface View{

        void setMainFragmentToolbar();

        void startMyMainActivity();

        void startRefillActivity();

        void setAverageUsageText(String averageUsage);

        void setTravelingCostText(String travelingCost);

        void setLatestRefillView(String addedMileage, String fuelUsage, String fuelCost);

        void setTotalCostsText(String mileage, String money, String volume);
    }

    interface Presenter{

        void onViewCreated();

        void onRefillButtonClicked();

        boolean checkIsAnonymous();

        boolean checkGoogleSignIn();
    }
}
