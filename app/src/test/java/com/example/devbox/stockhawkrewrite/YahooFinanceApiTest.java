package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.model.IYFNetDao;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;

import junit.framework.Assert;

import org.junit.Test;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;


/**
 * test for YahooFinanceApi queries
 */

public class YahooFinanceApiTest {

    private String[] mValidStockTickers = {"TSLA", "IBM", "BA"};

    @Test
    public void yahooFinanceApiSimpleQueryTest(){
        try {
            Map<String, Stock> stockMap = YahooFinance.get(mValidStockTickers);
            for(String ticker : mValidStockTickers){
                Assert.assertTrue(stockMap.containsKey(ticker));
                Assert.assertEquals(ticker, stockMap.get(ticker).getSymbol());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
