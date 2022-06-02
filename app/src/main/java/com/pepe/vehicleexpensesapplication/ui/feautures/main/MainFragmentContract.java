package com.pepe.vehicleexpensesapplication.ui.feautures.main;

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
