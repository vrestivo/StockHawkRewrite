package com.example.devbox.stockhawkrewrite;

import junit.framework.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;


/**
 * test for YahooFinanceApi queries
 */

public class YahooFinanceApiTest {

    private Map<String, Stock> mResults;
    private Stock mResult;


    @Test
    public void yahooFinanceApiSimpleQueryTest() {
        String[] valid = givenArrayOfValidTickers();
        whenSearchforMultipleTickers(valid);
        returnsStocksWithValidTickers(valid);
    }

    private String[] givenArrayOfValidTickers() {
        String valid[] = {"TSLA", "IBM", "BA"};
        return valid;
    }

    private void whenSearchforMultipleTickers(String[] validTickers) {
        try {
            mResults = YahooFinance.get(validTickers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnsStocksWithValidTickers(String[] validTickers) {
        for (String ticker : validTickers) {
            Assert.assertTrue(mResults.containsKey(ticker));
            Assert.assertEquals(ticker, mResults.get(ticker).getSymbol());
        }
    }




    @Test
    public void yahooFinanceApiValidAndInvalidStocks() {
        String[] validAndInvalid = givenValidAndInvalidTickers();
        whenSearchforMultipleTickers(validAndInvalid);
        returnsStocksWithValidTickers(givenArrayOfValidTickers());
    }

    private String[] givenValidAndInvalidTickers() {
        String[] validAndInvalidStockTickers = {"TSLA", "IBM", "ZZZZZZ", "XXXXX", "BA"};
        return validAndInvalidStockTickers;
    }




    @Test(expected = FileNotFoundException.class)
    public void yahooFinanceMultipleInvalidQueriesTest() throws IOException {
        String[] invalid = givenOnlyInvalidTickers();
        whenSearchforMultipleInvalidTickers(invalid);
        mResultsAreNull();
    }

    private String[] givenOnlyInvalidTickers() {
        String[] invalid = {"ZZZZZZ", "XXXXX"};
        return invalid;
    }

    private void whenSearchforMultipleInvalidTickers(String[] invalidTickers) throws IOException {
        mResults = YahooFinance.get(invalidTickers);
    }

    private void mResultsAreNull() {
        Assert.assertNull(mResults);
    }




    @Test(expected = FileNotFoundException.class)
    public void yahooFinanceSingleInvalidStockQueryTest() throws IOException {
        String invalid = givenASingleInvalidTicker();
        whenSearchForASingleTickerThrowsFileNotFoundException(invalid);
    }

    private String givenASingleInvalidTicker() {
        return "ZZZZZZ";
    }

    private void whenSearchForASingleTickerThrowsFileNotFoundException(String invalidTicker) throws IOException {
        mResult = YahooFinance.get(invalidTicker);
    }




    @Test
    public void yahooFinanceSingleValidStockTest(){
        String valid = givenASingleValidTicker();
        whenSearchForASingleValidTicker(valid);
        returnsValidStock(valid);
    }

    private String givenASingleValidTicker() {
        return "TSLA";
    }

    private void whenSearchForASingleValidTicker(String ticker) {
        try {
            mResult = YahooFinance.get(ticker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnsValidStock(String validTicker) {
        Assert.assertEquals(validTicker, mResult.getSymbol());
    }


}
