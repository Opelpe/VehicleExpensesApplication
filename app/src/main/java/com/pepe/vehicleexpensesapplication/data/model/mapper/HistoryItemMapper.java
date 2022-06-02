package com.pepe.vehicleexpensesapplication.data.model.mapper;

import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryItemMapper {



    public static HistoryUIModel mapToUiModel(HistoryItemModel model) {

        return new HistoryUIModel(


//                if(model != null){
//
//        }
                //formatMilage(model.CURRENT_MILEAGE),

        );
    }

    private static String formatMileage(Float m) {


HistoryUIModel historyUIModel;





//
//            private List<HistoryItemModel> getParsedItems(List<HistoryItemModel> items) {
//        List<HistoryItemModel> parsedItems = new ArrayList<>(items);
//
//        for (int i = 0; items.size() > i; i++) {
//
//            if (i != items.size() - 1) {
//                parsedItems.get(i).CURR_MILEAGE_TEXT = setCurrentMileageText(parsedItems.get(i).CURRENT_MILEAGE);
//
//                float diff = parsedItems.get(i).CURRENT_MILEAGE - parsedItems.get(i + 1).CURRENT_MILEAGE;
//                parsedItems.get(i).ADDED_MILEAGE_TEXT = setAddedMileageText(diff);
//
//                float fuelCost = parsedItems.get(i).FUEL_AMOUNT * parsedItems.get(i).FUEL_PRICE;
//                parsedItems.get(i).FUEL_COST_TEXT = setFuelCostText(fuelCost);
//
//                parsedItems.get(i).FUEL_AMOUNT_TEXT = setFuelAmountText(parsedItems.get(i).FUEL_AMOUNT);
//
//                float fuelUsage = (parsedItems.get(i).FUEL_AMOUNT * 100) / diff;
//                parsedItems.get(i).FUEL_USAGE_TEXT = setFuelUsageText(fuelUsage);
//
//                Log.w(HISTORY_FR_PRESENTER_TAG, "get parsedItems, ALL ITEMS: " + parsedItems);
//            } else {
//
//                parsedItems.get(i).CURR_MILEAGE_TEXT = setCurrentMileageText(parsedItems.get(i).CURRENT_MILEAGE);
//
//                float fuelCost = parsedItems.get(i).FUEL_AMOUNT * parsedItems.get(i).FUEL_PRICE;
//                parsedItems.get(i).FUEL_COST_TEXT = setFuelCostText(fuelCost);
//
//                parsedItems.get(i).FUEL_AMOUNT_TEXT = setFuelAmountText(parsedItems.get(i).FUEL_AMOUNT);
//
//                Log.w(HISTORY_FR_PRESENTER_TAG, "get parsedItems, LAST ITEM: " + parsedItems);
//            }
//        }
//        Log.w(HISTORY_FR_PRESENTER_TAG, "get parsedItems, before RETURN: " + parsedItems);
//        return parsedItems;
//    }

        return "";
    }

}
