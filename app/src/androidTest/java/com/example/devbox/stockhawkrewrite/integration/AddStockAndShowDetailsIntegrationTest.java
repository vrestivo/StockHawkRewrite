package com.example.devbox.stockhawkrewrite.integration;


import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.Util.MyIdlingResources;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.view.MainActivity;

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


@RunWith(AndroidJUnit4.class)
public class AddStockAndShowDetailsIntegrationTest {

    private static Context sContext;
    private static StockRoomDb sStockDatabase;
    private String mValidTicker = "LMT";
    private IdlingRegistry mIdlingRegistry;
    private MyIdlingResources mIdlingResources;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true);

    @BeforeClass
    public static void classSetup(){
        sContext = getTargetContext();
        sStockDatabase = StockRoomDb.getsDatabaseInstance(sContext);
        sStockDatabase.stockDao().deleteAllStocks();
    }


    @Before
    public void testSetup(){
        mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingResources = mainActivityActivityTestRule.getActivity().getMyIdlingResource();
        mIdlingRegistry.register(mIdlingResources);
    }

    @After
    public void testCleanup(){
        mIdlingRegistry.unregister(mIdlingResources);
    }

    @AfterClass
    public static void classCleanup(){
        sStockDatabase.stockDao().deleteAllStocks();
    }

    @Test
    public void AddAStockAndShowDetailsTest(){
        givenMainActivityUiAndUserAddingStock();
        whenUserAddsStockToTheList();
        userCanDisplayStockDetails();
    }

    private void givenMainActivityUiAndUserAddingStock() {
        onView(withId(R.id.add_stock_button)).check(matches(isClickable()));
        onView(withId(R.id.add_stock_button)).perform(click());
    }

    private void whenUserAddsStockToTheList() {
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


        onView(withId(R.id.stock_list))
                .inRoot(withDecorView(is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(hasDescendant(withText(mValidTicker))));
    }



    private void userCanDisplayStockDetails() {
        onView(withText(mValidTicker)).perform(click());

        onView(withId(R.id.detail_ticker))
                .check(ViewAssertions.matches(
                        ViewMatchers.withText(sContext.getString(R.string.detail_ticker, mValidTicker))));
    }


}
