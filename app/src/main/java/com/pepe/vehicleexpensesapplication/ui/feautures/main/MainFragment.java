package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.databinding.FragmentMainBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;


public class MainFragment extends Fragment implements MainFragmentContract.View {

    private static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";

    private FragmentMainBinding binding;

    private MainFragmentContract.Presenter presenter;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private Drawable cloudddd;
    private Drawable clouddddOff;

    private MenuItem menuItem2;
    private Menu optionMenu;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        presenter = new MainFragmentPresenter(this, getContext());

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        presenter.onViewCreated();

        cloudddd = AppCompatResources.getDrawable(getContext(), R.drawable.ic_baseline_cloud_queue_24);
        clouddddOff = AppCompatResources.getDrawable(getContext(), R.drawable.ic_baseline_cloud_off_24);


        setHasOptionsMenu(true);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(getContext(), gso);


        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        menuItem2 = item;

        switch (item.getItemId()) {
            case R.id.action_synchronize:

                presenter.actionChangeGooleAccountClicked(gso, gsc);

//                if (jakiItem.equals(cloudddd)){
//                    Toast.makeText(getContext(), "synchronize data", Toast.LENGTH_SHORT).show();
//                    item.setIcon(R.drawable.ic_baseline_cloud_off_24);
//                }else {
//                    if (jakiItem.equals(clouddddOff)){
//                        Toast.makeText(getContext(), "synchronize data", Toast.LENGTH_SHORT).show();
//                        item.setIcon(R.drawable.ic_baseline_cloud_queue_24);
//                    }else {
//                        Toast.makeText(getContext(), "CANNOT synchronize data", Toast.LENGTH_SHORT).show();
//                        item.setIcon(R.drawable.ic_baseline_cloud_off_24);
//                    }
//                }
                return true;
            case R.id.action_change_google_account:

                Toast.makeText(getContext(), "change_google_account", Toast.LENGTH_SHORT).show();
//                presenter.actionChangeGooleAccountClicked(gso, gsc);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_main_nav_menu, menu);
        optionMenu = menu;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setMainFragmentToolbar() {
        try {
            ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            bar.setIcon(R.drawable.ic_baseline_local_gas_station_24);
            bar.setTitle("VEA");
            if (!bar.isShowing()) {
                Log.d(MAIN_FRAGMENT_TAG, "getsuppotrtedActionBar START");
                bar.show();
            }
        } catch (Exception e) {
            Log.d(MAIN_FRAGMENT_TAG, "getsuppotrtedActionBar EXCEPTION CAPTURED: " + e);
        }
    }

    @Override
    public void presenterStartActivityForResult(Intent signInIntent, int rcSignIn) {

        startActivityForResult(signInIntent, rcSignIn);
    }

    @Override
    public void showLoadingGoogleDialog(String googleEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.dialog_check_email);
        AlertDialog dialogg = builder.create();
        dialogg.show();
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(getContext(), MyMainActivity.class));
    }

    @Override
    public void setSynchornizationImageViewOn() {
        optionMenu.findItem(R.id.action_synchronize).setIcon(R.drawable.ic_baseline_cloud_queue_24);
    }

    @Override
    public void setSynchornizationImageViewOff() {
        optionMenu.findItem(R.id.action_synchronize).setIcon(R.drawable.ic_baseline_cloud_off_24);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d(MAIN_FRAGMENT_TAG, " on Activity RESULT START, requested code: " + requestCode + " RC_Sign IN : " + RC_SIGN_IN + " result code: " + resultCode);

        if (requestCode == RC_SIGN_IN) {
            Log.d(MAIN_FRAGMENT_TAG, "\n on Activity IF requesteCode: " + requestCode);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);


            presenter.handleSignInResult(task);
        } else {
            Log.d(MAIN_FRAGMENT_TAG, "\n on Activity IF DIDNT PASSED requesteCode: " + requestCode);
        }
    }
}
