package com.example.devbox.stockhawkrewrite.view;


import android.content.Context;
import android.os.SystemClock;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.Util.MyIdlingResources;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class AddAStockDialogTest {


    private static Context sContext;
    private String mValidTicker = "LMT";
    private MainActivity mActivity;
    private IdlingRegistry mIdlingRegistry;
    private MyIdlingResources mMyIdlingResources;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @BeforeClass
    public static void classSetup(){
        sContext = getTargetContext();
    }

    @Before
    public void testSetup(){
        mActivity = mainActivityActivityTestRule.getActivity();
        mIdlingRegistry = IdlingRegistry.getInstance();
        mMyIdlingResources = mActivity.getMyIdlingResource();
        mIdlingRegistry.register(mMyIdlingResources);
    }

    @After
    public void testCleaup() {
        mIdlingRegistry.unregister(mMyIdlingResources);
        StockRoomDb stockRoomDb = StockRoomDb.getsDatabaseInstance(sContext);
        stockRoomDb.stockDao().deleteAllStocks();
    }

    @AfterClass
    public static void classCleanup(){
        StockRoomDb stockRoomDb = StockRoomDb.getsDatabaseInstance(sContext);
        stockRoomDb.stockDao().deleteAllStocks();
    }


    @Test
    public void addAStockActivityTest(){
        givenClickedAddStockButton();
        whenStockIsEnteredInTheDialogAndOkIsClicked();
        stockIsAddedToTheStockList();
    }

    private void givenClickedAddStockButton() {
        onView(withId(R.id.add_stock_button)).check(matches(isClickable()));
        onView(withId(R.id.add_stock_button)).perform(click());
    }

    private void whenStockIsEnteredInTheDialogAndOkIsClicked() {
        onView(withId(R.id.dialog_input_stock_ticker))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withId(R.id.dialog_input_stock_ticker))
                .perform(typeText(mValidTicker));


        onView(withId(R.id.dialog_text_input_layout))
                .inRoot(isDialog())
                .perform(closeSoftKeyboard());

        onView(withId(R.id.dialog_ok_button))
                .inRoot(isDialog())
                .perform(click());
    }

    private void stockIsAddedToTheStockList() {
        onView(withId(R.id.stock_list))
                .inRoot(withDecorView(is(mActivity.getWindow().getDecorView())))
                .check(matches(hasDescendant(withText(mValidTicker))));
    }


    @Test
    public void cancelStockAdditionTest(){
        givenClickedAddStockButton();
        whenUserEntersStockTickerAndPressesCancel();
        stockIsNotAdded();
    }

    private void whenUserEntersStockTickerAndPressesCancel() {
        onView(withId(R.id.dialog_input_stock_ticker))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withId(R.id.dialog_input_stock_ticker))
                .perform(typeText(mValidTicker));


        onView(withId(R.id.dialog_text_input_layout))
                .inRoot(isDialog())
                .perform(closeSoftKeyboard());

        onView(withId(R.id.dialog_cancel_button))
                .inRoot(isDialog())
                .perform(click());
    }

    private void stockIsNotAdded() {
        onView(withId(R.id.stock_list))
                .inRoot(withDecorView(is(mActivity.getWindow().getDecorView())))
                .check(matches(not(hasDescendant(withText(mValidTicker)))));
    }

}
