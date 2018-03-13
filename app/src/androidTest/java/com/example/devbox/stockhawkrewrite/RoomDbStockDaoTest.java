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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import yahoofinance.quotes.stock.StockStats;

/**
 * This class performs Room database test
 */

@RunWith(AndroidJUnit4.class)
public class RoomDbStockDaoTest {

    private StockRoomDb stockRoomDb;
    private YFNetDao yahooFinanceNetDao;
    private List<StockDto> downloadedStockData;
    private List<StockDto> databaseStockData;
    private String[] validStocks = {"TSLA", "IBM", "BA"};
    private String stockToDelete = "TSLA";
    private String stockToSearch = "IBM";
    private String newStockTicker = "LMT";
    private String invalidStockTicker = "ZZZZZZ";
    private boolean mStocksDownloaded = false;

    @Before
    public void setupInMemoryDb() {
        stockRoomDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                StockRoomDb.class
        ).build();

        downloadedStockData = null;
        databaseStockData = null;
    }

    @After
    public void tearDownInMemoryDb() {
        //clean up database
        if (stockRoomDb != null && stockRoomDb.isOpen()) {
            stockRoomDb.close();
        }
    }

    @Test
    public void databaseStorageAndRetrievalTest() {
        givenDownloadedStockData();
        whenStockDataIsSaved();
        dataIntegrityIsPreserved();

    }

    private void givenDownloadedStockData() {
        if(!mStocksDownloaded) {
            yahooFinanceNetDao = new YFNetDao();
            try {
                downloadedStockData = yahooFinanceNetDao.fetchStocks(validStocks);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Assert.assertNotNull("_in: databaseStorageAndRetrievalTest Failed To download stock data.", downloadedStockData);
        Assert.assertTrue(downloadedStockData.size()>0);
        mStocksDownloaded = true;
    }

    private void whenStockDataIsSaved() {
        for(StockDto stockDto : downloadedStockData) {
            stockRoomDb.stockDao().insertStocks(stockDto);
        }

    }

    private void dataIntegrityIsPreserved() {
        databaseStockData = stockRoomDb.stockDao().getAllStocks();
        Assert.assertEquals(downloadedStockData.size(), databaseStockData.size());
        int couter = 0;
        int historyCounter = 0;
        List<Entry> exptectedHistory;
        List<Entry> historyUnderTest;
        for(StockDto exptected: downloadedStockData){

            Assert.assertEquals("wrong ticker", exptected.getTicker(), databaseStockData.get(couter).getTicker());
            Assert.assertEquals("wrong ask price", exptected.getAsk(), databaseStockData.get(couter).getAsk());
            Assert.assertEquals("wrong big price", exptected.getBid(), databaseStockData.get(couter).getBid());
            Assert.assertEquals("wrong change currency", exptected.getChangeCurrency(), databaseStockData.get(couter).getChangeCurrency());
            Assert.assertEquals("wrong percent change", exptected.getChangePercent(), databaseStockData.get(couter).getChangePercent());
            Assert.assertEquals("wrong year high", exptected.getYearHigh(), databaseStockData.get(couter).getYearHigh());
            Assert.assertEquals("year low", exptected.getYearLow(), databaseStockData.get(couter).getYearLow());
            Assert.assertEquals("history size mismatch", exptected.getHistory().size(), databaseStockData.get(couter).getHistory().size());
            exptectedHistory = exptected.getHistory();
            historyUnderTest = databaseStockData.get(couter).getHistory();
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


    //TODO add edge cases tests
    @Test
    public void deleteSingleStockTest(){
        givenDownloadedStockData();
        whenStockDataIsSaved();
        selectStockCanBeDeleted();
    }

    private void selectStockCanBeDeleted() {
        int deletedStocks = stockRoomDb.stockDao().deleteASingleStock(stockToDelete);
        System.out.println("_in deleteSingleStockTest() deleted stock items: " +  deletedStocks);
        Assert.assertEquals(1,deletedStocks);
        databaseStockData = stockRoomDb.stockDao().getAllStocks();
        Assert.assertTrue(databaseStockData.size() == 2);
        System.out.println("_in deleteSingleStockTest() stock items: " +  databaseStockData.size());
        for(StockDto stockDto : databaseStockData){
            System.out.println("_in deleteSingleStockTest() remaining tickers: " + stockDto.getTicker());
        }
    }


    @Test
    public void searchForASingleStockTest() {
        givenDownloadedStockData();
        whenStockDataIsSaved();
        searchForASingleValidStockReturnsAValidResult();
        searchForAnInvalidStockReturnsNull();
    }

    private void searchForASingleValidStockReturnsAValidResult() {
        StockDto stockDto = stockRoomDb.stockDao().searchForASingleStock(stockToSearch);
        Assert.assertEquals(stockToSearch, stockDto.getTicker());
    }

    private void searchForAnInvalidStockReturnsNull() {
        StockDto stockDto = stockRoomDb.stockDao().searchForASingleStock(invalidStockTicker);
        Assert.assertNull(stockDto);
    }


    @Test
    public void getAllStockTickersTest(){

        giveInitializedDatabasAndInsrtedTestStocksDtos();
        String[] returnedTickers = whenQueriedForallStockTickers();
        Assert.assertNotNull("returned ticker string array is null",returnedTickers);
        returnsAStringArrayWithValidStockTickers(returnedTickers);
    }

    private void giveInitializedDatabasAndInsrtedTestStocksDtos(){
        for(String ticker : validStocks){
            StockDto stockDto = new StockDto();
            stockDto.setTicker(ticker);
            stockRoomDb.stockDao().insertStocks(stockDto);
        }
    }

    private String[] whenQueriedForallStockTickers(){
        return stockRoomDb.stockDao().getAllStockTickers();
    }

    private void returnsAStringArrayWithValidStockTickers(String[] tickersToTest) {
        Assert.assertEquals("invalid returned array size", validStocks.length, tickersToTest.length);

        List<String> validList = Arrays.asList(validStocks);
        for(String ticker: validStocks){
            Assert.assertTrue("list does not contain " + ticker, validList.contains(ticker));
        }
    }
}
