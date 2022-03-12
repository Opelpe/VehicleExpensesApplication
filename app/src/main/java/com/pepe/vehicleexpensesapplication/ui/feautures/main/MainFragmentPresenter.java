package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class MainFragmentPresenter implements MainFragmentContract.Presenter {

    private static final String MAIN_FR_PRESENTER_TAG = "MAIN_FR_PRESENTER_TAG";

    private MainFragmentContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    public MainFragmentPresenter(MainFragmentContract.View view, Context mainContext) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(mainContext);
        firebaseHelper = FirebaseHelper.getInstance();
    }

    @Override
    public void onViewCreated() {


        view.setMainFragmentToolbar();

        try {
            Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: START");
            if (firebaseHelper.getUid() != null) {
                Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: IF");
                FirebaseUser user = firebaseHelper.getCurrentUser();
                String uID = user.getUid();

                firebaseHelper.firestoreUIDProvidersCallback().whereEqualTo("UID", firebaseHelper.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: START before task.isSuccess");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                String provider = doc.get("Provider").toString();
                                Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: " + provider);

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
            Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER EXCEPTION CAPTURED:: " + e);
        }

    }

    @Override
    public void actionChangeGooleAccountClicked(GoogleSignInOptions gso, GoogleSignInClient gsc) {
        this.gso = gso;
        this.gsc = gsc;

        try {
            Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: START");
            if (firebaseHelper.getUid() != null) {
                Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: IF");
                FirebaseUser user = firebaseHelper.getCurrentUser();
                String uID = user.getUid();

                firebaseHelper.firestoreUIDProvidersCallback().whereEqualTo("UID", firebaseHelper.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: START before task.isSuccess");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                String provider = doc.get("Provider").toString();
                                Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: " + provider);
                            }
                        }
                    }
                });

            }

        } catch (Exception e) {
            Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER EXCEPTION CAPTURED:: " + e);
        }

//        signIn();

    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        Log.d(MAIN_FR_PRESENTER_TAG, "\n on handleSignInResult START");
        try {

            if (task.isSuccessful()) {
                Log.d(MAIN_FR_PRESENTER_TAG, "\n is task successful TASK: " + task.isSuccessful());
            } else {
                Log.d(MAIN_FR_PRESENTER_TAG, "\n is task successful TASK: " + task.isSuccessful());
            }
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(MAIN_FR_PRESENTER_TAG, "\n on handleSignInResult cmpleted TASK: " + account.getId());

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
                                    .addOnCompleteListener(task1 -> Log.d(MAIN_FR_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG + "\n successfully added to Users collection by GoogleSignIn, Email: " + email));

                            Map<String, Object> providerMap = new HashMap<>();
                            providerMap.put("Provider", provider);
                            providerMap.put("Email", email);
                            providerMap.put("UID", uID);

                            view.startMyMainActivity();

                            firebaseHelper.firestoreProvidersCallback(email)
                                    .set(providerMap)
                                    .addOnCompleteListener(task12 -> Log.d(MAIN_FR_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG + " success GOOGLE providers")).addOnFailureListener(e -> Log.d(MAIN_FR_PRESENTER_TAG, ConstantsPreferences.SH_FIRESTORE_TAG + "GOOGLE providers something wrong " + e));
                        } else {
                            Log.d(MAIN_FR_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER: " + authResult.getUser().getEmail());
                            view.startMyMainActivity();
                            user.reauthenticate(credential).addOnCompleteListener(task12 -> {
                                Log.d(MAIN_FR_PRESENTER_TAG, ConstantsPreferences.SH_FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER changed to GOOGLE USER");

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
                Log.w(MAIN_FR_PRESENTER_TAG, "signInResult:failed code = " + e);
            });


        } catch (Exception e) {
            Log.w(MAIN_FR_PRESENTER_TAG, "EXCEPTION CAPTURED: " + e);
        }
    }

    private void signIn() {

//        try {
//            FirebaseUser user = firebaseHelper.getCurrentUser();
//            String uID = user.getUid();
//
//            firebaseHelper.firestoreUIDProvidersCallback().whereEqualTo("UID", uID).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                    List<DocumentSnapshot> doc = value.getDocuments();
//                    DocumentSnapshot cos = doc.get(value.size());
//                    String provisder = cos.get("Provider").toString();
//
//                    Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER: " + provisder);
//                }
//            });
//        }catch (Exception e){
//            Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER EXCEPTION CAPTURED:: " + e);
//        }


//
//        Intent signInIntent = gsc.getSignInIntent();
//
//            view.presenterStartActivityForResult(signInIntent, RC_SIGN_IN);


    }
}
