package com.pepe.vehicleexpensesapplication.data.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.NewAccountActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private static final String FIREBASE_HELPER_TAG = "FIREBASE_HELPER_TAG";
    private static final String FIRESTORE_TAG = "FIRESTORE_TAG";
    private static final String FIREBASE_AUTH_TAG = "FIREBASE_AUTH_TAG";

    private SharedPrefsHelper sharedPrefsHelper;

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
                Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + " something wrong " + e);
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

    public void loginAnonymously(){

        fAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                AuthResult doc = task.getResult();
                FirebaseUser user = doc.getUser();
                String uID = user.getUid();

                Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n anonymously logged in, uID: " + uID);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("Guest", uID);
                userMap.put("User ID", uID);

                firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(uID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "\n successfully added to Users collection as guest, uID: " + uID);
                    }
                });
            }
        });
    }

    public void authWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n signInWithCredential:success");
                            AuthResult result = task.getResult();
                            if (result.getAdditionalUserInfo().isNewUser()){
                                FirebaseUser user = task.getResult().getUser();
                                String uID = user.getUid();
                                String email = user.getEmail();
                                String displayedName = user.getDisplayName();

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("Email", email);
                                userMap.put("User ID", uID);
                                userMap.put("Displayed name", displayedName);

                                firestore.collection(ConstantsPreferences.COLLECTION_USERS).document(uID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(FIREBASE_HELPER_TAG, FIRESTORE_TAG + "\n successfully added to Users collection by GoogleSignIn, Email: " + email);
                                    }
                                });
                            }else {
                                Log.d(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n signInWithCredential, EXISTED USER: " + task.getResult().getUser().getEmail());
                            }

                        } else {
                            Log.w(FIREBASE_HELPER_TAG, FIREBASE_AUTH_TAG + "\n signInWithCredential:failure ", task.getException());
                        }
                    }
                });
    }
}
