package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.adapters.HistoryAdapter;
import com.pepe.vehicleexpensesapplication.databinding.FragmentHistoryBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.refill.RefillActivity;

public class HistoryFragment extends Fragment implements HistoryContract.View {

    private static final String HISTORY_FRAGMENT_TAG = "HISTORY_FRAGMENT_TAG";

    private FragmentHistoryBinding binding;

    //    private HistoryFragment(){
//        presenter = new HistoryPresenter(this);
//    }
    private HistoryContract.Presenter presenter;
    private RecyclerView historyRecycler;
    private View.OnClickListener historyListener;


    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        presenter = new HistoryPresenter(this, getContext());

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHasOptionsMenu(true);

        presenter.onViewCreated();

        FloatingActionButton floatingRefilButton = binding.floatingRefillButton;
        floatingRefilButton.setOnClickListener(view -> {
            presenter.onFloatingRefillButtonClicked();
        });

        historyRecycler = binding.historyRecyclerView;

        historyListener = view -> {

            Log.d(HISTORY_FRAGMENT_TAG, "text clicked");

        };
        historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryAdapter historyAdapter = new HistoryAdapter(historyListener);

        historyRecycler.setAdapter(historyAdapter);

        return root;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_synchronize_history:

                Toast.makeText(getContext(), "synchronize data", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.history_nav_menu, menu);

        boolean cloudStatus = presenter.getSynchronizationStatus();

        if (!cloudStatus){
            Log.d(HISTORY_FRAGMENT_TAG, "on Create Options Menu : CLOUD STATUS OFF: " + cloudStatus );
            menu.findItem(R.id.action_synchronize_history).setIcon(R.drawable.ic_baseline_cloud_off_24);
        }else {
            Log.d(HISTORY_FRAGMENT_TAG, "on Create Options Menu : CLOUD STATUS ON: " + cloudStatus);
            menu.findItem(R.id.action_synchronize_history).setIcon(R.drawable.ic_baseline_cloud_queue_24);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setHistoryFragmentToolbar() {

        Toolbar toolbar = binding.getRoot().findViewById(R.id.historyFragmentToolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        try {
            ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            bar.setLogo(R.drawable.ic_baseline_local_gas_station_24);
            bar.setTitle("HISTORY");
            bar.show();

        } catch (Exception e) {
            Log.d(HISTORY_FRAGMENT_TAG, "getsuppotrtedActionBar EXCEPTION CAPTURED: " + e);
        }

    }

    @Override
    public void startRefilActivity() {
        startActivity(new Intent(getContext(), RefillActivity.class));
    }
}
