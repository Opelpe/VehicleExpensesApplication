package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withgoogle;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.databinding.ActivityExistedGoogleBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class ExistedGoogleActivity extends AppCompatActivity implements ExistedGoogleContract.View {


    private static final String EXISTED_GOOGLE_ACTIVITY_TAG = "EXISTED_GOOGLE_ACTIVITY";

    private ActivityExistedGoogleBinding binding;

    private ExistedGoogleContract.Presenter presenter;

    private TextView aboutGoogleEmailInfoTextView;

    private GoogleSignInClient googleSignInClient;

    private AlertDialog.Builder builder;
    private AlertDialog googleLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityExistedGoogleBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new ExistedGooglePresenter(this, getApplicationContext());

        builder = new AlertDialog.Builder(this);

        aboutGoogleEmailInfoTextView = binding.aboutGoogleEmailInfoTextView;
        Button logInWithGoogleButton = binding.logInExistedUserGoogleButton;

        presenter.onViewCreated();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        logInWithGoogleButton.setOnClickListener(view -> {
            presenter.onlogInWithGoogleButtonClicked(googleSignInClient);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        googleSignInClient.signOut();

        Log.d(EXISTED_GOOGLE_ACTIVITY_TAG, " on Activity RESULT START, requested code" + requestCode + " RC_Sign IN : " + ConstantsPreferences.RC_SIGN_IN);

        if (requestCode == ConstantsPreferences.RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            presenter.handleSignInResult(task);
        }
    }

    @Override
    public void setGoogleEmailInfo(String enteredEmail) {
        aboutGoogleEmailInfoTextView.setText("Adress " + enteredEmail + " was already used. To continue, log in with Google.");
    }

    @Override
    public void presenterStartActivityForResult(Intent signInIntent, int rcSignIn) {
        startActivityForResult(signInIntent, rcSignIn);
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
        if (googleLoadingDialog != null){
            if (googleLoadingDialog.isShowing()) {
                googleLoadingDialog.cancel();
            }}
    }

    @Override
    public void showLoadingGoogleDialog() {
        builder.setView(R.layout.dialog_check_email);
        googleLoadingDialog = builder.create();
        googleLoadingDialog.show();
    }

    @Override
    public void showToast(String toastMsg) {
        Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancelLoadingDialog() {
        googleLoadingDialog.cancel();
    }
}
