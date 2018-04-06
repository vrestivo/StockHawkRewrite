package com.example.devbox.stockhawkrewrite.view;


import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.Util.MyIdlingResources;
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

import java.util.Arrays;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RefreshStockDataUiTest {

    private static Context sContext;
    private static StockRoomDb sStockRoomDb;
    private List<StockDto> mDownloadedStockList;
    private YFNetDao mYfNetDao;
    private String[] mTickersToGet = {"LMT", "IBM", "F"};
    private List<String> mTickerToGetList = Arrays.asList(mTickersToGet);
    private IdlingRegistry mIdlingRegistry;
    private MyIdlingResources mIdlingResources;


    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @BeforeClass
    public static void classSetup(){
        sContext = getTargetContext();
        sStockRoomDb = StockRoomDb.getsDatabaseInstance(sContext);
        sStockRoomDb.stockDao().deleteAllStocks();
    }


    @Before
    public void testSetup(){
        mYfNetDao = new YFNetDao();
        mDownloadedStockList = mYfNetDao.fetchStocks(mTickersToGet);
        for (StockDto stockDto: mDownloadedStockList){
            Assert.assertTrue(
                    "initialization error while downloading test stock data",
                    mTickerToGetList.contains(stockDto.getTicker()));
            sStockRoomDb.stockDao().insertStocks(stockDto);
        }
        mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingResources = rule.getActivity().getMyIdlingResource();
        mIdlingRegistry.register(mIdlingResources);
    }


    @After
    public void testCleanup(){
        mIdlingRegistry.unregister(mIdlingResources);
    }


    @AfterClass
    public static void classCleanup(){
        sStockRoomDb.stockDao().deleteAllStocks();
    }


    @Test
    public void refreshStockDataTest(){
        givenInitializedDataSet();
        whenUserRefreshesStocks();
        noStockDataIsLost();
    }

    private void givenInitializedDataSet() {
        String[] tickersFromDatabase = sStockRoomDb.stockDao().getAllStockTickers();
        for(String ticker : tickersFromDatabase){
            Assert.assertTrue("database was not initialized correctly", mTickerToGetList.contains(ticker));
        }
    }

    private void whenUserRefreshesStocks() {
        onView(withContentDescription((sContext.getString(R.string.refresh_stock_data)))).perform(click());
    }

    private void noStockDataIsLost() {
        for(String ticker : mTickersToGet) {
            onView(withId(R.id.stock_list)).check(matches(hasDescendant(withText(ticker))));
        }
    }


}
