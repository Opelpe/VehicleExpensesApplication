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
            case R.id.action_synchronize_history:

                Toast.makeText(getContext(), "synchronize", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_change_google_account:

                Toast.makeText(getContext(), "change_google_account", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_main_nav_menu, menu);

        boolean cloudStatus = presenter.getSynchronizationStatus();

        if (!cloudStatus){
            Log.d(MAIN_FRAGMENT_TAG, "on Create Options Menu : CLOUD STATUS OFF: " + cloudStatus );
            menu.findItem(R.id.action_synchronize).setIcon(R.drawable.ic_baseline_cloud_off_24);
        }else {
            Log.d(MAIN_FRAGMENT_TAG, "on Create Options Menu : CLOUD STATUS ON: " + cloudStatus);
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        try {
            ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            bar.setTitle("VEA");
            bar.show();

        } catch (Exception e) {
            Log.d(MAIN_FRAGMENT_TAG, "getsuppotrtedActionBar EXCEPTION CAPTURED: " + e);
        }
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
