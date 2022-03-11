package com.pepe.vehicleexpensesapplication.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private FirebaseHelper() {
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void createWithEmailPassword(String email, String password, String nickName) {
        fAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(task -> {
                    AuthResult doc = task.getResult();
                    FirebaseUser user = doc.getUser();
                    String uID = user.getUid();

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Email", email);
                    userMap.put("Password", password);
                    userMap.put("Name", nickName);
                    userMap.put("User ID", uID);

                    firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                            .document(uID)
                            .set(userMap)
                            .addOnCompleteListener(task1 -> {

                            });

                    Map<String, Object> providerMap = new HashMap<>();
                    providerMap.put("Provider", ConstantsPreferences.PROVIDERS_EMAIL_PASSWORD);
                    providerMap.put("Email", email);
                    providerMap.put("UID", uID);
                    firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
                            .document(email)
                            .set(providerMap)
                            .addOnCompleteListener(task12 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + " success EMAIL providers")).addOnFailureListener(e -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "EMAIL something wrong " + e));

                }).addOnFailureListener(e -> Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "EMAIL something wrong " + e));
    }

    public FirebaseUser getCurrentUser() {
        return fAuth.getCurrentUser();
    }

    private static FirebaseHelper instance = null;

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public void loginAnonymously() {

        fAuth.signInAnonymously().addOnCompleteListener(task -> {

            AuthResult doc = task.getResult();
            FirebaseUser user = doc.getUser();
            String uID = user.getUid();

            Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n anonymously logged in, uID: " + uID);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("Guest", uID);
            userMap.put("User ID", uID);

            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                    .document(uID)
                    .set(userMap)
                    .addOnCompleteListener(task12 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "\n successfully added to Users collection as guest, uID: " + uID));
            Map<String, Object> providerMap = new HashMap<>();
            providerMap.put("Provider", "Anonymously");
            providerMap.put("UID", uID);

            firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
                    .document(uID).set(providerMap)
                    .addOnCompleteListener(task1 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + " success ANONYMOUS providers")).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "ANONYMOUS something wrong " + e);
                }
            });
        }).addOnFailureListener(e -> {

        });
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

    public Task<DocumentSnapshot> getProviderCallback(String email) {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS).document(email).get();
    }

    public DocumentReference firestoreUsersUIDCallback(String uID) {
        return firestore.collection(ConstantsPreferences.COLLECTION_USERS)
                .document(uID);
    }

    public DocumentReference firestoreProvidersCallback(String email) {
        return firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
                .document(email);
    }

//    public void loginWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//
//        fAuth.signInWithCredential(credential)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//
//                        boolean taskOk = task.isComplete();
//
//
//                        Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n signInWithCredential:success");
//                        AuthResult result = task.getResult();
//                        FirebaseUser user = result.getUser();
//
//
//                        String uID = user.getUid();
//                        String email = user.getEmail();
//                        String displayedName = user.getDisplayName();
//                        String provider = credential.getProvider();
////
////                        sharedPrefsHelper.saveSignedUserEmail(email);
////                        for (UserInfo userInfo : user.getProviderData()){
////                           String providerId = userInfo.getProviderId();
////                        }
//
//
//                        if (result.getAdditionalUserInfo().isNewUser()) {
//
//
//                            Map<String, Object> userMap = new HashMap<>();
//                            userMap.put("Email", email);
//                            userMap.put("User ID", uID);
//                            userMap.put("Displayed name", displayedName);
//                            userMap.put("Provider", provider);
//
//
//                            firestore.collection(ConstantsPreferences.COLLECTION_USERS)
//                                    .document(uID).set(userMap)
//                                    .addOnCompleteListener(task1 -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "\n successfully added to Users collection by GoogleSignIn, Email: " + email));
//
//                            Map<String, Object> providerMap = new HashMap<>();
//                            providerMap.put("Provider", provider);
//                            providerMap.put("Email", email);
//                            providerMap.put("UID", uID);
//
//                            firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
//                                    .document(email)
//                                    .set(providerMap)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + " success GOOGLE providers");
//                                        }
//                                    }).addOnFailureListener(e -> Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "GOOGLE something wrong " + e));
//                        } else {
//                            Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER: " + task.getResult().getUser().getEmail());
//
//                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER changed to GOOGLE USER");
//
//                                    Map<String, Object> providerMap = new HashMap<>();
//                                    providerMap.put("Provider", provider);
//                                    providerMap.put("Email", email);
//                                    providerMap.put("UID", uID);
//
//                                    firestore.collection(ConstantsPreferences.COLLECTION_PROVIDERS)
//                                            .document(email)
//                                            .set(providerMap)
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                }
//                                            });
//                                }
//                            });
//                        }
//                        sharedPrefsHelper.saveGoogleSignInCompleted(task.isSuccessful());
//                    } else {
//                        Log.w(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n signInWithCredential:failure ", task.getException());
//                    }
//                });
//    }

}
