package com.example.devbox.stockhawkrewrite.model;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by devbox on 2/20/18.
 */

public class Util {

    public static final String REGEX_DATE = "\\d{0,14}";
    public static final String REGEX_PRICE = "\\d{0,7}\\.\\d{0,7}";



    public static List<IStockDto> convertStockData(Map<String, Stock> stockData) {
        //TODO implement
        return null;
    }

    public static String historicalStockQuoteToCSV(List<HistoricalQuote> historicalPriceData) {
        //TODO implement
        return null;
    }

    /**
     * converts raw historical stock data in CSV format
     * to List<Entry> usable for the Chart class
     * @param rawData raw stock price data in CSV format
     * @return entries in List<Entry> format, readty for the
     * Chart class consumption
     */
    public static List<Entry> historicalStockQuotesCSVtoEntryList(@NonNull String rawData){
        String[] lineArray;
        List<Entry> entries = new ArrayList<Entry>();
        String dateString = null;
        String priceString = null;

        if(rawData!=null){
            lineArray = rawData.split("[\n]");
            for(String valuePair : lineArray){
                String[] pairSplit = valuePair.split("[,]");
                if(pairSplit!=null && pairSplit.length == 2
                        && pairSplit[0] != null && pairSplit[1] != null) {
                    dateString = pairSplit[0].trim();
                    priceString = pairSplit[1].trim();
                    if (dateString.matches(REGEX_DATE) && priceString.matches(REGEX_PRICE)) {
                        long date = Long.parseLong(dateString);
                        float price = Float.parseFloat(priceString);
                        entries.add(new Entry(date, price));
                    }
                }
            }
            return entries;
        }
        return null;
    }

    public static List<Entry> historicalStockHistoricalQuoteListtoEntryList(List<HistoricalQuote> quotes) {
        List<Entry> result = new ArrayList<>();
        for(HistoricalQuote quote : quotes){
            if(quote.getDate().getTimeInMillis() >0 && quote.getClose()!=null){
                result.add(new Entry(quote.getDate().getTimeInMillis(), quote.getClose().floatValue()));
            }
        }

        return result;
    }
}
