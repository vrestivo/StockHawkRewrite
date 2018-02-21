package com.example.devbox.stockhawkrewrite;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;
import com.github.mikephil.charting.data.Entry;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


/**
 * Created by devbox on
 */

@RunWith(AndroidJUnit4.class)
public class RoomDbStockDaoTest {

    private StockRoomDb stockRoomDb;
    private YFNetDao yahooFinanceNetDao;
    private List<StockDto> downloadedStockData;
    private List<StockDto> databaseStockData;
    private String[] validStocks = {"TSLA", "IBM", "BA"};

    @Before
    public void setupInMemoryDb() {
        stockRoomDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                StockRoomDb.class
        ).build();
    }

    @After
    public void tearDownInMemoryDb() {
        if (stockRoomDb != null && stockRoomDb.isOpen()) {
            stockRoomDb.close();
        }
    }

    @Test
    public void databaseStorageAndRetrievalTest() {
        givenDownloadedStockData();
        whenStockDataIsSavedAndRrieved();
        theDataIntegrityIsPreserved();

    }

    private void givenDownloadedStockData() {
        yahooFinanceNetDao = new YFNetDao();
        downloadedStockData = yahooFinanceNetDao.fetchStocks(validStocks);
        Assert.assertNotNull("_in: databaseStorageAndRetrievalTest Failed To download stock data.", downloadedStockData);
        Assert.assertTrue(downloadedStockData.size()>0);
    }

    private void whenStockDataIsSavedAndRrieved() {
        for(StockDto stockDto : downloadedStockData) {
            stockRoomDb.stockDao().insertStocks(stockDto);
        }

    }

    private void theDataIntegrityIsPreserved() {
        databaseStockData = stockRoomDb.stockDao().getAllStocks();
        Assert.assertEquals(downloadedStockData.size(), databaseStockData.size());
        int couter = 0;
        int historyCounter = 0;
        List<Entry> exptectedHistory;
        List<Entry> historyUnderTest;
        for(StockDto exptected: downloadedStockData){

            Assert.assertEquals("wrong ticker", exptected.getmTicker(), databaseStockData.get(couter).getmTicker());
            Assert.assertEquals("wrong ask price", exptected.getmAsk(), databaseStockData.get(couter).getmAsk());
            Assert.assertEquals("wrong big price", exptected.getmBid(), databaseStockData.get(couter).getmBid());
            Assert.assertEquals("wrong change currency", exptected.getmChangeCurrency(), databaseStockData.get(couter).getmChangeCurrency());
            Assert.assertEquals("wrong percent change", exptected.getmChangePercent(), databaseStockData.get(couter).getmChangePercent());
            Assert.assertEquals("wrong year high", exptected.getmYearHigh(), databaseStockData.get(couter).getmYearHigh());
            Assert.assertEquals("year low", exptected.getmYearLow(), databaseStockData.get(couter).getmYearLow());
            Assert.assertEquals("history size mismatch", exptected.getmHistory().size(), databaseStockData.get(couter).getmHistory().size());
            exptectedHistory = exptected.getmHistory();
            historyUnderTest = databaseStockData.get(couter).getmHistory();
            Assert.assertNotNull("expected history is null", exptectedHistory);
            Assert.assertNotNull("history under test is null", historyUnderTest);
            for(Entry entryUnderTest: historyUnderTest){
                Assert.assertEquals("", entryUnderTest.getX(), exptectedHistory.get(historyCounter).getX());
                Assert.assertEquals("", entryUnderTest.getY(), exptectedHistory.get(historyCounter).getY());
                historyCounter++;
            }

            couter++;
            historyCounter = 0;
        }
    }


}
