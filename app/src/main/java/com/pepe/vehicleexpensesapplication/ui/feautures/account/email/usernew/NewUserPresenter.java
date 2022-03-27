package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.usernew;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class NewUserPresenter implements NewUserContract.Presenter {

    private static final String NEW_USER_PRESENTER_TAG = " NEW_USER_PRESENTER_TAG";

    private NewUserContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;


    public NewUserPresenter(NewUserContract.View view, Context applicationContext) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(applicationContext);
        firebaseHelper = FirebaseHelper.getInstance(applicationContext);
    }

    @Override
    public void onViewCreated() {
        String email = sharedPrefsHelper.getEnteredEmail();
        view.setEmailEditText(email);
    }

    @Override
    public void saveNewUserButtonClicked(String enteredEmail, String enteredName, String enteredPassword) {
        Log.d(NEW_USER_PRESENTER_TAG, " " + "\n entered email: " + enteredEmail + "\n entered name: "
                + enteredName + "\n password LENGTH:" + enteredPassword.trim().length());

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        boolean passwordIsEmpty = enteredPassword.trim().isEmpty();
        boolean emailIsEmpty = enteredEmail.trim().isEmpty();
        boolean nameIsEmpty = enteredName.trim().isEmpty();
        boolean emailWrongPatten = enteredEmail.trim().matches(emailPattern);


        if (passwordIsEmpty && emailIsEmpty && nameIsEmpty) {
            view.makeToast("FILL ALL EMPTY FIELDS");
        } else {
            if (passwordIsEmpty && emailIsEmpty) {
                view.makeToast("FILL ALL EMPTY FIELDS");
            } else {
                if (passwordIsEmpty && nameIsEmpty) {
                    view.makeToast("FILL ALL EMPTY FIELDS");
                } else {
                    if (nameIsEmpty && emailIsEmpty) {
                        view.makeToast("FILL ALL EMPTY FIELDS");
                    } else {
                        if (passwordIsEmpty) {
                            view.makeToast("ENTER PASSWORD");
                        } else {
                            if (emailIsEmpty) {
                                view.makeToast("ENTER EMAIL");
                            } else {
                                if (nameIsEmpty) {
                                    view.makeToast("ENTER NAME");
                                } else {
                                    if (!emailWrongPatten) {
                                        view.makeToast("ENTER CORRECT EMAIL");
                                    } else {
                                        if (enteredPassword.trim().length() < 7) {
                                            view.makeToast("PASSWORD MUST CONTAINS 7 CHARACTERS");
                                        } else {
                                            view.showLoadingEmailDialog();
                                            firebaseHelper.fetchSignInMethodsForEmailCallback(enteredEmail).addOnCompleteListener(task -> {

                                                boolean emailNotExists = task.getResult().getSignInMethods().isEmpty();

                                                if (!emailNotExists) {
                                                    view.cancelLoadingDialog();
                                                    view.showExistedAccountDialog(enteredEmail);
                                                } else {
                                                    createNewAccount(enteredEmail, enteredPassword, enteredName);
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void checkUserProvider(String enteredEmail) {
        sharedPrefsHelper.saveEnteredEmail(enteredEmail);

        firebaseHelper.getProviderCallback(enteredEmail).addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.w(NEW_USER_PRESENTER_TAG, "Provider listen failed.", error);
            } else {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("Provider") != null) {

                        String providerName = doc.get("Provider").toString();

                        if (providerName.equals("Email & Password")) {
                    view.startExistedEmailActivity();
                } else {
                    if (providerName.equals("google.com")) {
                        view.startExistedGoogleActivity();
                    }
                }
                    }
                }
            }
        });
    }

    private void createNewAccount(String enteredEmail, String enteredPassword, String enteredName) {

        firebaseHelper.createWithEmailPasswordCallback(enteredEmail, enteredPassword, enteredName)
                .addOnCompleteListener(task -> {
                    AuthResult doc = task.getResult();
                    FirebaseUser user = doc.getUser();
                    String uID = user.getUid();

                    sharedPrefsHelper.saveSignedUserID(uID);
                    sharedPrefsHelper.saveSignWEmailEmail(user.getEmail());
                    sharedPrefsHelper.saveGoogleSignInCompleted(false);
                    sharedPrefsHelper.saveSignWGoogleEmail(null);
                    sharedPrefsHelper.saveIsAnonymous(false);
                    sharedPrefsHelper.saveSignedUserEmail(user.getEmail());

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Email", enteredEmail);
                    userMap.put("Password", enteredPassword);
                    userMap.put("Name", enteredName);
                    userMap.put("User ID", uID);

                    firebaseHelper.firestoreUIDUsersCallback(uID)
                            .set(userMap);

                    Map<String, Object> providerMap = new HashMap<>();
                    providerMap.put("Provider", ConstantsPreferences.PROVIDERS_EMAIL_PASSWORD);
                    providerMap.put("Email", enteredEmail);
                    providerMap.put("UID", uID);
                    firebaseHelper.firestoreUIDProvidersCallback()
                            .document(uID)
                            .set(providerMap)
                            .addOnCompleteListener(task12 -> {
                                Log.d(NEW_USER_PRESENTER_TAG, " success saving EMAIL providers");
                            })
                            .addOnFailureListener(e -> Log.d(NEW_USER_PRESENTER_TAG, "saving EMAIL providers something wrong: " + e));

                    view.cancelLoadingDialog();
                    view.makeToast("CREATED ACCOUNT \n" + enteredEmail);
                    view.startMyMainActivity();

                }).addOnFailureListener(e -> {

            sharedPrefsHelper.saveSignWEmailEmail(null);
            sharedPrefsHelper.saveSignedUserEmail(null);
            Log.d(NEW_USER_PRESENTER_TAG, "create with EMAIL something goes wrong: " + e);
        });
    }

    @Override
    public void setTransformationMethod(TransformationMethod transformationMethod) {

        if (transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            view.transformationHide();
        }
        if (transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
            view.transformationShow();
        }
    }
}
