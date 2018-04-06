package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.presenter.IStockDetailsPresenter;
import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

import io.reactivex.Flowable;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.mockito.Mockito.*;

/**
 * Model Test
 */

@RunWith(AndroidJUnit4.class)
public class ModelTest {

    private static final String DELETE_ALL_QUERY = "DELETE FROM stocks;";
    private static IModel mModelUderTest;
    private String[] mValidStocks = {"TSLA", "IBM", "BA"};
    private String mSingleTestTicker = "NOC";
    private String mInvalidStockTicker = "ZZZZZZZ";
    private String mTestErrorMessage = "test_error_message";
    private Flowable <StockDto> mSingleStockDtoFlowable;

    @Mock
    private static IStockListPresenter mStockListPresenter;

    @Mock
    private static IStockDetailsPresenter mStockDetailsPresenter;

    @BeforeClass
    public static void testClassSetup(){
        mStockListPresenter = mock(IStockListPresenter.class);
        mStockDetailsPresenter = mock(IStockDetailsPresenter.class);
        mModelUderTest = Model.getInstance(getTargetContext(), mStockListPresenter);
        Model.getInstance(getTargetContext(), mStockDetailsPresenter);
    }

    @After
    public void testCleanup()
    {
        mModelUderTest.getStockRoomDb().query(new SimpleSQLiteQuery(DELETE_ALL_QUERY));
        clearInvocations(mStockListPresenter);
        clearInvocations(mStockDetailsPresenter);
    }


    @AfterClass
    public static void testClassCleanup(){
        mModelUderTest.getStockRoomDb().query(new SimpleSQLiteQuery(DELETE_ALL_QUERY));
    }


    @Test
    public void addAndDeleteASingleStockTest(){
        givenInitializedModel();
        whenASingleStockIsFetchedAndAddedToDatabase();
        SystemClock.sleep(7000);
        stockCanBeFoundInTheDatabase();
        whenASingleStockIsDeleted();
        SystemClock.sleep(1000);
        stockNotFoundInDatabase();
    }

    private void givenInitializedModel(){
        Assert.assertNotNull(mModelUderTest);
        Assert.assertNotNull(mModelUderTest.getStockRoomDb());
    }

    private void whenASingleStockIsFetchedAndAddedToDatabase(){
        mModelUderTest.fetchASingleStockAndStoreInDatabase(mSingleTestTicker);
    }

    private void stockCanBeFoundInTheDatabase() {
        StockDto retrievedStock = mModelUderTest.getStockRoomDb().stockDao().searchForASingleStock(mSingleTestTicker);
        Assert.assertNotNull("retrieved stock is null", retrievedStock);
        Assert.assertEquals("retrieved ticked does not match", mSingleTestTicker, retrievedStock.getTicker());
    }

    private void whenASingleStockIsDeleted() {
        mModelUderTest.deleteASingleStock(mSingleTestTicker);
        SystemClock.sleep(5000);
    }

    private void stockNotFoundInDatabase() {
        StockDto retrievedStock = mModelUderTest.getStockRoomDb().stockDao().searchForASingleStock(mSingleTestTicker);
        Assert.assertNull("retrieved stock is not null", retrievedStock);
    }


    @Test
    public void clearDataBaseTest(){
        givenInitializedModel();
        givenModelDatabasePopulatedWithTestData();
        whenClearDataIsCalled();
        SystemClock.sleep(3000);
        allDataIsErasedFromTheDatabase();
    }

    private void givenModelDatabasePopulatedWithTestData() {
        for (String ticker : mValidStocks) {
            StockDto testStockToInsert = new StockDto();
            testStockToInsert.setTicker(ticker);
            mModelUderTest.getStockRoomDb().stockDao().insertStocks(testStockToInsert);
        }
    }

    private void whenClearDataIsCalled() {
        mModelUderTest.clearStockDatabase();
    }

    private void allDataIsErasedFromTheDatabase() {
        List<StockDto> stockDtoList = mModelUderTest.getStockRoomDb().stockDao().getAllStocks();
        Assert.assertTrue("there are still stocks remaining in the database", stockDtoList.size() == 0);
    }


    @Test
    public void addInvalidStockTest(){
        givenInitializedModel();
        whenAnInvalidStockIsFetchedExceptionIsThrown();
        SystemClock.sleep(10000);
        presenterNotifiedOfError();
    }

    private void whenAnInvalidStockIsFetchedExceptionIsThrown(){
        mModelUderTest.fetchASingleStockAndStoreInDatabase(mInvalidStockTicker);
    }

    private void presenterNotifiedOfError(){
        verify(mStockListPresenter, atLeastOnce()).sendMessageToUI(anyString());
    }


    @Test
    public void refreshStockDataTest(){
        givenInitializedModel();
        givenModelDatabasePopulatedWithTestData();
        whenStockDataIsRefreshed();
        SystemClock.sleep(7000);
        dataBaseHasValidResults();
    }

    private void whenStockDataIsRefreshed() {
        mModelUderTest.refreshStockData();
    }

    private void dataBaseHasValidResults() {
        for (String ticker :mValidStocks) {
            StockDto stockToVerify = mModelUderTest.getStockRoomDb().stockDao().searchForASingleStock(ticker);
            Assert.assertEquals("stock not found: " + ticker,
                    ticker,
                    stockToVerify.getTicker());
            Assert.assertNotNull("stock not found: " + ticker,
                    stockToVerify.getName());
        }
    }



    @Test
    public void stockListPresenterNotifyUserTest(){
        givenInitializedModel();
        whenNotifyErrorIsCalledOnPresenter();
        verifiedNotifyErrorWasCalled();
    }

    private void whenNotifyErrorIsCalledOnPresenter() {
        mModelUderTest.sendMessageToUI(mTestErrorMessage);
    }

    private void verifiedNotifyErrorWasCalled() {
        verify(mStockListPresenter, atLeastOnce()).sendMessageToUI(anyString());
    }


    @Test
    public void fetchASingleStockTest(){
        givenInitializedModel();
        whenASingleStockIsFetchedAndAddedToDatabase();
        whenSearchingForASingleStockByTicker();
        aFlowableIsReturnedAndStockDetailPresenterNotified();
    }

    private void whenSearchingForASingleStockByTicker() {
        mSingleStockDtoFlowable = mModelUderTest
                .getASingleStockFlowable(mSingleTestTicker);
    }


    private void aFlowableIsReturnedAndStockDetailPresenterNotified() {
        Assert.assertNotNull(mSingleStockDtoFlowable);
    }


}
