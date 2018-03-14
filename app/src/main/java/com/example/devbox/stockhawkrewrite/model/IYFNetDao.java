package com.example.devbox.stockhawkrewrite.model;
import com.example.devbox.stockhawkrewrite.exceptions.StockHawkException;

import java.util.List;

/**
 * Interface for YahooFinance Network Data Access Object
 */

public interface IYFNetDao {
    List<StockDto> fetchStocks(String[] tickers) throws StockHawkException;
    StockDto fetchASingleStock(String ticker) throws StockHawkException;
}
