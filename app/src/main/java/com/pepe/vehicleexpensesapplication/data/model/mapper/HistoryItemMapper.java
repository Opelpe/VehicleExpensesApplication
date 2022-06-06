package com.pepe.vehicleexpensesapplication.data.model.mapper;

import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;

import java.util.List;

public class HistoryItemMapper {


    public static HistoryUIModel mapToUiModel(HistoryItemModel model) {
        return mapToUiModel(model, -1);
    }


    public static HistoryUIModel mapToUiModel(HistoryItemModel model, float millage) {

        if (millage > 1) {
            float diff = model.CURRENT_MILEAGE - millage;

            float fuelUsage = (model.FUEL_AMOUNT * 100) / diff;

            return new HistoryUIModel(formatAddedMileage(diff), formatFuelUsage(fuelUsage), formatMileage(model.CURRENT_MILEAGE),
                    formatFuelCost(model.FUEL_PRICE, model.FUEL_AMOUNT), formatFuelAmount(model.FUEL_AMOUNT));
        } else {
            return new HistoryUIModel(formatAddedMileage(millage), formatFuelUsage(millage), formatMileage(model.CURRENT_MILEAGE),
                    formatFuelCost(model.FUEL_PRICE, model.FUEL_AMOUNT), formatFuelAmount(model.FUEL_AMOUNT));
        }
    }

    private static String formatAddedMileage(float diff) {
        if (diff > 999) {
            return "+" + String.format("%.0f", diff);
        } else {
            if (diff > 99999) {
                return ("+  --- ---");
            }
            if (diff < 0){
                return "---";
            }else{
                return "+" + String.format("%.1f", diff);
            }

        }
    }

    private static String formatFuelUsage(float fuelUsage) {
        if (fuelUsage > 99) {
            return String.format("%.1f", fuelUsage);
        } else {
            if (fuelUsage > 999) {
                return String.format("%.0f", fuelUsage);
            } else {
                if (fuelUsage > 9999) {
                    return "# , #";
                } else {
                    if (fuelUsage < 0) {
                        return "---,--";
                    } else {
                        return String.format("%.2f", fuelUsage);
                    }
                }
            }
        }
    }

    private static String formatFuelCost(float fp, float fa) {

        float fuelCost = fp * fa;

        if (fuelCost > 999) {
            return String.format("%.0f", fuelCost);
        } else {
            if (fuelCost > 9999) {
                return "+9999";
            }
            return String.format("%.2f", fuelCost);
        }
    }

    private static String formatFuelAmount(float fuel) {
        if (fuel > 9999) {
            return "+9999";
        } else {
            return "+" + String.format("%.0f", fuel);
        }
    }

    private static String formatMileage(Float m) {

        if (m > 999999) {
            return "+999999";
        } else {
            return String.format("%.1f", m);
        }

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
    }

}
