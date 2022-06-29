package com.pepe.vehicleexpensesapplication.data.firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.firebase.NewUserModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseHelper {

    private static final String FIREBASE_HELPER_TAG = "FIREBASE_HELPER_TAG";
    private static final String FIRESTORE_TAG = "FIRESTORE_TAG";
    private static final String FIREBASE_AUTH_TAG = "FIREBASE_AUTH_TAG";

    private FirebaseAuth fAuth;
    private FirebaseFirestore firestore;
    private FirebaseHistoryListener listener;
    private FirebaseSuccessListener successListener;
    private FirebaseAnonymousListener anonymousListener;
    private FirebaseUserListener usersListener;


    private static FirebaseHelper instance = null;


    public interface FirebaseHistoryListener {
        void onHistoryItemsLoaded(List<HistoryItemModel> items);
    }

    public interface FirebaseSuccessListener {
        void successStatus(boolean success);
    }

    public interface FirebaseAnonymousListener {
        void userData(String userID, String userEmail, String password, String name, String provider, boolean isAnonymous);
    }

    public interface FirebaseUserListener {
        void usersData(NewUserModel userData);

        void dataFailure(boolean failure);
    }


    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();

        }
        return instance;
    }

    public void setFirebaseListener(FirebaseHistoryListener listener) {
        this.listener = listener;
    }

    public void setFirebaseSuccessListener(FirebaseSuccessListener listener) {
        this.successListener = listener;
    }

    public void setFirebaseAnonymousListener(FirebaseAnonymousListener listener) {
        anonymousListener = listener;
    }

    public void setFirebaseUsersListener(FirebaseUserListener listener) {
        usersListener = listener;
    }

    private FirebaseHelper() {
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        firestore.setFirestoreSettings(settings);
    }


    public void signInAnonymously() {

        if (fAuth.getCurrentUser() != null) {

            String currentUID = fAuth.getCurrentUser().getUid();

            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                    .document(currentUID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            Log.d(FIREBASE_HELPER_TAG, "current user not null, provider: " + doc.getId());
                            if (Objects.equals(doc.get("PROVIDER"), "Anonymous")) {
                                NewUserModel newUserModel = doc.toObject(NewUserModel.class);
                                Log.d(FIREBASE_HELPER_TAG, "current user not null, provider: " + newUserModel.provider + "\n user ID: " + newUserModel.userID);
                                anonymousListener.userData(newUserModel.userID, newUserModel.userEmail, newUserModel.password,
                                        newUserModel.name, newUserModel.provider, newUserModel.isAnonymous);
                            } else {
                                fAuth.signInAnonymously()
                                        .addOnCompleteListener(task1 -> {
                                            if (task.isSuccessful()) {
                                                AuthResult doc1 = task1.getResult();
                                                FirebaseUser user = doc1.getUser();
                                                String uID = user.getUid();

                                                firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                        .whereEqualTo("provider", "Anonymous")
                                                        .addSnapshotListener((value, error) -> {
                                                            if (error != null) {
                                                                Log.d(FIREBASE_HELPER_TAG, "getUsers EXCEPTION CAPTURED: " + error.getCode());
                                                            } else {
                                                                if (value != null) {
                                                                    String guestNr = String.valueOf(value.size());
                                                                    NewUserModel newUserModel = new NewUserModel(uID, null, null,
                                                                            "Guest" + guestNr, "Anonymous", user.isAnonymous());
                                                                    Log.d(FIREBASE_HELPER_TAG, "current user is null, provider: " + newUserModel.provider
                                                                            + "\n user ID: " + newUserModel.userID);

                                                                    firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                                            .document(uID)
                                                                            .set(newUserModel)
                                                                            .addOnCompleteListener(task11 ->

                                                                                    anonymousListener.userData(newUserModel.userID, newUserModel.userEmail, newUserModel.password,
                                                                                            newUserModel.name, newUserModel.provider, newUserModel.isAnonymous));
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }

                    }).addOnFailureListener(e -> {
                        usersListener.dataFailure(true);
                        anonymousListener.userData(null, null, null, null, null, false);
                    });

        } else {

            fAuth.signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AuthResult doc = task.getResult();
                            FirebaseUser user = doc.getUser();
                            String uID = user.getUid();

                            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                    .whereEqualTo("provider", "Anonymous")
                                    .addSnapshotListener((value, error) -> {
                                        if (error != null) {
                                            Log.d(FIREBASE_HELPER_TAG, "getUsers EXCEPTION CAPTURED: " + error.getCode());
                                        } else {
                                            if (value != null) {
                                                String guestNr = String.valueOf(value.size());
                                                NewUserModel newUserModel = new NewUserModel(uID, null, null,
                                                        "Guest" + guestNr, "Anonymous", user.isAnonymous());

                                                Log.d(FIREBASE_HELPER_TAG, "current user is null, provider: " + newUserModel.provider
                                                        + "\n user ID: " + newUserModel.userID);

                                                firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                        .document(uID)
                                                        .set(newUserModel)
                                                        .addOnCompleteListener(task1 ->

                                                                anonymousListener.userData(newUserModel.userID, newUserModel.userEmail, newUserModel.password,
                                                                        newUserModel.name, newUserModel.provider, newUserModel.isAnonymous));
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

    public void checkCurrentUserCallback() {

        if (fAuth.getCurrentUser() != null) {
            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                    .document(fAuth.getCurrentUser().getUid())
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.d(FIREBASE_HELPER_TAG, "getUsers EXCEPTION CAPTURED: " + error.getCode());
                        } else {
                            if (value != null) {
                                NewUserModel newUserModel = value.toObject(NewUserModel.class);
                                usersListener.usersData(newUserModel);
                            }
                        }
                    });
        } else {
            NewUserModel newUserModel = new NewUserModel();
            usersListener.usersData(newUserModel);
        }
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

    public void createNewUserAccount(String email, String password, String nickName) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AuthResult authResult = task.getResult();
                        FirebaseUser user = authResult.getUser();
                        NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(), null, nickName, "Email&Password", user.isAnonymous());

                        firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                .document(user.getUid())
                                .set(newUserModel)
                                .addOnCompleteListener(task1 -> usersListener.usersData(newUserModel));
                    }
                });
    }

    public void loginWithGoogleCallback(AuthCredential authCredential) {

        fAuth.signInWithCredential(authCredential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();

                    if (authResult.getAdditionalUserInfo().isNewUser()) {

                        NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(), null,
                                user.getDisplayName(), "Google", user.isAnonymous());

                        firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                .document(user.getUid())
                                .set(newUserModel)
                                .addOnSuccessListener(unused -> usersListener.usersData(newUserModel));

                    } else {

                        firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                .whereEqualTo("USER_ID", user.getUid())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {

                                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                    String provider = documents.get(0).get("provider").toString();

                                    if (provider.equals("Email&Password")) {
                                        user.reauthenticate(authCredential)
                                                .addOnSuccessListener(unused -> {
                                                    NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(),
                                                            null, user.getDisplayName(), "Google", user.isAnonymous());

                                                    firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                            .document(user.getUid())
                                                            .set(newUserModel)
                                                            .addOnSuccessListener(unused12 -> usersListener.usersData(newUserModel));
                                                });
                                    } else {
                                        user.reauthenticate(authCredential)
                                                .addOnSuccessListener(unused -> {
                                                    NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(),
                                                            null, user.getDisplayName(), "Google", user.isAnonymous());

                                                    firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                            .document(user.getUid())
                                                            .set(newUserModel)
                                                            .addOnSuccessListener(unused1 -> usersListener.usersData(newUserModel));
                                                });
                                    }
                                });

                    }

                });

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

    public DocumentReference usersIdDocument(String uID) {
        return firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                .document(uID);
    }

    public DocumentReference providersUIdDocument(String uId) {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
                .document(uId);
    }

    public CollectionReference providersCollection() {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS);
    }

    public void saveNewRefill(float currentMileage, float refilledFuel, float priceOfFuel, int refillDate,
                              boolean fullCapacity, long currentTime, String refillNotes) {

        HistoryItemModel itemModel = new HistoryItemModel(refillDate, currentMileage, refilledFuel, priceOfFuel, refillNotes, fullCapacity, currentTime);

        if (getRefillsListCollection() != null) {
            getRefillsListCollection()
                    .document(String.valueOf(currentTime))
                    .set(itemModel)
                    .addOnCompleteListener(task -> {
                        successListener.successStatus(task.isSuccessful());
                    }).addOnFailureListener(e -> successListener.successStatus(false));
        }
    }

    public void getHistoryItems() {
        if (getRefillsListCollection() != null) {

            getRefillsListCollection()
                    .orderBy("currentMileage", Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.w(FIREBASE_HELPER_TAG, "Get history Items, list size: QUERY EXCEPTION: " + error.getCode());
                        } else {
                            if (value != null) {
                                List<HistoryItemModel> items = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : value) {
                                    HistoryItemModel historyItemModel = doc.toObject(HistoryItemModel.class);
                                    items.add(historyItemModel);
                                    Log.w(FIREBASE_HELPER_TAG, "Get history Items, list size items ADDED: " + items);
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

    public void deleteHistoryItem(String itemId) {
        firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                .document(getUid())
                .collection(ConstantsPreferences.COLLECTION_REFILL)
                .document(itemId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                .document(getUid())
                                .collection(ConstantsPreferences.COLLECTION_REFILL)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        successListener.successStatus(task1.isSuccessful());
                                        Log.d(FIREBASE_HELPER_TAG, "HISTORY ITEM DELETE SUCCESSFUL! HISTORY SIZE: " + task1.getResult().size());
                                    }
                                });
                    }
                });
    }

}
