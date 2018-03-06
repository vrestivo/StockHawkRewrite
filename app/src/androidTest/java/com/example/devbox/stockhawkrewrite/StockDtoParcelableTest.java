package com.example.devbox.stockhawkrewrite;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.github.mikephil.charting.data.Entry;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import yahoofinance.YahooFinance;

/**
 * Test for StockDto Parcelable implementation
 */

@RunWith(AndroidJUnit4.class)
public class StockDtoParcelableTest {

    private String mStockTicker = "IBM";
    private String mBundleKey = "stock";
    private StockDto mStockDto;
    private Bundle mBundle;

    @Test
    public void stockDtoParcellableTest(){
        givenInitializedStockDto();
        whenPackingStockDtoToBundle();
        stockDataRemainsValid();
    }

    private void givenInitializedStockDto() {
        try {
            mStockDto = new StockDto(YahooFinance.get(mStockTicker));
            Assert.assertNotNull("stockDto is null", mStockDto);
            Assert.assertEquals("Invalid stock initialization\nStock tickers don't match", mStockTicker, mStockDto.getTicker());
            Assert.assertNotNull("stock history is null", mStockDto.getHistory());
            Assert.assertTrue("stock history size is 0", mStockDto.getHistory().size() > 0);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IO Exception while getting stock data");
        }
    }

    private void whenPackingStockDtoToBundle() {
        mBundle = new Bundle();
        mBundle.putParcelable(mBundleKey, mStockDto);
    }



    private void stockDataRemainsValid(){
        StockDto stockDtoFromBundle = (StockDto) mBundle.get(mBundleKey);

        Assert.assertNotNull(stockDtoFromBundle);
        Assert.assertEquals("Invaid ticker", mStockDto.getTicker(), stockDtoFromBundle.getTicker());
        Assert.assertEquals("Invaid name", mStockDto.getName(), stockDtoFromBundle.getName());
        Assert.assertEquals("Invaid Ask", mStockDto.getAsk(), stockDtoFromBundle.getAsk());
        Assert.assertEquals("Invaid Bid", mStockDto.getBid(), stockDtoFromBundle.getBid());
        Assert.assertEquals("Invaid changeCurrency", mStockDto.getChangeCurrency(), stockDtoFromBundle.getChangeCurrency());
        Assert.assertEquals("Invaid changePercent", mStockDto.getChangePercent(), stockDtoFromBundle.getChangePercent());
        Assert.assertEquals("Invaid yearHigh", mStockDto.getYearHigh(), stockDtoFromBundle.getYearHigh());
        Assert.assertEquals("Invaid yearLow", mStockDto.getYearLow(), stockDtoFromBundle.getYearLow());
        List<Entry> exptectedHistory = mStockDto.getHistory();
        List<Entry> historyUnderTest = stockDtoFromBundle.getHistory();
        Assert.assertNotNull("Invalid stockHistory",stockDtoFromBundle.getHistory());

        int counter = 0;
        for(Entry entry : exptectedHistory) {
            Assert.assertEquals("Invaid X", entry.getX(), historyUnderTest.get(counter).getX());
            Assert.assertEquals("Invaid Y", entry.getY(), historyUnderTest.get(counter).getY());
            counter++;
        }
    }

}
