package com.pepe.vehicleexpensesapplication.data.firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private static final String FIREBASE_HELPER_TAG = "FIREBASE_HELPER_TAG";
    private static final String FIRESTORE_TAG = "FIRESTORE_TAG";
    private static final String FIREBASE_AUTH_TAG = "FIREBASE_AUTH_TAG";

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseAuth fAuth;
    private FirebaseFirestore firestore;

    private static FirebaseHelper instance = null;

    public static FirebaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseHelper(context);

        }
        return instance;
    }

    private FirebaseHelper(Context context) {
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        sharedPrefsHelper = new SharedPrefsHelper(context);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        firestore.setFirestoreSettings(settings);
    }

    public void logOutCurrentUser() {
        fAuth.signOut();
    }

    public Task<Void> deleteFirebaseUserCallback() {
        return fAuth.getCurrentUser().delete();
    }

    public Task<Void> deleteUserDataCallback(String uID) {
        return firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(uID).delete();
    }

    public Task<AuthResult> createWithEmailPasswordCallback(String email, String password, String nickName) {
        return fAuth.createUserWithEmailAndPassword(email, password);
    }

    public FirebaseUser getCurrentUserCallback() {
        Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + " get CURRENT USER START: " + fAuth.getCurrentUser());
        return fAuth.getCurrentUser();
    }

    public String getUid() {
        Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + " get UID START: " + fAuth.getUid());
        if (getCurrentUserCallback() != null) {
            Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + " get UID user != null: " + fAuth.getCurrentUser().getUid());
            return fAuth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public void loginAnonymously() {

        if (sharedPrefsHelper.getSignedUserID() != null && fAuth.getCurrentUser() != null
                && sharedPrefsHelper.getSignedUserID().equals(getUid())) {

            sharedPrefsHelper.saveSignedUserID(getCurrentUserCallback().getUid());

        } else {

            if (sharedPrefsHelper.getSignedUserID() == null) {

                fAuth.signInAnonymously().addOnCompleteListener(task -> {

                    AuthResult doc = task.getResult();
                    FirebaseUser user = doc.getUser();
                    String uID = user.getUid();

                    Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n anonymously logged in, uID: " + uID);

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Guest", uID);
                    userMap.put("User ID", uID);

                    sharedPrefsHelper.saveIsAnonymous(true);
                    sharedPrefsHelper.saveSignedUserID(uID);
                    sharedPrefsHelper.saveSignWEmailEmail(null);
                    sharedPrefsHelper.saveSignWGoogleEmail(null);

                    firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                            .document(uID)
                            .set(userMap)
                            .addOnCompleteListener(task12 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG
                                    + "\n successfully added to Users collection as guest, uID: " + uID));
                    Map<String, Object> providerMap = new HashMap<>();
                    providerMap.put("Provider", "Anonymously");
                    providerMap.put("UID", uID);

                    firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
                            .document(uID).set(providerMap)
                            .addOnCompleteListener(task1 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + " success ANONYMOUS providers"))
                            .addOnFailureListener(e -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "ANONYMOUS something wrong " + e));
                }).addOnFailureListener(e -> {
                    sharedPrefsHelper.saveIsAnonymous(false);
                    sharedPrefsHelper.saveSignedUserID(null);
                });
            } else {
                sharedPrefsHelper.saveIsAnonymous(true);
            }
        }
    }

    public Task<AuthResult> loginWithEmailPasswordCallback(String email, String password) {
        return fAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> loginWithGoogleCallback(AuthCredential authCredential) {
        return fAuth.signInWithCredential(authCredential);
    }

    public AuthCredential getAuthCredential(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return credential;
    }

    public Task<SignInMethodQueryResult> fetchSignInMethodsForEmailCallback(String email) {
        return fAuth.fetchSignInMethodsForEmail(email);
    }

    public Query getProviderCallback(String email) {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS).whereEqualTo("Email", email);
    }

    public DocumentReference firestoreUsersUIDCallback(String uID) {
        return firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                .document(uID);
    }

    public DocumentReference firestoreProvidersCallback(String uId) {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
                .document(uId);
    }

    public CollectionReference firestoreUIDProvidersCallback() {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS);
    }

    public DocumentReference firestoreUIDUsersCallback(String uID) {
        return firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(uID);
    }

}
