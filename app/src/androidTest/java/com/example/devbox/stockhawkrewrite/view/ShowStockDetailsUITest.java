package com.example.devbox.stockhawkrewrite.view;


import android.content.Context;
import android.os.PatternMatcher;
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


    @Rule
    public ActivityTestRule<StockDetailActivity> rule = new ActivityTestRule<StockDetailActivity>(StockDetailActivity.class, false, true);

    //@Rule
    //public IntentsTestRule<StockDetailActivity> detailRule = new IntentsTestRule<>(StockDetailActivity.class);


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
        rule.getActivity().onDataLoaded(mTestSTock);
    }



    private void stockDetailsAreShown() {
        checkViewValue(R.id.detail_company_name, R.string.detail_name, mTestSTock.getName());
        checkViewValue(R.id.detail_ticker, R.string.detail_ticker, mTestSTock.getTicker());
        checkViewValue(R.id.detail_price, R.string.detail_price, mTestSTock.getRegPrice());
        checkViewValue(R.id.detail_ask, R.string.detail_ask, mTestSTock.getAsk());
        checkViewValue(R.id.detail_bid, R.string.detail_bid, mTestSTock.getBid());
        checkViewValue(R.id.detail_currency_change, R.string.detail_currency_change, mTestSTock.getChangeCurrency());
        checkViewValue(R.id.detail_percent_change, R.string.detail_percent_change, mTestSTock.getChangePercent());
        checkViewValue(R.id.detail_year_high, R.string.detail_year_high, mTestSTock.getYearHigh());
        checkViewValue(R.id.detail_year_low, R.string.detail_year_low, mTestSTock.getYearLow());
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
