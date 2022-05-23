package com.pepe.vehicleexpensesapplication.ui.feautures.refill;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.databinding.ActivityRefillBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;


public class RefillActivity extends AppCompatActivity implements RefillContract.View {

    private static final String REFIL_ACTIVITY_TAG = "REFIL_ACTIVITY_TAG";

    private ActivityRefillBinding binding;

    private RefillContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRefillBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new RefilPresenter(this, getApplicationContext());

        Toolbar toolbar = findViewById(R.id.myRefillToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.ic_baseline_local_gas_station_24);
        getSupportActionBar().setTitle("REFILL");

        Button saveRefillButton = binding.saveRefillButton;

        TextInputLayout currentMileageTIL = binding.currentMileageTIL;

        TextInputEditText currentMileageET = binding.currentMileageTIET;
        TextInputEditText refilledFuelET = binding.refilledFuelTIET;
        TextInputEditText priceOfFuelET = binding.priceOfFuelTIET;
        TextInputEditText refillNotesET = binding.refillNotesTIET;
        TextInputEditText refillDateET = binding.refillDateTIET;
        presenter.onViewCreated();

        binding.refillDateTIET.setOnClickListener(view -> {
            presenter.refillDateClicked();
        });

        saveRefillButton.setOnClickListener(view -> {
            Log.d(REFIL_ACTIVITY_TAG, "status : " + currentMileageTIL.getEditText().getText().toString().isEmpty());

            presenter.saveRefillButtonClicked(currentMileageET.getText().toString(), refillDateET.getText().toString(), refilledFuelET.getText().toString()
                    , priceOfFuelET.getText().toString(), refillNotesET.getText().toString(), binding.fullRefillCheckBox.isChecked());
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refill_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                presenter.backFromRefillActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setCurrentDateEditText(int day, int month, int year) {

        Log.d(REFIL_ACTIVITY_TAG, "get date: " + day + " / " + month + " / " + year);

        binding.refillDateTIET.setText(day + " / " + month + " / " + year);
    }

    @Override
    public void makeDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            int month = i1 + 1;
            binding.refillDateTIET.setText(i2 + " / " + month + " / " + i);
            presenter.setDateHistoryCount(i, i1, i2);
        }, presenter.getYear(), presenter.getMonth() - 1, presenter.getDay());
        datePickerDialog.show();
    }

    @Override
    public void showToast(String toastMsg) {
        Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void returnFromRefillActivity() {
        finish();
    }
}

