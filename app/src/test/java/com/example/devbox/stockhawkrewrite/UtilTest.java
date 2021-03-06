package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.Util.Util;
import com.github.mikephil.charting.data.Entry;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Test for Util class' conversion methods
 */

public class UtilTest {

    private Stock mMalformedStock;
    private StockDto mStockDtoUnderTest;

    public static String sTestData = "1454313600000,\n" +
            "1456819200000, 55.230000\n" +
            "1459494000000, 49.869999\n" +
            "1462086000000, 53.000000\n" +
            "1464764400000, 51.169998\n" +
            "1467356400000, 56.680000\n" +
            "1470034800000, 57.459999\n" +
            "1472713200000, 57.599998\n" +
            "1475305200000, 59.919998\n" +
            "1477983600000, 60.259998\n" +
            "1480579200000, 62.139999\n" +
            "1483257600000, 64.650002\n" +
            "1485936000000, 63.980000\n" +
            "1488355200000, 65.860001\n" +
            "1491030000000, 68.459999\n" +
            "1493622000000, 69.839996\n" +
            "1496300400000, 68.930000\n" +
            "1498892400000, 72.699997\n" +
            "1501570800000, 74.769997\n" +
            "1504249200000, 74.489998\n" +
            "1506841200000, 83.180000\n" +
            "1509519600000, 84.169998\n" +
            "1512115200000, 85.540001\n" +
            "1514793600000, 95.010002\n" +
            "1517472000000, 89.610001\n" +
            "1518076800000, 85.010002\n";

    public static float[] sTestFloatValues = {
            55.230000F,
            49.869999F,
            53.000000F,
            51.169998F,
            56.680000F,
            57.459999F,
            57.599998F,
            59.919998F,
            60.259998F,
            62.139999F,
            64.650002F,
            63.980000F,
            65.860001F,
            68.459999F,
            69.839996F,
            68.930000F,
            72.699997F,
            74.769997F,
            74.489998F,
            83.180000F,
            84.169998F,
            85.540001F,
            95.010002F,
            89.610001F,
            85.010002F,
    };

    public static long[] sTestLongVaues = {
            1456819200000L,
            1459494000000L,
            1462086000000L,
            1464764400000L,
            1467356400000L,
            1470034800000L,
            1472713200000L,
            1475305200000L,
            1477983600000L,
            1480579200000L,
            1483257600000L,
            1485936000000L,
            1488355200000L,
            1491030000000L,
            1493622000000L,
            1496300400000L,
            1498892400000L,
            1501570800000L,
            1504249200000L,
            1506841200000L,
            1509519600000L,
            1512115200000L,
            1514793600000L,
            1517472000000L,
            1518076800000L
    };

    private String mTestTicker = "IBM";
    private Stock mTestStock;
    private List<Entry> priceEntryList;
    private List<Entry> priceEntryListFromCSV;
    private String mStockDataCSV;



    
    @Test
    public void parseCSVtoEntryListTest() {
        String stringCSV = givenCSVDataString();
        List<Entry> result = covertCSVDataStringToEntryList(stringCSV);
        retainingOnlyValidValues(result);
    }

    private String givenCSVDataString() {
        return sTestData;
    }

    private List<Entry> covertCSVDataStringToEntryList(String testData) {
        return Util.historicalStockQuotesCSVtoEntryList(testData);
    }


    private void retainingOnlyValidValues(List<Entry> results) {
        int counter = 0;
        for (Entry result : results) {
            Assert.assertEquals(results.get(counter).getX(), (float) sTestLongVaues[counter]);
            Assert.assertEquals(results.get(counter).getY(), sTestFloatValues[counter]);
            counter++;
        }
    }



    @Test
    public void covertStockEntryListToCSVStringTest(){
        givenAvalidSTockEntryList();
        whenConvertedToCSVString();
        dataRemainsValid();
    }

    private void givenAvalidSTockEntryList() {
        priceEntryList = Util.historicalStockQuotesCSVtoEntryList(sTestData);
        Assert.assertNotNull(priceEntryList);
        Assert.assertTrue(priceEntryList.size() == sTestFloatValues.length );
    }

    private void whenConvertedToCSVString() {
        mStockDataCSV = Util.historicalStockQuoteEntryListToCSVString(priceEntryList);
        Assert.assertNotNull(mStockDataCSV);
        Assert.assertTrue(mStockDataCSV.length() > 0);
    }

    private void dataRemainsValid() {
        priceEntryListFromCSV = Util.historicalStockQuotesCSVtoEntryList(mStockDataCSV);
        Assert.assertNotNull(priceEntryListFromCSV);
        Assert.assertTrue("Wront size: exptected/actual: " +  priceEntryListFromCSV.size() + "/" + priceEntryList.size(),
                priceEntryListFromCSV.size() == priceEntryList.size());
        int counter = 0;
        for(Entry entryUnderTest : priceEntryListFromCSV){
            Assert.assertEquals(priceEntryList.get(counter).getX(), entryUnderTest.getX());
            Assert.assertEquals(priceEntryList.get(counter).getY(), entryUnderTest.getY());
            counter++;
        }
    }



    @Test
    public void historicalQuoteListToChartEntryListConversion() {
       Stock testStock = givenAValidStockObject();
        List<Entry> result = whenConvertingBetweenListTypes(testStock);
        aValidEntryListIsCreated(testStock, result);
    }

    private Stock givenAValidStockObject() {
        Stock testStock = null;
        try {
            testStock = YahooFinance.get(mTestTicker);
            Assert.assertNotNull(testStock);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testStock;
    }

    private List<Entry> whenConvertingBetweenListTypes(Stock stock) {
        List<Entry> result = new ArrayList<>();
        try {
            result = Util.historicalStockHistoricalQuoteListtoEntryList(stock.getHistory());
            Assert.assertNotNull(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void aValidEntryListIsCreated(Stock stock, List<Entry> result) {
        int counter = 0;
        try {
            List<HistoricalQuote> history = stock.getHistory();
            for (HistoricalQuote quote : history) {
                if (quote.getDate().getTimeInMillis() > 0 && quote.getClose() != null) {
                    Assert.assertEquals(quote.getClose().floatValue(), result.get(counter).getY());
                    counter++;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    
    @Test
    public void convertStockMaptoStockDtoTest(){
        String[] validTickers = {"TSLA", "IBM", "BA"};
        Map<String, Stock> stockMap = givenAValidStockMap(validTickers);
        List<StockDto> results = whenConvertingFromStockMapToStockDtoList(stockMap);
        returnsValidResults(stockMap, results);
    }

    private Map<String, Stock> givenAValidStockMap(String[] validTickers) {
        Map<String, Stock> results = null;
        try {
            results = YahooFinance.get(validTickers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull("Failed to fetch date for the Map<String, Stock>", results);
        return results;
    }

    private List<StockDto> whenConvertingFromStockMapToStockDtoList(Map<String, Stock> stockMap) {
        List<StockDto> results  = Util.convertStockMapToStockDtoList(stockMap);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() == stockMap.size());
        return results;
    }

    private void returnsValidResults(Map<String, Stock> stockMap, List<StockDto> stockDtoList) {
        for(StockDto stockDto : stockDtoList){
            Assert.assertTrue(stockMap.containsKey(stockDto.getTicker()));
            Stock stock = stockMap.get(stockDto.getTicker());
            Assert.assertEquals(stock.getQuote().getAsk().floatValue(), stockDto.getAsk());
            Assert.assertEquals(stock.getQuote().getBid().floatValue(), stockDto.getBid());
            Assert.assertEquals(stock.getQuote().getChange().floatValue(), stockDto.getChangeCurrency());
            Assert.assertEquals(stock.getQuote().getChangeInPercent().floatValue(), stockDto.getChangePercent());
            Assert.assertEquals(stock.getQuote().getYearHigh().floatValue(), stockDto.getYearHigh());
            Assert.assertEquals(stock.getQuote().getYearLow().floatValue(), stockDto.getYearLow());
            Assert.assertEquals(stock.getName(), stockDto.getName());
            aValidEntryListIsCreated(stock, stockDto.getHistory());
        }
    }


    @Test
    public void stockNormalizerTest(){
        givenAMalformedStockObjectWithMissingData();
        whenStockDtoIsInitializedWithAMalformedStockObject();
        allFieldsAreInitializedWithDefaultNonNullValues();
    }

    private void givenAMalformedStockObjectWithMissingData() {
        try {
            mMalformedStock = YahooFinance.get("BAO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void whenStockDtoIsInitializedWithAMalformedStockObject() {
        mStockDtoUnderTest = new StockDto(mMalformedStock);
    }

    private void allFieldsAreInitializedWithDefaultNonNullValues() {
        Assert.assertNotNull("mId is null",mStockDtoUnderTest.getId());
        Assert.assertNotNull("mTicker is null",mStockDtoUnderTest.getTicker());
        Assert.assertNotNull("mRegPrice is null",mStockDtoUnderTest.getRegPrice());
        Assert.assertNotNull("mBid is null",mStockDtoUnderTest.getBid());
        Assert.assertNotNull("mAsk is null",mStockDtoUnderTest.getAsk());
        Assert.assertNotNull("mHistory is null",mStockDtoUnderTest.getHistory());
        Assert.assertNotNull("mName is null",mStockDtoUnderTest.getName());
        Assert.assertNotNull("mChangeCurrency is null",mStockDtoUnderTest.getChangeCurrency());
        Assert.assertNotNull("mChangePercent is null",mStockDtoUnderTest.getChangePercent());
        Assert.assertNotNull("mYearHigh is null",mStockDtoUnderTest.getYearHigh());
        Assert.assertNotNull("mYearLow is null",mStockDtoUnderTest.getYearLow());
    }


    @Test
    public void stockInitializationWithNullArgument(){
        givenNullStockObject();
        whenStockDtoIsInitializedWithAMalformedStockObject();
        allFieldsAreInitializedWithDefaultNonNullValues();
    }

    private void givenNullStockObject() {
        mMalformedStock = null;
    }
}
