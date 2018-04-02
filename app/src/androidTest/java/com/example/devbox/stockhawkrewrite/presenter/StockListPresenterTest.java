package com.example.devbox.stockhawkrewrite.presenter;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.view.IStockListView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Stock list presenter test
 */

@RunWith(AndroidJUnit4.class)
public class StockListPresenterTest {

    private static StockListPresenter sPresenterUnderTest;
    private String mASingleTestTicker = "LMT";
    private String mInvalidStockTicker = "ZZZZaaaaZZZZ";
    private String[] mValidTestStockTickerList = {"LMT", "TSLA", "IBM"};


    @Mock
    private static IStockListView sView;

    @After
    public void testCleanup(){
        sPresenterUnderTest.getModel().getStockRoomDb().stockDao().deleteAllStocks();
        SystemClock.sleep(1000);
        clearInvocations(sView);
    }


    @BeforeClass
    public static void testTestClassSetup(){
        sView = mock(IStockListView.class);
        sPresenterUnderTest = new StockListPresenter(sView, InstrumentationRegistry.getTargetContext());
        sPresenterUnderTest.getModel().getStockRoomDb().stockDao().deleteAllStocks();
    }

    @AfterClass
    public static void afterTestClassCleanup(){
        sPresenterUnderTest.cleanup();
        sPresenterUnderTest.getModel().getStockRoomDb().stockDao().deleteAllStocks();
    }


    @Test
    public void addAStockTest(){
        givenInitializedStockListPresenter();
        whenAStockIsAdded();
        SystemClock.sleep(7000);
        theViewIsGivenAnUpdatedStockList();
    }

    private void givenInitializedStockListPresenter() {
        Assert.assertNotNull(sView);
        Assert.assertNotNull(sPresenterUnderTest);
        Assert.assertNotNull(sPresenterUnderTest.getModel());
    }

    private void whenAStockIsAdded() {
        sPresenterUnderTest.addAStock(mASingleTestTicker);
    }

    private void theViewIsGivenAnUpdatedStockList() {
        verify(sView, atLeastOnce()).onStockListLoaded(anyList());
        Assert.assertEquals("invalid ticker for single stock fetch"
                ,mASingleTestTicker,
                sPresenterUnderTest.getModel().getStockRoomDb().stockDao().searchForASingleStock(mASingleTestTicker).getTicker());
    }

    @Test
    public void refreshStockDataTest(){
        givenInitializedStockListPresenter();
        givenPrepopulatedStockDatabaseWidthTestObjects();
        whenStocksAreRefreshed();
        validDataIsStoredInDatabaseAndViewIsUpdated();
    }

    private void givenPrepopulatedStockDatabaseWidthTestObjects() {
        for(String ticker: mValidTestStockTickerList){
            StockDto stockToInsert = new StockDto();
            stockToInsert.setTicker(ticker);
            sPresenterUnderTest.getModel().getStockRoomDb().stockDao().insertStocks(stockToInsert);
        }
    }

    private void whenStocksAreRefreshed() {
        sPresenterUnderTest.refreshStockData();
        SystemClock.sleep(5000);
    }

    private void validDataIsStoredInDatabaseAndViewIsUpdated() {
        verify(sView, atLeastOnce()).onStockListLoaded(anyList());
        List<StockDto> retrievedStocks = sPresenterUnderTest.getModel().getStockRoomDb().stockDao().getAllStocks();
        List<String> validTickerList = Arrays.asList(mValidTestStockTickerList);
        for(StockDto stockDto : retrievedStocks){
            Assert.assertTrue("retrieved tickers don't match " + stockDto.getTicker(), validTickerList.contains(stockDto.getTicker()));
            Assert.assertNotNull(stockDto.getName());
        }
        SystemClock.sleep(2000);
        clearInvocations(sView);
    }

    @Test
    public void addInvalidStockTest(){
        givenInitializedStockListPresenter();
        clearInvocations(sView);
        whenInvalidStockIsAdded();
        SystemClock.sleep(5000);
        userIsNotifiedOfError();
    }

    private void whenInvalidStockIsAdded() {
        sPresenterUnderTest.addAStock(mInvalidStockTicker);
    }

    private void userIsNotifiedOfError() {
        verify(sView, atLeastOnce()).displayError(anyString());
        verify(sView, never()).onStockListLoaded(anyList());
    }


    @Test
    public void loadingEmptyStockList(){
        givenInitializedStockListPresenter();
        whenThereAreNoStocksInDatabaseAndDataISRefreshed();
        nothingIsEmittedAndUILayersIsNotified();
    }

    private void whenThereAreNoStocksInDatabaseAndDataISRefreshed() {
        sPresenterUnderTest.refreshStockData();
        SystemClock.sleep(1000);
    }

    private void nothingIsEmittedAndUILayersIsNotified() {
        verify(sView, never()).onStockListLoaded(anyList());
        verify(sView, atLeastOnce()).showListIsEmpty();
    }


}
