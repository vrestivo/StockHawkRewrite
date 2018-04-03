package com.example.devbox.stockhawkrewrite.view;


import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;

@RunWith(AndroidJUnit4.class)
public class ShowStockDetailsUITest {

    private static Context sContext;
    private static StockRoomDb sDatabase;
    private static String sValidStock = "LMT";
    private StockDto mTestSTock;


    @Rule
    public ActivityTestRule<StockDetailActivity> rule = new ActivityTestRule<StockDetailActivity>(StockDetailActivity.class);


    @BeforeClass
    public static void classSetup(){
        sContext = getTargetContext();
        sDatabase = StockRoomDb.getsDatabaseInstance(sContext);
    }

    @Before
    public void testSetup(){

    }

    @After
    public void testCleaup(){

    }

    @AfterClass
    public static void classCleaup(){
        sDatabase.stockDao().deleteAllStocks();
    }

    @Test
    public void showStockDetailUITest(){
        givenInitializedDatabase();
        whenUserClicksOnDesiredStock();
        stockDetailsAreShown();
    }

    private void givenInitializedDatabase() {
        YFNetDao yfNetDao = new YFNetDao();
        mTestSTock = yfNetDao.fetchASingleStock(sValidStock);
        sDatabase.stockDao().insertStocks(mTestSTock);
        Assert.assertNotNull("database was not properly initiated", sDatabase.stockDao().searchForASingleStock(sValidStock));
    }

    private void whenUserClicksOnDesiredStock() {
        rule.getActivity().getStockData(sValidStock);
    }

    private void stockDetailsAreShown() {
        //TODO compare fields
    }

}
