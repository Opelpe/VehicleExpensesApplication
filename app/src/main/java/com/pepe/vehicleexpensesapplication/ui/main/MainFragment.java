package com.pepe.vehicleexpensesapplication.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pepe.vehicleexpensesapplication.databinding.FragmentMainBinding;




public class MainFragment extends Fragment implements MainFragmentContract.View{

    private FragmentMainBinding binding;

    private MainFragmentContract.Presenter presenter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        presenter = new MainFragmentPresenter(this);

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
