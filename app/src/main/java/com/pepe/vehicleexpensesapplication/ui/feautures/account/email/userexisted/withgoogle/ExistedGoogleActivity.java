package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withgoogle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.pepe.vehicleexpensesapplication.databinding.ActivityExistedGoogleBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class ExistedGoogleActivity extends AppCompatActivity implements ExistedGoogleContract.View{


    private static final String EXISTED_GOOGLE_ACTIVITY_TAG = "EXISTED_GOOGLE_ACTIVITY";

    private ActivityExistedGoogleBinding binding;

    private ExistedGoogleContract.Presenter presenter;

    private TextView aboutGoogleEmailInfoTextView;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityExistedGoogleBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new ExistedGooglePresenter(this, getApplicationContext());

        aboutGoogleEmailInfoTextView = binding.aboutGoogleEmailInfoTextView;
        Button logInWithGoogleButton = binding.logInExistedUserGoogleButton;

        presenter.onViewCreated();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logInWithGoogleButton.setOnClickListener(view -> {
            presenter.onlogInWithGoogleButtonClicked(mGoogleSignInClient, gso);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(EXISTED_GOOGLE_ACTIVITY_TAG, " on Activity RESULT START, requested code" + requestCode + " RC_Sign IN : " + RC_SIGN_IN);

        if (requestCode == RC_SIGN_IN) {
            Log.d(EXISTED_GOOGLE_ACTIVITY_TAG, "\n on Activity IF requesteCode" + requestCode);

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
    }
}
