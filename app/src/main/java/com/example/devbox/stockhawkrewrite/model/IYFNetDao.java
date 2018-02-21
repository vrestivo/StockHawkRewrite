package com.example.devbox.stockhawkrewrite.model;
import java.util.List;

/**
 * Interface for YahooFinance Network Data Access Object
 */

public interface IYFNetDao {
    List<StockDto> fetchStocks(String[] tickers);
}
