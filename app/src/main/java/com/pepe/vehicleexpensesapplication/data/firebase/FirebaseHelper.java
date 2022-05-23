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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pepe.vehicleexpensesapplication.data.model.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    private static final String FIREBASE_HELPER_TAG = "FIREBASE_HELPER_TAG";
    private static final String FIRESTORE_TAG = "FIRESTORE_TAG";
    private static final String FIREBASE_AUTH_TAG = "FIREBASE_AUTH_TAG";

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseAuth fAuth;
    private FirebaseFirestore firestore;
    private FirebaseHistoryListener listener;
    private FirebaseSuccessListener successListener;

    private static FirebaseHelper instance = null;

    public interface FirebaseHistoryListener {
        void onHistoryItemsLoaded(List<HistoryItemModel> items);
    }

    public interface FirebaseSuccessListener {
        void successStatus(boolean success);
    }

    public static FirebaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseHelper(context);

        }
        return instance;
    }

    public void setFirebaseListener(FirebaseHistoryListener listener) {
        this.listener = listener;
    }

    public void setFirebaseSuccessListener(FirebaseSuccessListener listener) {
        this.successListener = listener;
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

    public Task<Void> deleteFirebaseUserTask() {
        return fAuth.getCurrentUser().delete();
    }

    public Task<Void> deleteUserDataTask(String uID) {
        return firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(uID).delete();
    }

    public Task<AuthResult> createWithEmailPasswordTask(String email, String password, String nickName) {
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

    public Task<AuthResult> loginWithEmailPasswordTask(String email, String password) {
        return fAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> loginWithGoogleTask(AuthCredential authCredential) {
        return fAuth.signInWithCredential(authCredential);
    }

    public AuthCredential getAuthCredential(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return credential;
    }

    public Task<SignInMethodQueryResult> fetchSignInMethodsForEmailTask(String email) {
        return fAuth.fetchSignInMethodsForEmail(email);
    }

    public Query getProvidersQuery(String email) {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS).whereEqualTo("Email", email);
    }

    public DocumentReference usersIDDocument(String uID) {
        return firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                .document(uID);
    }

    public DocumentReference providersUIDDocument(String uId) {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
                .document(uId);
    }

    public CollectionReference providersCollection() {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS);
    }

    public void saveNewRefill(float currentMileage, float refilledFuel, float priceOfFuel, String refillDate,
                              boolean fullCapacity, long currentTime, String dateCount, String refillNotes) {

        HistoryItemModel itemModel = new HistoryItemModel(refillDate, currentMileage, refilledFuel, priceOfFuel, refillNotes, fullCapacity, currentTime);

        getRefillsListCollection()
                .document(String.valueOf(currentTime))
                .set(itemModel)
                .addOnCompleteListener(task -> {
                    successListener.successStatus(task.isSuccessful());
                }).addOnFailureListener(e -> successListener.successStatus(false));

    }

    public void getHistoryItems() {
        if (getRefillsListCollection() != null) {

            getRefillsListCollection()
                    .orderBy("CURRENT_MILEAGE", Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.w(FIREBASE_HELPER_TAG, "Get history Items, list size: QUERY EXCEPTION: " + error.getCode());
                        } else {
                            if (value != null) {
                                List<HistoryItemModel> items = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : value) {
                                    HistoryItemModel historyItemModel = doc.toObject(HistoryItemModel.class);
                                    items.add(historyItemModel);
                                }
                                Log.w(FIREBASE_HELPER_TAG, "Get history Items, list size items: " + items);
                                Log.w(FIREBASE_HELPER_TAG, "Get history Items, list size: " + items.size());
                                listener.onHistoryItemsLoaded(items);
                            }
                        }
                    });
        }
    }


    public CollectionReference getRefillsListCollection() {
        if (getUid() != null) {
            return firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(getUid()).collection(ConstantsPreferences.COLLECTION_REFILL);
        } else return null;

    }

}
