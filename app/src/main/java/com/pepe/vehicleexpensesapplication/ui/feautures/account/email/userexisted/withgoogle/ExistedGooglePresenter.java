package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withgoogle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class ExistedGooglePresenter implements ExistedGoogleContract.Presenter {

    private static final String EXISTED_GOOGLE_PRESENTER_TAG = "EXISTED_GOOGLE_PRESENTER";

    private ExistedGoogleContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    private GoogleSignInClient googleSignInClient;

    public ExistedGooglePresenter(ExistedGoogleContract.View view, Context context) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(context);
        firebaseHelper = FirebaseHelper.getInstance(context);

    }

    @Override
    public void onViewCreated() {
        view.setGoogleEmailInfo(sharedPrefsHelper.getEnteredEmail());
    }

    @Override
    public void onlogInWithGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient) {
        googleSignInClient = mGoogleSignInClient;

        signIn();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            view.showLoadingGoogleDialog();
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(EXISTED_GOOGLE_PRESENTER_TAG, "\n on handleSignInResult, completed TASK, GOOGLE account ID: " + account.getId());

            AuthCredential credential = firebaseHelper.getAuthCredential(account.getIdToken());

            firebaseHelper.loginWithGoogleTask(credential)
                    .addOnSuccessListener(authResult -> {

                        FirebaseUser user = authResult.getUser();
                        String uID = user.getUid();
                        String email = user.getEmail();
                        String displayedName = user.getDisplayName();
                        String provider = credential.getProvider();

                        sharedPrefsHelper.saveIsAnonymous(false);
                        sharedPrefsHelper.saveSignedUserEmail(email);
                        sharedPrefsHelper.saveGoogleSignInCompleted(true);
                        sharedPrefsHelper.saveSignWGoogleEmail(email);
                        sharedPrefsHelper.saveSignedUserID(uID);
                        sharedPrefsHelper.saveSignWEmailEmail(null);

                        if (authResult.getAdditionalUserInfo().isNewUser()) {

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("Email", email);
                            userMap.put("User ID", uID);
                            userMap.put("Displayed name", displayedName);
                            userMap.put("Provider", provider);

                            firebaseHelper.usersIDDocument(uID)
                                    .set(userMap)
                                    .addOnCompleteListener(task1 -> Log.d(EXISTED_GOOGLE_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG
                                            + "\n successfully added to Users collection by GoogleSignIn, Email: " + email));

                            Map<String, Object> providerMap = new HashMap<>();
                            providerMap.put("Provider", provider);
                            providerMap.put("Email", email);
                            providerMap.put("UID", uID);

                            firebaseHelper.providersUIDDocument(email)
                                    .set(providerMap)
                                    .addOnCompleteListener(task12 -> Log.d(EXISTED_GOOGLE_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG
                                            + " success with saving GOOGLE providers"))
                                    .addOnFailureListener(e -> Log.d(EXISTED_GOOGLE_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG
                                            + "GOOGLE providers something wrong with saving, FAILURE: " + e));

                            view.cancelLoadingDialog();
                            view.showToast("NEW ACCOUNT CREATED \n" + email);
                            view.startMyMainActivity();
                        } else {

                            user.reauthenticate(credential).addOnCompleteListener(task12 -> {
                                Log.d(EXISTED_GOOGLE_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG
                                        + "\n signInWithCredential, EXISTED USER changed to GOOGLE USER");

                                Map<String, Object> providerMap = new HashMap<>();
                                providerMap.put("Provider", provider);
                                providerMap.put("Email", email);
                                providerMap.put("UID", uID);

                                firebaseHelper.providersUIDDocument(email)
                                        .set(providerMap);

                                view.cancelLoadingDialog();
                                view.showToast("SUCCESSFULLY LOGGED IN \n" + email);
                                view.startMyMainActivity();
                            });
                        }

                    }).addOnFailureListener(e -> {

                view.cancelLoadingDialog();

                view.showToast(e.getMessage());

                sharedPrefsHelper.saveSignedUserEmail(null);
                sharedPrefsHelper.saveGoogleSignInCompleted(false);
                sharedPrefsHelper.saveSignWGoogleEmail(null);

                Log.w(EXISTED_GOOGLE_PRESENTER_TAG, "signInResult: failed code = " + e);
            });
        } catch (ApiException e) {
            Log.w(EXISTED_GOOGLE_PRESENTER_TAG, "signInResult: EXCEPTION captured = " + e.getStatusCode());
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        view.presenterStartActivityForResult(signInIntent, ConstantsPreferences.RC_SIGN_IN);
    }
}
