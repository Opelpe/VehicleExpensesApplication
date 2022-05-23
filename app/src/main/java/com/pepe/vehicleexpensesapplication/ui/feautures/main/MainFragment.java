package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.databinding.FragmentMainBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.refill.RefillActivity;


public class MainFragment extends Fragment implements MainFragmentContract.View {

    private static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";

    private FragmentMainBinding binding;

    private MainFragmentContract.Presenter presenter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        presenter = new MainFragmentPresenter(this, getContext());

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        presenter.onViewCreated();
        setHasOptionsMenu(true);

        ImageButton refillButton = binding.refillButton;
        refillButton.setOnClickListener(view -> {
            presenter.onRefillButtonClicked();
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_synchronize:

                if (presenter.checkIsAnonymous()) {
                    Toast.makeText(getContext(), "Sign in & Synchronize data", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Your data is synchronized", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_change_google_account:

                if (presenter.checkGoogleSignIn()){
                    Toast.makeText(getContext(), "You're already sign with Google", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Connect your account with Google", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_main_nav_menu, menu);

        if (presenter.checkIsAnonymous()){
            menu.findItem(R.id.action_synchronize).setIcon(R.drawable.ic_baseline_cloud_off_24);
        }else {
            menu.findItem(R.id.action_synchronize).setIcon(R.drawable.ic_baseline_cloud_queue_24);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setMainFragmentToolbar() {

        Toolbar toolbar = binding.getRoot().findViewById(R.id.mainFragmentToolbar);
        try {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            bar.setTitle("VEA");
            bar.show();
        } catch (Exception e) {
            Log.d(MAIN_FRAGMENT_TAG, "Setting MainFragment Toolbar EXCEPTION CAPTURED: " + e);
        }
    }

    @Override
    public void setAverageUsageText(String averageUsage) {
        binding.averageUsageScore.setText(averageUsage);
    }

    @Override
    public void setTravelingCostText(String travelingCost) {
        binding.travelingCostsScore.setText(travelingCost);
    }

    @Override
    public void setLatestRefillView(String currentMileage, String fuelUsage, String fuelCost) {
        binding.lRefillMileageScore.setText(currentMileage);
        binding.lRefillUsageScore.setText(fuelUsage);
        binding.lRefillPriceScore.setText(fuelCost);
    }

    @Override
    public void setTotalCostsText(String addedMileage, String money, String volume) {
        binding.tRefillAdMileageScore.setText(addedMileage);
        binding.tRefillVolumeScore.setText(volume);
        binding.tRefillCashScore.setText(money);
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(getContext(), MyMainActivity.class));
    }

    @Override
    public void startRefillActivity() {
        startActivity(new Intent(getContext(), RefillActivity.class));
    }

}
