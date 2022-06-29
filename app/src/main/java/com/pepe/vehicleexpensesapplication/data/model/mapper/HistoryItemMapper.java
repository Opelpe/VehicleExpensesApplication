package com.pepe.vehicleexpensesapplication.data.model.mapper;

import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;

public class HistoryItemMapper {
    private static final String H_ITEM_MAPPER_TAG = "H_ITEM_MAPPER_TAG";

    public static HistoryUIModel mapToUiModel(HistoryItemModel model) {
        return mapToUiModel(model, -1);
    }

    public static HistoryUIModel mapToUiModel(HistoryItemModel model, float millage) {

        if (millage > 0) {
            float diff = model.currentMileage - millage;

            float fuelUsage = (model.fuelAmount * 100) / diff;

            return new HistoryUIModel(formatAddedMileage(diff), formatFuelUsage(fuelUsage), formatMileage(model.currentMileage),
                    formatFuelCost(model.fuelPrice, model.fuelAmount), formatFuelAmount(model.fuelAmount), formatDate(model.defaultDate), formatFuelUsage(fuelUsage));
        } else {
            return new HistoryUIModel(formatAddedMileage(millage), formatFuelUsage(millage), formatMileage(model.currentMileage),
                    formatFuelCost(model.fuelPrice, model.fuelAmount), formatFuelAmount(model.fuelAmount), formatDate(model.defaultDate), formatFuelUsage(millage));
        }
    }

    private static String formatDate(int refillDate) {
        Log.d(H_ITEM_MAPPER_TAG, "format DATE, handled data: " + refillDate);
        String date = String.valueOf(refillDate);

        String day = date.substring(0, 2);
        String month = date.substring(2, 4);
        String year = date.substring(4, 8);

        StringBuilder dayFirst = new StringBuilder(day);
        dayFirst.append("/").append(month).append("/").append(year);
        Log.d(H_ITEM_MAPPER_TAG, "format DATE, prepared data: " + dayFirst);
        return dayFirst.toString();

    }

    private static String formatAddedMileage(float diff) {
        if (diff > 99999) {
            return ("+  --- ---");
        } else {
            if (diff > 999) {
                return "+" + String.format("%.0f", diff);
            } else {
                if (diff < 0) {
                    return "---";
                } else {
                    return "+" + String.format("%.1f", diff);
                }
            }
        }
    }

    private static String formatFuelUsage(float fuelUsage) {
        if (fuelUsage > 9999) {
            return "# , #";
        } else {
            if (fuelUsage > 999) {
                return String.format("%.0f", fuelUsage);
            } else {
                if (fuelUsage > 99) {
                    return String.format("%.1f", fuelUsage);
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

        if (fuelCost > 9999) {
            return "+9999";
        } else {
            if (fuelCost > 999) {
                return String.format("%.0f", fuelCost);
            } else {
                return String.format("%.2f", fuelCost);
            }
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
    }

}
