package com.example.devbox.stockhawkrewrite.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Interface for YahooFinance Network Data Access Object
 */

public interface IYFNetDao {
    List<StockDto> fetchStocks();
    Map<String, Stock> retrieveStockDataFromYahoo(String[] stockTickers);
}
