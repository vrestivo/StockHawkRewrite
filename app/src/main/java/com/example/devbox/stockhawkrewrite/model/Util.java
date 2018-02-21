package com.example.devbox.stockhawkrewrite.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by devbox on 2/20/18.
 */

public class Util {
    public static List<IStockDto> convertStockData(Map<String, Stock> stockData) {
        //TODO implement
        return null;
    }

    public static String historicalStockQuoteToCSV(List<HistoricalQuote> historicalPriceData) {
        //TODO implement
        return null;
    }

    public static List<Entry> historicalStockQuotesCSVtoEntryList(String csvData) {
        //TODO implement
        return null;
    }

    public static List<Entry> historicalStockHistoricalQuoteListtoEntryList(List<HistoricalQuote> quotes) {
        //TODO implement
        return null;
    }
}
