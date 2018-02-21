package com.example.devbox.stockhawkrewrite.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by devbox on 2/21/18.
 */

public class HistoricalStockDataTypeConverter {

    public static String convertEntryValuesToCSVString(List<Entry> entryList){
        return Util.historicalStockQuoteEntryListToCSVString(entryList);
    }

}
