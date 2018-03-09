package com.example.devbox.stockhawkrewrite;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Observable;

import io.reactivex.Flowable;


/**
 * TODO: implement
 */


@RunWith(AndroidJUnit4.class)
public class RxJavaRoomDbTest {

    private String[] validStockTickers = {"TSLA", "BA", "LMT"};
    private StockRoomDb stockRoomDb;
    private List<StockDto> downloadedStockData;
    private Flowable<List<StockDto>> databaseStockData;


    @Before
    public void setupInMemoryDb() {
        stockRoomDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                StockRoomDb.class
        ).build();

        downloadedStockData = null;
        databaseStockData = null;
    }

    @After
    public void tearDownInMemoryDb() {
        //clean up database
        if (stockRoomDb != null && stockRoomDb.isOpen()) {
            stockRoomDb.close();
        }
    }


    @Test
    public void downloadAndStoreStocksReactivelyTest(){

    }

}
