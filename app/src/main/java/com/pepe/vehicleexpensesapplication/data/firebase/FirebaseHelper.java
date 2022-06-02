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

public class FirebaseHelper {

    private static final String FIREBASE_HELPER_TAG = "FIREBASE_HELPER_TAG";
    private static final String FIRESTORE_TAG = "FIRESTORE_TAG";
    private static final String FIREBASE_AUTH_TAG = "FIREBASE_AUTH_TAG";

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseAuth fAuth;
    private FirebaseFirestore firestore;
    private FirebaseHistoryListener listener;
    private FirebaseSuccessListener successListener;
    private FirebaseAnonymousListener anonymousListener;
    private FirebaseUserListener usersListener;
    private FirebaseIsAnonymousListener isAnonymousListener;


    private static FirebaseHelper instance = null;

    public interface FirebaseHistoryListener {
        void onHistoryItemsLoaded(List<HistoryItemModel> items);
    }

    public interface FirebaseSuccessListener {
        void successStatus(boolean success);
    }

    public interface FirebaseIsAnonymousListener {
        void isAnonymous(boolean isAnonymous);
    }

    public interface FirebaseAnonymousListener {
        void loginSuccess(boolean success);

        void userData(String userID, String userEmail, String password, String name, String provider, boolean isAnonymous);
    }

    public interface FirebaseUserListener {
        void usersData(NewUserModel userData);
        void dataFailure(boolean failure);
    }

    public static FirebaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseHelper(context);

        }
        return instance;
    }

    public void setFirebaseIsAnonymousListener(FirebaseIsAnonymousListener listener){
        isAnonymousListener = listener;
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


    public void signInAnonymouslyV2() {

        if (fAuth.getCurrentUser() != null) {

            String currentUID = fAuth.getCurrentUser().getUid();

            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                    .document(currentUID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            if (doc.get("PROVIDER").equals("Anonymous")) {
                                NewUserModel newUserModel = doc.toObject(NewUserModel.class);
                                Log.d(FIREBASE_HELPER_TAG, "current user not null, provider: " + newUserModel.PROVIDER + "\n user ID: " + newUserModel.USER_ID);
                                anonymousListener.userData(newUserModel.USER_ID, newUserModel.USER_EMAIL, newUserModel.PASSWORD,
                                        newUserModel.NAME, newUserModel.PROVIDER, newUserModel.IS_ANONYMOUS);
                            } else {
                                fAuth.signInAnonymously()
                                        .addOnCompleteListener(task1 -> {
                                            if (task.isSuccessful()) {
                                                AuthResult doc1 = task1.getResult();
                                                FirebaseUser user = doc1.getUser();
                                                String uID = user.getUid();

                                                NewUserModel newUserModel = new NewUserModel(uID, null, null, "Guest", "Anonymous", user.isAnonymous());
                                                Log.d(FIREBASE_HELPER_TAG, "current user is null, provider: " + newUserModel.PROVIDER + "\n user ID: " + newUserModel.USER_ID);
                                                sharedPrefsHelper.saveSignedUserID(uID);
                                                anonymousListener.loginSuccess(task1.isSuccessful());

                                                firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                        .document(uID)
                                                        .set(newUserModel)
                                                        .addOnCompleteListener(task2 -> anonymousListener.userData(newUserModel.USER_ID, newUserModel.USER_EMAIL, newUserModel.PASSWORD,
                                                                newUserModel.NAME, newUserModel.PROVIDER, newUserModel.IS_ANONYMOUS));
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
                                    .whereEqualTo("PROVIDER", "Anonymous")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (error != null) {
                                                Log.d(FIREBASE_HELPER_TAG, "getUsers EXCEPTION CAPTURED: " + error.getCode());
                                            } else {
                                                if (value != null) {
                                                    String guestNr = String.valueOf(value.size());
                                                    NewUserModel newUserModel = new NewUserModel(uID, null, null, "Guest" + guestNr, "Anonymous", user.isAnonymous());
                                                    Log.d(FIREBASE_HELPER_TAG, "current user is null, provider: " + newUserModel.PROVIDER + "\n user ID: " + newUserModel.USER_ID);
                                                    sharedPrefsHelper.saveSignedUserID(uID);
                                                    anonymousListener.loginSuccess(task.isSuccessful());

                                                    firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                            .document(uID)
                                                            .set(newUserModel)
                                                            .addOnCompleteListener(task1 ->

                                                                    anonymousListener.userData(newUserModel.USER_ID, newUserModel.USER_EMAIL, newUserModel.PASSWORD,
                                                                            newUserModel.NAME, newUserModel.PROVIDER, newUserModel.IS_ANONYMOUS));
                                                }
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

    public void getCurrentUserCallbackV2() {

        if (fAuth.getCurrentUser() != null) {
            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                    .document(fAuth.getCurrentUser().getUid())
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.d(FIREBASE_HELPER_TAG, "getUsers EXCEPTION CAPTURED: " + error.getCode());
                        } else {
                            if (value != null) {
                                NewUserModel newUserModel = value.toObject(NewUserModel.class);
                                sharedPrefsHelper.saveSignedUserID(fAuth.getCurrentUser().getUid());
                                usersListener.usersData(newUserModel);
                            }
                        }
                    });
        }else {
            NewUserModel newUserModel = new NewUserModel();
            usersListener.usersData(newUserModel);
        }
    }

    public void checkIsUserAnonymous(){
        if (fAuth.getCurrentUser() != null){
            isAnonymousListener.isAnonymous(fAuth.getCurrentUser().isAnonymous());
        }else {
            isAnonymousListener.isAnonymous(false);
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
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AuthResult authResult = task.getResult();
                            FirebaseUser user = authResult.getUser();
                            NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(), null, nickName, "Email&Password", user.isAnonymous());

                            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                    .document(user.getUid())
                                    .set(newUserModel)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            usersListener.usersData(newUserModel);
                                        }
                                    });
                        }
                    }
                });
    }

    public void loginWithGoogleCallback(AuthCredential authCredential) {

        fAuth.signInWithCredential(authCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();

                        if (authResult.getAdditionalUserInfo().isNewUser()) {

                            NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(), null, user.getDisplayName(), "Google", user.isAnonymous());

                            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                    .document(user.getUid())
                                    .set(newUserModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            usersListener.usersData(newUserModel);
                                        }
                                    });

                        } else {

                            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                    .whereEqualTo("USER_ID", user.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<String> wlo = new ArrayList<>();

                                            List<DocumentSnapshot> elo = queryDocumentSnapshots.getDocuments();
                                            String jjj = elo.get(0).get("PROVIDER").toString();

                                            if (jjj.equals("Email&Password")) {
                                                user.reauthenticate(authCredential)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(), null, user.getDisplayName(), "Google", user.isAnonymous());

                                                                firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                                        .document(user.getUid())
                                                                        .set(newUserModel)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                usersListener.usersData(newUserModel);
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            } else {
                                                user.reauthenticate(authCredential)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                NewUserModel newUserModel = new NewUserModel(user.getUid(), user.getEmail(), null, user.getDisplayName(), "Google", user.isAnonymous());

                                                                firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                                                                        .document(user.getUid())
                                                                        .set(newUserModel)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                usersListener.usersData(newUserModel);
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }

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

//    public void loginAnonymously() {
//
//        if (sharedPrefsHelper.getSignedUserID() != null && fAuth.getCurrentUser() != null
//                && sharedPrefsHelper.getSignedUserID().equals(getUid())) {
//
//            sharedPrefsHelper.saveSignedUserID(getCurrentUserCallback().getUid());
//
//        } else {
//
//            if (sharedPrefsHelper.getSignedUserID() == null) {
//
//                fAuth.signInAnonymously().addOnCompleteListener(task -> {
//
//                    AuthResult doc = task.getResult();
//                    FirebaseUser user = doc.getUser();
//                    String uID = user.getUid();
//
//                    Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n anonymously logged in, uID: " + uID);
//
//                    Map<String, Object> userMap = new HashMap<>();
//                    userMap.put("Guest", uID);
//                    userMap.put("User ID", uID);
//
//                    sharedPrefsHelper.saveIsAnonymous(true);
//                    sharedPrefsHelper.saveSignedUserID(uID);
//                    sharedPrefsHelper.saveSignWEmailEmail(null);
//                    sharedPrefsHelper.saveSignWGoogleEmail(null);
//
//                    firestore.collection(ConstantsPreferences.COLLECTION_USERS)
//                            .document(uID)
//                            .set(userMap)
//                            .addOnCompleteListener(task12 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG
//                                    + "\n successfully added to Users collection as guest, uID: " + uID));
//                    Map<String, Object> providerMap = new HashMap<>();
//                    providerMap.put("Provider", "Anonymously");
//                    providerMap.put("UID", uID);
//
//                    firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
//                            .document(uID).set(providerMap)
//                            .addOnCompleteListener(task1 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + " success ANONYMOUS providers"))
//                            .addOnFailureListener(e -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "ANONYMOUS something wrong " + e));
//                }).addOnFailureListener(e -> {
//                    sharedPrefsHelper.saveIsAnonymous(false);
//                    sharedPrefsHelper.saveSignedUserID(null);
//                });
//            } else {
//                sharedPrefsHelper.saveIsAnonymous(true);
//            }
//        }
//    }

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

    public  void fetchSignInMethodsForEmailCallback(String email){

        fAuth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                    @Override
                    public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {
                        boolean isRegister = signInMethodQueryResult.getSignInMethods().isEmpty();
                        if (!isRegister) {

                        }else {

                        }
                    }
                });

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
