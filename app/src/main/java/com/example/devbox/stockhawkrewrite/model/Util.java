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



    public static String historicalStockQuoteEntryListToCSVString(List<Entry> historicalPriceData) {
        StringBuilder stringBuilder = new StringBuilder();
        if(historicalPriceData!=null){
            for(Entry entry : historicalPriceData){
                if(entry!=null) {
                    stringBuilder.append(String.valueOf((long)entry.getX()))
                            .append(", ")
                            .append(entry.getY())
                            .append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }


    public static List<StockDto> convertStockMapToStockDtoList(Map<String, Stock> stockData) {
        List<StockDto> results = new ArrayList<>();
        if(stockData!=null && stockData.size() > 0) {
            for (Stock stock : stockData.values()) {
                results.add(new StockDto(stock));
            }
        }
        return results;
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

    /**
     * converts stock data from List<HistoricalQuote> List<Entry> format
     * @param quotes
     * @return
     */
    public static List<Entry> historicalStockHistoricalQuoteListtoEntryList(List<HistoricalQuote> quotes) {
        List<Entry> result = new ArrayList<>();
        if(quotes!=null && quotes.size() > 0) {
            for (HistoricalQuote quote : quotes) {
                if (quote.getDate().getTimeInMillis() > 0 && quote.getClose() != null) {
                    result.add(new Entry(quote.getDate().getTimeInMillis(), quote.getClose().floatValue()));
                }
            }
        }

        return result;
    }




}
