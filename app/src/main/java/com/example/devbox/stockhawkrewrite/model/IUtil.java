package com.example.devbox.stockhawkrewrite.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by devbox on 2/20/18.
 */

public interface IUtil {
    List<IStockDto> convertStockData(Map<String, Stock> stockData);
    String historicalStockQuoteToCSV(List<HistoricalQuote> historicalPriceData);
    List<Entry> historicalStockQuotesCSVtoEntryList(String csvData);
}
