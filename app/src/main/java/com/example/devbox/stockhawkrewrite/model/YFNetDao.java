package com.example.devbox.stockhawkrewrite.model;

import java.util.List;
import java.util.Map;

import yahoofinance.Stock;

/**
 * Created by devbox on 2/20/18.
 */

public class YFNetDao implements IYFNetDao {

    @Override
    public List<StockDto> fetchStocks() {
        return null;
    }

    @Override
    public Map<String, Stock> retrieveStockDataFromYahoo(String[] stockTickers) {
        return null;
    }


}

