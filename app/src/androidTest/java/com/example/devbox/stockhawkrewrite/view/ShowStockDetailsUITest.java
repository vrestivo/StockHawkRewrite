package com.example.devbox.stockhawkrewrite.view;


import android.content.Context;
import android.os.PatternMatcher;
import android.os.SystemClock;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Patterns;

import com.example.devbox.stockhawkrewrite.R;
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

import java.util.regex.Pattern;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.endsWith;

@RunWith(AndroidJUnit4.class)
public class ShowStockDetailsUITest {

    private static Context sContext;
    private static StockRoomDb sDatabase;
    private static String sValidStock = "LMT";
    private StockDto mTestSTock;
    private String mInvalidTicker = "AADDFFBB";


    @Rule
    public ActivityTestRule<StockDetailActivity> rule = new ActivityTestRule<StockDetailActivity>(StockDetailActivity.class, false, true);


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
    public void showEmptyUITest(){
        givenInitializedStockDetailActivity();
        whenQueriedForInvalidStock();
        stockDataIsSetToDefaultValues();
    }


    private void givenInitializedStockDetailActivity() {
        Assert.assertNotNull("activity under test is null", rule.getActivity());
    }

    private void whenQueriedForInvalidStock() {
        rule.getActivity().getStockData(mInvalidTicker);
        SystemClock.sleep(2000);
    }

    private void stockDataIsSetToDefaultValues() {
        stockDetailsAreShown(new StockDto());
    }


    private void stockDetailsAreShown(StockDto testStock) {
        checkViewValue(R.id.detail_company_name, R.string.detail_name, testStock.getName());
        checkViewValue(R.id.detail_ticker, R.string.detail_ticker, testStock.getTicker());
        checkViewValue(R.id.detail_price, R.string.detail_price, testStock.getRegPrice());
        checkViewValue(R.id.detail_ask, R.string.detail_ask, testStock.getAsk());
        checkViewValue(R.id.detail_bid, R.string.detail_bid, testStock.getBid());
        checkViewValue(R.id.detail_currency_change, R.string.detail_currency_change, testStock.getChangeCurrency());
        checkViewValue(R.id.detail_percent_change, R.string.detail_percent_change, testStock.getChangePercent());
        checkViewValue(R.id.detail_year_high, R.string.detail_year_high, testStock.getYearHigh());
        checkViewValue(R.id.detail_year_low, R.string.detail_year_low, testStock.getYearLow());
    }


    @Test
    public void showStockDetailUITest(){
        givenInitializedDatabase();
        whenUserClicksOnDesiredStock();
        stockDetailsAreShown(mTestSTock);
    }

    private void givenInitializedDatabase() {
        YFNetDao yfNetDao = new YFNetDao();
        mTestSTock = yfNetDao.fetchASingleStock(sValidStock);
        sDatabase.stockDao().insertStocks(mTestSTock);
        Assert.assertNotNull("database was not properly initiated", sDatabase.stockDao().searchForASingleStock(sValidStock));
    }

    private void whenUserClicksOnDesiredStock() {
        rule.getActivity().getStockData(sValidStock);
        SystemClock.sleep(3000);
    }





    /**
     *
     * @param viewId view ID
     * @param stringId formatted string ID
     * @param args format arguments
     */
    private void checkViewValue(int viewId, int stringId, Object... args){
        onView(withId(viewId))
                .check(ViewAssertions.matches(
                        ViewMatchers.withText(sContext.getString(stringId, args)))
                );
    }

}
