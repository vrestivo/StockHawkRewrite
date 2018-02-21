package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.model.Util;
import com.github.mikephil.charting.data.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Test for Util class' conversion methods
 */

public class UtilTest {

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

}
