package com.example.devbox.stockhawkrewrite;

import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.IModel;
import com.example.devbox.stockhawkrewrite.model.Model;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Model Test
 */

@RunWith(AndroidJUnit4.class)
public class ModelTest {

    private IModel mModelUderTest;
    private IStockListPresenter mStockListPresenter;
    private StockDto mTestStockToAdd;
    private String mTestTicker = "NOC";
    private String mInvalidStockTicker = "ZZZZZZZ";

    @BeforeClass
    public static void testClassSetup(){
        //TODO implement
    }

    @Before
    public void modelSetup(){
        mStockListPresenter = Mockito.mock(IStockListPresenter.class);

    }

    @After
    public void modelCleaup(){
        mTestStockToAdd = null;
    }

    @AfterClass
    public static void testClassCleanup(){
        //TODO implement
    }

    @Test
    public void addAndDeleteASingleStockTest(){
        givenInitializedModel();
        whenASingleStockIsFetchedAndAddedToDatabase();
        stockCanBeFoundInTheDatabase();
    }

    private void givenInitializedModel(){
        mTestStockToAdd = new StockDto();
        mModelUderTest = Model.getInstance(getContext());
    }

    private void whenASingleStockIsFetchedAndAddedToDatabase(){
        mTestStockToAdd.setTicker(mTestTicker);
        mModelUderTest.fetchASingleStockAndStoreInDatabase(mTestTicker);
    }

    private void stockCanBeFoundInTheDatabase() {
        StockRoomDb db = mModelUderTest.getsStockRoomDb();
        Assert.assertNotNull("database is null", db);
        Assert.assertTrue("database is closed", db.isOpen());
        StockDto retrievedStock = db.stockDao().searchForASingleStock(mTestTicker);
        Assert.assertEquals("retrieved ticked does not match", mTestTicker, retrievedStock.getTicker());
    }

    @Test
    public void addInvalidStockTest(){
        givenInitializedModel();
        whenAnInvalideStockIsfetchedExceptionIsThrown();
    }

    private void whenAnInvalideStockIsfetchedExceptionIsThrown(){
        mModelUderTest.fetchASingleStockAndStoreInDatabase(mInvalidStockTicker);
    }



}
