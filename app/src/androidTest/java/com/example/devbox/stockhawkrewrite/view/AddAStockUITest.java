package com.example.devbox.stockhawkrewrite.view;


import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.Util.MyIdlingResources;
import com.example.devbox.stockhawkrewrite.model.Model;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddAStockUITest {


    private static Context sTargetContext;
    private static Model sModel;
    private static IStockListPresenter sPresenter;
    private static String mStockToAdd = "GOOG";
    private static IdlingRegistry sIdlingRegistry;
    private IdlingResource mIdlingResource;
    private StockRoomDb stockRoomDb;
    

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @BeforeClass
    public static void classSetup(){
        sTargetContext = getTargetContext();
        sIdlingRegistry = IdlingRegistry.getInstance();
    }

    @AfterClass
    public static void classCleanup(){
        sIdlingRegistry = null;
    }

    @Before
    public void testSetup(){
        sIdlingRegistry.register(rule.getActivity().getMyIdlingResource());
        stockRoomDb = StockRoomDb.getsDatabaseInstance(sTargetContext);
        stockRoomDb.stockDao().deleteAllStocks();
    }

    @After
    public void testCleanup(){
        if(mIdlingResource!=null){
            sIdlingRegistry.unregister(mIdlingResource);
        }
        if(stockRoomDb != null){
            stockRoomDb.stockDao().deleteAllStocks();
        }

    }


    @Test
    public void addAStockTest(){
        onView(withId(R.id.ticker_input_edit_text)).perform(typeText(mStockToAdd));
        onView(withId(R.id.ticker_input_edit_text)).perform(closeSoftKeyboard());
        onView(withId(R.id.add_stock_button)).perform(click());
        onView(withId(R.id.stock_list)).check(matches(hasDescendant(withText(mStockToAdd))));
    }


}
