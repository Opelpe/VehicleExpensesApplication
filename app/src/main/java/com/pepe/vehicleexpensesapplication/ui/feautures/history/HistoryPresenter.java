package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;

public class HistoryPresenter implements HistoryContract.Presenter {

    private static final String HISTORY_FR_PRESENTER_TAG = "HISTORY_FR_PRESENTER_TAG";

    private HistoryContract.View view;

    private FirebaseHelper firebaseHelper;

    public HistoryPresenter(HistoryContract.View view) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
    }


    @Override
    public void onViewCreated() {
        try {
            Log.w(HISTORY_FR_PRESENTER_TAG, "handle PROVIDER: START");
            if (firebaseHelper.getUid() != null) {
                Log.w(HISTORY_FR_PRESENTER_TAG, "handle PROVIDER: IF");
                FirebaseUser user = firebaseHelper.getCurrentUser();
                String uID = user.getUid();

                firebaseHelper.firestoreUIDProvidersCallback().whereEqualTo("UID", firebaseHelper.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.w(HISTORY_FR_PRESENTER_TAG, "handle PROVIDER: START before task.isSuccess");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                String provider = doc.get("Provider").toString();
                                Log.w(HISTORY_FR_PRESENTER_TAG, "handle PROVIDER: " + provider);

                                if (provider.equals("Anonymously")) {
                                    view.setSynchornizationImageViewOff();
                                }else {
                                    view.setSynchornizationImageViewOn();
                                }


                            }
                        }
                    }
                });

            }

        } catch (Exception e) {
            Log.w(HISTORY_FR_PRESENTER_TAG, "handle PROVIDER EXCEPTION CAPTURED:: " + e);
        }
    }
}
