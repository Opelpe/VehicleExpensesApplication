package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class NewAccountPresenter implements NewAccountContract.Presenter {

    private static final String NACC_PRESENTER_TAG = "NACC_PRESENTER_TAG";

    private NewAccountContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private GoogleSignInClient googleSignInClient;

    private FirebaseHelper firebaseHelper;


    public NewAccountPresenter(NewAccountContract.View view, Context applicationContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(applicationContext);
        sharedPrefsHelper = new SharedPrefsHelper(applicationContext);
    }

    @Override
    public void onGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient) {
        googleSignInClient = mGoogleSignInClient;

        signIn();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        view.presenterStartActivityForResult(signInIntent, ConstantsPreferences.RC_SIGN_IN);
    }

    @Override
    public void onEmailSignButtonClicked() {
        view.startEmailAccountActivity();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult START");
        try {
            view.showLoadingGoogleDialog();
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult completed TASK: " + account.getId() + " \n" + account.getEmail());

            String googleEmail = account.getEmail();

            AuthCredential credential = firebaseHelper.getAuthCredential(account.getIdToken());

            firebaseHelper.loginWithGoogleCallback(credential)
                    .addOnSuccessListener(authResult -> {

                        FirebaseUser user = authResult.getUser();

                        String uID = user.getUid();
                        String email = user.getEmail();
                        String displayedName = user.getDisplayName();
                        String provider = credential.getProvider();

                        Log.d(NACC_PRESENTER_TAG, "get email: " + user.getEmail() + "\n email email passing: " + email);

                        sharedPrefsHelper.saveSignWGoogleEmail(email);
                        sharedPrefsHelper.saveSignWEmailEmail(null);
                        sharedPrefsHelper.saveSignedUserID(uID);
                        sharedPrefsHelper.saveIsAnonymous(false);
                        sharedPrefsHelper.saveGoogleSignInCompleted(true);

                        if (authResult.getAdditionalUserInfo().isNewUser()) {

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("Email", email);
                            userMap.put("User ID", uID);
                            userMap.put("Displayed name", displayedName);
                            userMap.put("Provider", provider);

                            firebaseHelper.firestoreUsersUIDCallback(uID)
                                    .set(userMap)
                                    .addOnCompleteListener(task1 -> Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG
                                            + "\n successfully added to Users collection by GoogleSignIn, Email: " + email));

                            Map<String, Object> providerMap = new HashMap<>();
                            providerMap.put("Provider", provider);
                            providerMap.put("Email", email);
                            providerMap.put("UID", uID);

                            firebaseHelper.firestoreProvidersCallback(uID)
                                    .set(providerMap)
                                    .addOnCompleteListener(task12 -> Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG
                                            + " success GOOGLE providers"))
                                    .addOnFailureListener(e -> Log.d(NACC_PRESENTER_TAG,
                                            ConstantsPreferences.SH_FIRESTORE_TAG + "GOOGLE providers something wrong " + e));

                            view.cancelLoadingDialog();
                            view.showToast("SUCCESSFULLY LOGGED IN \n" + email);
                            view.startMyMainActivity();

                        } else {
                            Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER: "
                                    + authResult.getUser().getEmail());

                            if (credential.getProvider().equals("google.com")) {

                                user.reauthenticate(credential).addOnCompleteListener(task2 -> {
                                    Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG
                                            + "\n signInWithCredential, EXISTED USER is GOOGLE USER");

                                    Map<String, Object> providerMap = new HashMap<>();
                                    providerMap.put("Provider", provider);
                                    providerMap.put("Email", email);
                                    providerMap.put("UID", uID);
                                    firebaseHelper.firestoreProvidersCallback(email)
                                            .set(providerMap);

                                    view.cancelLoadingDialog();
                                    view.showToast("SUCCESSFULLY LOGGED IN \n" + email);
                                    view.startMyMainActivity();
                                });

                            } else {
                                user.reauthenticate(credential).addOnCompleteListener(task1 -> {
                                    Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG
                                            + "\n signInWithCredential, EXISTED USER changed to GOOGLE USER");

                                    Map<String, Object> providerMap = new HashMap<>();
                                    providerMap.put("Provider", provider);
                                    providerMap.put("Email", email);
                                    providerMap.put("UID", uID);

                                    firebaseHelper.firestoreProvidersCallback(email)
                                            .set(providerMap);

                                    view.cancelLoadingDialog();
                                    view.showToast("SUCCESSFULLY LOGGED IN \n" + email);
                                    view.startMyMainActivity();
                                });
                            }
                        }
                    }).addOnFailureListener(e -> {
                sharedPrefsHelper.saveSignWGoogleEmail(null);
                sharedPrefsHelper.saveSignedUserID(null);
                sharedPrefsHelper.saveGoogleSignInCompleted(false);
                view.cancelLoadingDialog();
                Log.w(NACC_PRESENTER_TAG, "signInResult:failed code = " + e);
            });
        } catch (Exception e) {
            Log.w(NACC_PRESENTER_TAG, "EXCEPTION CAPTURED signInResult: failed, code = " + e);
            sharedPrefsHelper.saveSignWGoogleEmail(null);
            sharedPrefsHelper.saveSignedUserID(null);
            sharedPrefsHelper.saveGoogleSignInCompleted(false);
        }
    }
}
