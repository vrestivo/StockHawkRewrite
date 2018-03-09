package com.example.devbox.stockhawkrewrite.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by devbox on 2/20/18.
 */

public class YFNetDao implements IYFNetDao {

    @Override
    public List<StockDto> fetchStocks(String[] tickers) throws IOException {
        List<StockDto> result = new ArrayList<>();
        if (tickers != null) {
            Map<String, Stock> stockData = YahooFinance.get(tickers);
            result = Util.convertStockMapToStockDtoList(stockData);
        }
        return result;
    }

}

