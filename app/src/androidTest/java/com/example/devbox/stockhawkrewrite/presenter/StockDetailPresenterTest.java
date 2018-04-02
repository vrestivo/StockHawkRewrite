package com.example.devbox.stockhawkrewrite.presenter;

import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.IModel;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.view.IStockDetailsView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class StockDetailPresenterTest {

    private static IModel sModel;
    private static StockDetailsPresenter sDetailPresenter;
    private static String sValidTicker = "LMT";


    @Mock
    private static IStockDetailsView sDetailView;
    private String mInvalidTicker = "ZZZAAADDDBBB";

    @BeforeClass
    public static void classSetup(){
        sDetailView = mock(IStockDetailsView.class);
        sDetailPresenter = new StockDetailsPresenter(sDetailView, getTargetContext());
        sModel = sDetailPresenter.getModel();
        sModel.getStockRoomDb().stockDao().deleteAllStocks();
        sModel.fetchASingleStockAndStoreInDatabase(sValidTicker);
        sleep(10000);
    }

    @Before
    public void testSetup(){

    }

    @After
    public void testCleanup(){

    }

    @AfterClass
    public static void classCleanup(){
        sModel.getStockRoomDb().stockDao().deleteAllStocks();
    }


    @Test
    public void getStockDataTest(){
        givenInitializedModelWithSampleData();
        whenAValidStockIsRequested();
        dataIsPassedToUI();
    }

    private void givenInitializedModelWithSampleData() {
        Assert.assertNotNull("model is null", sModel);
        StockDto stockDto = sModel.getStockRoomDb().stockDao().searchForASingleStock(sValidTicker);
        Assert.assertNotNull("database does not contain initialization data", stockDto);
        Assert.assertEquals("invalid stock initialization data", sValidTicker, stockDto.getTicker());
    }

    private void whenAValidStockIsRequested() {
        sDetailPresenter.getStockByTicker(sValidTicker);
        sleep(2000);
    }

    private void dataIsPassedToUI() {
        verify(sDetailView, atLeastOnce()).onDataLoaded(notNull());
    }


    @Test
    public void getInvalidStockDataTest(){
        givenInitializedModelWithSampleData();
        whenInvalidStockIsRequested();
        thereAreNoEmissions();
    }

    private void whenInvalidStockIsRequested() {
        sDetailPresenter.getStockByTicker(mInvalidTicker);
        sleep(2000);
    }

    private void thereAreNoEmissions() {
        verify(sDetailView, never()).onDataLoaded(any());
    }

}
