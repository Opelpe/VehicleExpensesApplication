package com.pepe.vehicleexpensesapplication.ui.feautures.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.adapters.SettingsAdapter;
import com.pepe.vehicleexpensesapplication.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment implements SettingsContract.View {

    private static final String SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT_TAG";

    private FragmentSettingsBinding binding;

    private SettingsContract.Presenter presenter;

    private RecyclerView settingsRecycler;
    private View.OnClickListener onSettingsClickListener;

    private View.OnClickListener onSettingsWithDataClickListener;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        try {
            ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();

            if (bar != null) {
                Log.d(SETTINGS_FRAGMENT_TAG, "getsuppotrtedActionBar START");
                bar.hide();
            }
        } catch (Exception e) {
            Log.d(SETTINGS_FRAGMENT_TAG, "getsuppotrtedActionBar EXCEPTION CAPTURED: " + e);
        }


        presenter = new SettingsPresenter(this);

        presenter.onViewCreated();

        settingsRecycler = binding.settingRecyclerView;

        onSettingsClickListener = view -> {

           TextView setText = view.findViewById(R.id.settingsText);

           String clickedSet = setText.getText().toString();

            Log.d(SETTINGS_FRAGMENT_TAG, "CHOSEN SETTING: " + clickedSet + "  no data text ");

        };

        onSettingsWithDataClickListener = view -> {

            TextView setText = view.findViewById(R.id.settingsText);

            String clickedSet = setText.getText().toString();

            TextView dataText = view.findViewById(R.id.settingsDataText);
            String dataClick = dataText.getText().toString();
            Log.d(SETTINGS_FRAGMENT_TAG, "CHOSEN SETTING: " + clickedSet + " data text: " + dataClick);
        };

        settingsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        SettingsAdapter settingsAdapter = new SettingsAdapter(onSettingsClickListener, onSettingsWithDataClickListener);
        settingsAdapter.setItems(presenter.getRVItems());
        settingsRecycler.setAdapter(settingsAdapter);


        return root;
    }


}
