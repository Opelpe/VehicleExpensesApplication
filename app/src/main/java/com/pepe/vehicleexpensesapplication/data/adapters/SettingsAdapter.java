package com.pepe.vehicleexpensesapplication.data.adapters;

import static com.pepe.vehicleexpensesapplication.R.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;


public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context settingsContext;
    private View.OnClickListener onSettingsClickListener;

    private View.OnClickListener onSettingsWithDataClickListener;

    private SharedPrefsHelper sharedPrefsHelper;

    private List<SettingsUiModel> settingsUiModelList = new ArrayList<>();

    private final static int TYPE_SECTION = 1;
    private final static int TYPE_SETTINGS_NO_DATA = 2;
    private final static int TYPE_SETTINGS_WITH_DATA = 3;
    private View.OnClickListener dataSettingsListener;


    public SettingsAdapter(View.OnClickListener listener, View.OnClickListener onSettingsWithDataClickListener, View.OnClickListener dataSettingsListener, Context settingsContext) {
        this.onSettingsClickListener = listener;
        this.onSettingsWithDataClickListener = onSettingsWithDataClickListener;
        this.dataSettingsListener = dataSettingsListener;
        this.settingsContext = settingsContext;
    }

    private boolean isSyncTitle(String itemTitle) {
        return itemTitle.equals("SYNCHRONIZATION");

    }

    private boolean isCurencyTitle(String itemTitle) {
        return itemTitle.equals("CURRENCY");
    }

    private String cloudSyncText() {
        return settingsContext.getString(string.cloud_sync_text).replaceAll("\"", "");
    }

    private String offlineText() {
        return settingsContext.getString(string.offline_text).replaceAll("[^\\p{L}\\p{Z}]", "");
    }

    private String onlineText() {
        return settingsContext.getString(string.online_text).replaceAll("[^\\p{L}\\p{Z}]", "");
    }

    private String currencyText() {
        return settingsContext.getString(string.price_units).replaceAll("[^\\p{L}\\p{Z}]", "");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_SECTION) {

            View sectionView = LayoutInflater.from(parent.getContext()).inflate(layout.item_settings_section, parent, false);

            sharedPrefsHelper = new SharedPrefsHelper(parent.getContext());

            SectionViewHolder sectionViewHolder = new SectionViewHolder(sectionView);
            return sectionViewHolder;

        } else {
            if (viewType == TYPE_SETTINGS_NO_DATA) {

                View settingsNoDataView = LayoutInflater.from(parent.getContext()).inflate(layout.item_settings_no_data, parent, false);

                SettingsNoDataViewHolder settingsNoDataHolder = new SettingsNoDataViewHolder(settingsNoDataView);
                settingsNoDataHolder.itemView.setOnClickListener(onSettingsClickListener);

                return settingsNoDataHolder;

            } else {
//                view type == TYPE_SETTINGS_WITH_DATA

                View settingWithDataView = LayoutInflater.from(parent.getContext()).inflate(layout.item_settings_with_data, parent, false);
                SettingsWithDataViewHolder settingWithDataHolder = new SettingsWithDataViewHolder(settingWithDataView);
                settingWithDataHolder.itemView.setOnClickListener(onSettingsWithDataClickListener);

                return settingWithDataHolder;

            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

//                    TEST
        if (holder instanceof SectionViewHolder) {
            SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;

        } else {
            if (holder instanceof SettingsNoDataViewHolder) {
                SettingsNoDataViewHolder settingsViewHolder = (SettingsNoDataViewHolder) holder;
                settingsViewHolder.settingsText.setText(settingsUiModelList.get(position).title);

            } else {
                if (holder instanceof SettingsWithDataViewHolder) {
                    SettingsWithDataViewHolder settingsWithDataViewHolder = (SettingsWithDataViewHolder) holder;

                    settingsWithDataViewHolder.settingsText.setText(settingsUiModelList.get(position).title);
                    if (isCurencyTitle(settingsUiModelList.get(position).title)) {
                        settingsWithDataViewHolder.dataSettingsText.setText(currencyText());
                    }
                    if (isSyncTitle(settingsUiModelList.get(position).title)) {
                        if (sharedPrefsHelper.getIsAnonymous()) {
                            settingsWithDataViewHolder.dataSettingsText.setText(new StringBuilder().append(cloudSyncText()).append(offlineText()).toString());
                            settingsWithDataViewHolder.itemView.setOnClickListener(dataSettingsListener);
                        } else {
                            settingsWithDataViewHolder.dataSettingsText.setText(String.format("%s%s", cloudSyncText(), onlineText()));
                        }
                    }

                    if (settingsUiModelList.get(position).title.equals("CAPACITY UNITS")) {
                        settingsWithDataViewHolder.dataSettingsText.setText("LITTERS");
                    }
                    if (settingsUiModelList.get(position).title.equals("AVERAGE USAGE UNITS")) {
                        settingsWithDataViewHolder.dataSettingsText.setText("L/100KM");
                    }
                    if (settingsUiModelList.get(position).title.equals("DATE FORMAT")) {
                        settingsWithDataViewHolder.dataSettingsText.setText("EX: 01.01.1999");
                    }
                    if (settingsUiModelList.get(position).title.equals("MILEAGE UNITS")) {
                        settingsWithDataViewHolder.dataSettingsText.setText("KM");
                    }
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        SettingsUiModel uiModel = settingsUiModelList.get(position);

        if (uiModel.type == SettingsUiModel.Type.SectionHeader) {
            return TYPE_SECTION;
        } else {
            if (uiModel.type == SettingsUiModel.Type.NoDataHeader) {
                return TYPE_SETTINGS_NO_DATA;
            } else
                return TYPE_SETTINGS_WITH_DATA;
        }
    }

    @Override
    public int getItemCount() {
        return settingsUiModelList.size();
    }

    public void setItems(List<SettingsUiModel> list) {
        settingsUiModelList = list;

    }

    class SettingsNoDataViewHolder extends RecyclerView.ViewHolder {

        TextView settingsText;

        public SettingsNoDataViewHolder(@NonNull View itemView) {
            super(itemView);

            settingsText = itemView.findViewById(id.settingsText);

        }
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView sectionHeaderText;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionHeaderText = itemView.findViewById(id.settingsHeaderTextView);

        }
    }

    class SettingsWithDataViewHolder extends RecyclerView.ViewHolder {

        TextView settingsText;
        TextView dataSettingsText;

        public SettingsWithDataViewHolder(@NonNull View itemView) {
            super(itemView);

            dataSettingsText = itemView.findViewById(id.settingsDataText);
            settingsText = itemView.findViewById(id.settingsText);

        }
    }

}
