package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.adapters.HistoryAdapter;
import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.databinding.FragmentHistoryBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.refill.RefillActivity;

import java.util.List;

public class HistoryFragment extends Fragment implements HistoryContract.View {

    private static final String HISTORY_FRAGMENT_TAG = "HISTORY_FRAGMENT_TAG";

    private FragmentHistoryBinding binding;

    //    private HistoryFragment(){
//        presenter = new HistoryPresenter(this);}

    private HistoryContract.Presenter presenter;
    private RecyclerView historyRecycler;
    private HistoryAdapter historyAdapter;

    private AlertDialog.Builder deleteItemDialogBuilder;
    private AlertDialog deleteItemDialog;

    private final HistoryAdapter.HistoryItemListener historyItemListener = (itemID) ->
            Log.w(HISTORY_FRAGMENT_TAG, "on Item Clicked ID: " + itemID);


    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(binding.getRoot().getContext());

        presenter = new HistoryPresenter(this, sharedPrefsHelper);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true);

        deleteItemDialogBuilder = new AlertDialog.Builder(getContext());

        presenter.onViewCreated();

        FloatingActionButton floatingRefillButton = binding.floatingRefillButton;
        floatingRefillButton.setOnClickListener(view -> {
            presenter.onFloatingRefillButtonClicked();
        });

        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_synchronize_history:

                if (presenter.checkIsAnonymous()) {
                    Toast.makeText(getContext(), "Sign in & Synchronize data", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Your data is synchronized", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.history_nav_menu, menu);

        if (presenter.checkIsAnonymous()) {
            menu.findItem(R.id.action_synchronize_history).setIcon(R.drawable.ic_baseline_cloud_off_24);
        } else {
            menu.findItem(R.id.action_synchronize_history).setIcon(R.drawable.ic_baseline_cloud_queue_24);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setHistoryItems(List<HistoryUIModel> uiModels, List<HistoryItemModel> itemModels, HistoryAdapter.DeleteItemListener deleteItemListener) {
        historyRecycler = binding.historyRecyclerView;
        historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new HistoryAdapter(historyItemListener, uiModels, itemModels, deleteItemListener);
        historyRecycler.setAdapter(historyAdapter);

    }

    @Override
    public void showToast(String toastMsg) {
        Toast.makeText(binding.getRoot().getContext(), toastMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeleteItemDialog() {
        deleteItemDialogBuilder.setMessage("Do you want to delete this?");
        deleteItemDialogBuilder.setPositiveButton("DELETE", (dialogInterface, i) -> presenter.deleteItemConfirmed());
        deleteItemDialogBuilder.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());
        deleteItemDialog = deleteItemDialogBuilder.create();
        deleteItemDialog.show();
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
            Log.d(HISTORY_FRAGMENT_TAG, "set History Fragment Toolbar EXCEPTION CAPTURED: " + e);
        }
    }

    @Override
    public void startRefillActivity() {
        startActivity(new Intent(getContext(), RefillActivity.class));
    }

}
