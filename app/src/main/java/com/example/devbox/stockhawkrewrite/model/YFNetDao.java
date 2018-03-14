package com.example.devbox.stockhawkrewrite.model;

import com.example.devbox.stockhawkrewrite.exceptions.InvalidStockException;
import com.example.devbox.stockhawkrewrite.exceptions.StockHawkException;
import com.example.devbox.stockhawkrewrite.exceptions.UnableToDownloadDataException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Network DAO for accessing Yahoo Finance data
 */

public class YFNetDao implements IYFNetDao {

    @Override
    public List<StockDto> fetchStocks(String[] tickers) throws StockHawkException {
        List<StockDto> result;


        if (tickers == null) {
            throw new InvalidStockException("StockList is Empty");
        }

        Map<String, Stock> stockData;
        try {
            stockData = YahooFinance.get(tickers);
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            throw new InvalidStockException("Invalid Stock detected");
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnableToDownloadDataException("Unable to download stock data");
        }
        if (stockData == null) {
            throw new UnableToDownloadDataException("Unable to download stock data");
        }

        result = Util.convertStockMapToStockDtoList(stockData);

        return result;
    }


    @Override
    public StockDto fetchASingleStock(String ticker) throws StockHawkException {

        if (ticker == null) {
            throw new InvalidStockException("Invalid Stock");
        }

        Stock stock;
        try {
            stock = YahooFinance.get(ticker);
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            throw new InvalidStockException("Invalid Stock");
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnableToDownloadDataException("Unable to download data for " + ticker);
        }

        if (stock == null) {
            throw new UnableToDownloadDataException("Unable to download data for " + ticker);
        }

        return new StockDto(stock);
    }
}

