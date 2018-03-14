package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.room.TypeConverter;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by devbox on 2/21/18.
 */

public class HistoricalStockDataTypeConverter {

    @TypeConverter
    public static String convertEntryValuesToCSVString(List<Entry> entryList){
        return Util.historicalStockQuoteEntryListToCSVString(entryList);
    }

    @TypeConverter
    public static List<Entry> convertCSVStringToEntryList(String csvData){
        return Util.historicalStockQuotesCSVtoEntryList(csvData);
    }

}
