package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.model.Util;
import com.github.mikephil.charting.data.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Test for Util class' conversion methods
 */

public class UtilTest {

    private String testTicker = "TSLA";
    private Stock testStock;
    private List<Entry> priceEntryList;

    @Test
    public void historicalQuoteListToChartEntryListConversion(){
        givenAValidStockObject();
        whenConvertingBetweenListTypes();
        aValidEntryListIsCreated();

    }

    private void givenAValidStockObject() {
        try {
            testStock = YahooFinance.get(testTicker);
            Assert.assertNotNull(testStock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void whenConvertingBetweenListTypes() {
        try {
            priceEntryList = Util.historicalStockHistoricalQuoteListtoEntryList(testStock.getHistory());
            Assert.assertNotNull(priceEntryList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void aValidEntryListIsCreated(){
        int counter = 0;
        try {
            for(HistoricalQuote quote : testStock.getHistory()){
                Assert.assertEquals(quote.getClose(), priceEntryList.get(counter));
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
