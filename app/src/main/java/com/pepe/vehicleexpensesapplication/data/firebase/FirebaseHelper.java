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
import com.google.firebase.firestore.FirebaseFirestore;
import com.pepe.vehicleexpensesapplication.data.ConstantsPreferences;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private FirebaseAuth fAuth;
    private FirebaseFirestore firestore;

    private FirebaseHelper(){
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void createWithEmailPassword(String email, String password, String nickName){
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                AuthResult doc = task.getResult();
                FirebaseUser user = doc.getUser();
                String uID = user.getUid();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("Email", email);
                userMap.put("Password", password);
                userMap.put("Nick", nickName);
                userMap.put("User ID", uID);

                firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(uID).set(userMap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FirebaseData", "something wrong " + e);
            }
        });

    }

    public FirebaseUser getCurrentUser(){
        FirebaseUser user = fAuth.getCurrentUser();
        return user;
    }

    private static FirebaseHelper instance = null;

    public static FirebaseHelper getInstance(){
        if (instance == null){
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public void setStartCheckBoxStatus(boolean startCheckboxChecked) {

        if (getCurrentUser() != null){
            String uId = getCurrentUser().getUid();
            Log.d("helper", uId);

            Map<String, Object> mapa = new HashMap<>();
            mapa.put("CHECKED", startCheckboxChecked);

            firestore.collection(ConstantsPreferences.COLLECTION_USERS).document("jakisinny").collection(ConstantsPreferences.COLLECTION_CHECKBOX).document("checkbox").set(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Void doc = task.getResult();
                }
            });
        }else {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("CHECKED", startCheckboxChecked);
            firestore.collection(ConstantsPreferences.COLLECTION_USERS).document("jakisinny")
                    .collection(ConstantsPreferences.COLLECTION_CHECKBOX).document("checkbox").set(mapa);
        }

    }
//
//    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
//            new FirebaseAuthUIActivityResultContract(),
//            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
//                @Override
//                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
//                    onSignInResult(result);
//                }
//            }
//    );
//
//    private ActivityResultLauncher<Intent> registerForActivityResult(FirebaseAuthUIActivityResultContract firebaseAuthUIActivityResultContract, ActivityResultCallback<FirebaseAuthUIAuthenticationResult> firebaseAuthUIAuthenticationResultActivityResultCallback) {
//        return (ActivityResultLauncher<Intent>) firebaseAuthUIAuthenticationResultActivityResultCallback;
//    }
//
//    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.EmailBuilder().build(),
//            new AuthUI.IdpConfig.PhoneBuilder().build(),
//            new AuthUI.IdpConfig.GoogleBuilder().build(),
//            new AuthUI.IdpConfig.FacebookBuilder().build(),
//            new AuthUI.IdpConfig.TwitterBuilder().build());
//
//    // Create and launch sign-in intent
//    Intent signInIntent = AuthUI.getInstance()
//            .createSignInIntentBuilder()
//            .setAvailableProviders(providers)
//            .build();
//
//    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
//        IdpResponse response = result.getIdpResponse();
//        if (result.getResultCode() == RESULT_OK) {
//            // Successfully signed in
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            // ...
//        } else {
//            // Sign in failed. If response is null the user canceled the
//            // sign-in flow using the back button. Otherwise check
//            // response.getError().getErrorCode() and handle the error.
//            // ...
//        }
//    }

    public void loginAnonymously(){

        fAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                AuthResult doc = task.getResult();
                FirebaseUser user = doc.getUser();
                String uID = user.getUid();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("Guest", uID);
                userMap.put("User ID", uID);

                firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(uID).set(userMap);
            }
        });
    }

    public Task<AuthResult> signInWithCredential(AuthCredential credential){
        return fAuth.signInWithCredential(credential);
    }
}
