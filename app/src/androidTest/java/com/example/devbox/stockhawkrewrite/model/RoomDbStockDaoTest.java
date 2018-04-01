package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;
import com.github.mikephil.charting.data.Entry;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

/**
 * This class performs Room database test
 */

@RunWith(AndroidJUnit4.class)
public class RoomDbStockDaoTest {

    private static StockRoomDb stockRoomDb;
    private static YFNetDao yahooFinanceNetDao;
    private static List<StockDto> downloadedStockData;
    private static String newStockTicker = "LMT";
    private static StockDto newStockDto;
    private static String[] validStocks = {"TSLA", "IBM", "BA"};
    private List<StockDto> databaseStockData;
    private String stockToDelete = "TSLA";
    private String stockToSearch = "IBM";
    private String invalidStockTicker = "ZZZZZZ";
    private String mClearDatabaseQuery = "DELETE FROM stocks;";

    @BeforeClass
    public static void testClassSetup(){
        yahooFinanceNetDao = new YFNetDao();
        downloadedStockData = yahooFinanceNetDao.fetchStocks(validStocks);
        newStockDto = yahooFinanceNetDao.fetchASingleStock(newStockTicker);
        stockRoomDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                StockRoomDb.class)
                .allowMainThreadQueries()
                .build();
    }

    @Before
    public void setupInMemoryDb() {
        databaseStockData = null;
    }

    @After
    public void cleanup(){
        stockRoomDb.compileStatement(mClearDatabaseQuery).execute();
    }

    @AfterClass
    public static void tearDownInMemoryDb() {
        //clean up database
        if (stockRoomDb != null && stockRoomDb.isOpen()) {
            stockRoomDb.close();
        }
    }



    @Test
    public void databaseStorageAndRetrievalTest() {
        givenDownloadedStockData();
        whenDownloadedStockDataIsSaved();
        dataIntegrityIsPreserved();
    }

    private void givenDownloadedStockData() {
        Assert.assertNotNull("_in: databaseStorageAndRetrievalTest Failed To download stock data.", downloadedStockData);
        Assert.assertNotNull("newStockDto is null", newStockDto);
        Assert.assertTrue(downloadedStockData.size()>0);
        Assert.assertEquals("new stock ticker does not match", newStockTicker, newStockDto.getTicker());
    }

    private void whenDownloadedStockDataIsSaved() {
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


    @Test
    public void deleteAllTest(){
        givenDownloadedStockData();
        givenADataBasePopulatedWithStockData();
        whenDeletingAllItems();
        dataBaseIsEmpty();
    }

    private void givenADataBasePopulatedWithStockData(){
        Assert.assertNotNull("", downloadedStockData);
        Assert.assertTrue("",downloadedStockData.size() > 0);
        for(StockDto stock : downloadedStockData) {
            stockRoomDb.stockDao().insertStocks(stock);
        }
        Assert.assertTrue("",stockRoomDb.stockDao().getAllStocks().size() > 0);
    }

    private void whenDeletingAllItems() {
        stockRoomDb.stockDao().deleteAllStocks();
    }

    private void dataBaseIsEmpty(){
        Assert.assertTrue("",stockRoomDb.stockDao().getAllStocks().size() == 0);
    }

    //TODO add edge cases tests
    @Test
    public void deleteSingleStockTest(){
        givenDownloadedStockData();
        whenDownloadedStockDataIsSaved();
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
        whenDownloadedStockDataIsSaved();
        searchForASingleValidStockReturnsAValidResult(stockToSearch);
        searchForAnInvalidStockReturnsNull();
    }

    private void searchForASingleValidStockReturnsAValidResult(String tickerToSearch) {
        StockDto stockDto = stockRoomDb.stockDao().searchForASingleStock(tickerToSearch);
        Assert.assertEquals(stockToSearch, stockDto.getTicker());
    }

    private void searchForAnInvalidStockReturnsNull() {
        StockDto stockDto = stockRoomDb.stockDao().searchForASingleStock(invalidStockTicker);
        Assert.assertNull(stockDto);
    }



    @Test
    public void getAllStockTickersTest(){
        givenInitializedDatabaseWithDownloadedStockData();
        String[] queryResults = whenQueriedForAllStockTickers();
        returnsAStringArrayWithValidStockTickers(queryResults);
    }

    private void givenInitializedDatabaseWithDownloadedStockData() {
        givenDownloadedStockData();
        whenDownloadedStockDataIsSaved();
    }

    private String[] whenQueriedForAllStockTickers(){
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
