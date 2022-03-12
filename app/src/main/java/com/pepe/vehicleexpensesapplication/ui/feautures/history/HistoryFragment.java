package com.pepe.vehicleexpensesapplication.ui.feautures.history;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.suggestedevents.ViewOnClickListener;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.adapters.HistoryAdapter;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.databinding.FragmentHistoryBinding;

import java.util.function.Consumer;

public class HistoryFragment extends Fragment implements HistoryContract.View {

    private static final String HISTORY_FRAGMENT_TAG = "HISTORY_FRAGMENT_TAG";

    private FragmentHistoryBinding binding;

    //    private HistoryFragment(){
//        presenter = new HistoryPresenter(this);
//    }
    private HistoryContract.Presenter presenter;
    private RecyclerView historyRecycler;
    private View.OnClickListener historyListener;

    private Menu optionMenu;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        presenter = new HistoryPresenter(this);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        presenter.onViewCreated();

        try {
            ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();

            if (!bar.isShowing()) {
                Log.d(HISTORY_FRAGMENT_TAG, "getsuppotrtedActionBar START");
                bar.show();
                bar.setIcon(R.drawable.ic_baseline_local_gas_station_24);
            }
        } catch (Exception e) {
            Log.d(HISTORY_FRAGMENT_TAG, "getsuppotrtedActionBar EXCEPTION CAPTURED: " + e);
        }

        setHasOptionsMenu(true);

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
            case R.id.action_synchronize:
                Toast.makeText(getContext(), "synchronize data", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        optionMenu = menu;

        inflater.inflate(R.menu.history_nav_menu, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setSynchornizationImageViewOff() {
        optionMenu.findItem(R.id.action_synchronize).setIcon(R.drawable.ic_baseline_cloud_off_24);
    }

    @Override
    public void setSynchornizationImageViewOn() {
        optionMenu.findItem(R.id.action_synchronize).setIcon(R.drawable.ic_baseline_cloud_queue_24);
    }
}
