package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.suggestedevents.ViewOnClickListener;
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

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        presenter = new HistoryPresenter(this);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
