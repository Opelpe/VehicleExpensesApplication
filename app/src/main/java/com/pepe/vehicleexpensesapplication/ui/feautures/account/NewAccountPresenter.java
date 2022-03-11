package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class NewAccountPresenter implements NewAccountContract.Presenter {

    private static final String NACC_PRESENTER_TAG = "NACC_PRESENTER_TAG";

    private NewAccountContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;

    private FirebaseHelper firebaseHelper;


    public NewAccountPresenter(NewAccountContract.View view, Context applicationContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = new SharedPrefsHelper(applicationContext);
    }

    @Override
    public void onGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient, GoogleSignInOptions gso) {

        googleSignInClient = mGoogleSignInClient;
        googleSignInOptions = gso;

        signIn();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        view.presenterStartActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onEmailSignButtonClicked() {
        view.startEmailAccountActivity();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult START");
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult cmpleted TASK: " + account.getId());

            String googleEmail = account.getEmail();

            AuthCredential credential = firebaseHelper.getAuthCredential(account.getIdToken());
            view.showLoadingGoogleDialog(googleEmail);
            firebaseHelper.loginWithGoogleCallback(credential)
                    .addOnSuccessListener(authResult -> {


                        sharedPrefsHelper.saveGoogleSignInCompleted(true);

                        FirebaseUser user = authResult.getUser();

                        sharedPrefsHelper.saveSignedUserEmail(user.getEmail());

                        String uID = user.getUid();
                        String email = user.getEmail();
                        String displayedName = user.getDisplayName();
                        String provider = credential.getProvider();

                        if (authResult.getAdditionalUserInfo().isNewUser()) {

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("Email", email);
                            userMap.put("User ID", uID);
                            userMap.put("Displayed name", displayedName);
                            userMap.put("Provider", provider);

                            firebaseHelper.firestoreUsersUIDCallback(uID)
                                    .set(userMap)
                                    .addOnCompleteListener(task1 -> Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG + "\n successfully added to Users collection by GoogleSignIn, Email: " + email));

                            Map<String, Object> providerMap = new HashMap<>();
                            providerMap.put("Provider", provider);
                            providerMap.put("Email", email);
                            providerMap.put("UID", uID);

                            view.startMyMainActivity();

                            firebaseHelper.firestoreProvidersCallback(email)
                                    .set(providerMap)
                                    .addOnCompleteListener(task12 -> Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG + " success GOOGLE providers")).addOnFailureListener(e -> Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG + "GOOGLE providers something wrong " + e));
                        } else {
                            Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER: " + authResult.getUser().getEmail());
                            view.startMyMainActivity();
                            user.reauthenticate(credential).addOnCompleteListener(task12 -> {
                                Log.d(NACC_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER changed to GOOGLE USER");

                                Map<String, Object> providerMap = new HashMap<>();
                                providerMap.put("Provider", provider);
                                providerMap.put("Email", email);
                                providerMap.put("UID", uID);


                                firebaseHelper.firestoreProvidersCallback(email)
                                        .set(providerMap)
                                        .addOnCompleteListener(task121 -> {

                                        });
                            });
                        }
                    }).addOnFailureListener(e -> {
                        sharedPrefsHelper.saveGoogleSignInCompleted(false);
                        Log.w(NACC_PRESENTER_TAG, "signInResult:failed code = " + e);
                    });
        } catch (ApiException e) {
            Log.w(NACC_PRESENTER_TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
