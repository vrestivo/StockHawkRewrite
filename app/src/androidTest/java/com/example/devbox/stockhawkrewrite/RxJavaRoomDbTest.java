package com.example.devbox.stockhawkrewrite;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;


/**
 * TODO validate flowable udate
 */


@RunWith(AndroidJUnit4.class)
public class RxJavaRoomDbTest {

    private String[] validStockTickers = {"TSLA", "BA", "LMT"};
    private String[] newValidStockTickers = {"TSLA", "BA", "LMT", "NOC"};
    private StockRoomDb stockRoomDb;
    private YFNetDao yfNetDao = new YFNetDao();
    private List<StockDto> downloadedStockData;
    private Flowable<List<StockDto>> databaseStockFlowableData;
    private boolean checkedResult;


    @Before
    public void setupInMemoryDb() {
        stockRoomDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                StockRoomDb.class
        ).build();

        checkedResult = false;
        downloadedStockData = null;
        databaseStockFlowableData = null;
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
        givenDownloadedStockDataStoredInRoomDatabase();
        whileDatabaseIsUpatedAsyncrhonously();
        changesAreReflectedOntheMainThread();
    }

    private void givenDownloadedStockDataStoredInRoomDatabase() {
        downloadStockDataAsyncronously(validStockTickers);
        Assert.assertNotNull("donwloaded sock data is null",downloadedStockData);
        Assert.assertEquals("wrong stock data size", validStockTickers.length, downloadedStockData.size());
        saveStockDataAsynchronously(downloadedStockData);

        for (String tickerToCheck: validStockTickers) {
            Assert.assertTrue("database does not contain" + tickerToCheck, databaseContainsGivenStock(tickerToCheck));
        }
    }

    private void whileDatabaseIsUpatedAsyncrhonously() {
        downloadStockDataAsyncronously(newValidStockTickers);
        Assert.assertNotNull("donwloaded sock data is null", downloadedStockData);
        Assert.assertEquals("wrong stock data size", newValidStockTickers.length, downloadedStockData.size());
        saveStockDataAsynchronously(downloadedStockData);
    }

    private void changesAreReflectedOntheMainThread() {
        for (String tickerToCheck: newValidStockTickers) {
            Assert.assertTrue("database does not contain" + tickerToCheck, databaseContainsGivenStock(tickerToCheck));
        }
    }

    private void downloadStockDataAsyncronously(String[] stockListToDownload){
        Observable.fromCallable(() -> yfNetDao.fetchStocks(stockListToDownload))
                .timeout(10000, TimeUnit.MILLISECONDS)
                .blockingSubscribe(
                        stockDtoList -> { downloadedStockData = stockDtoList;},
                        throwable -> { throwable.printStackTrace();
                            Assert.fail("failed to download stock data");
                            }
                );
    }

    private void saveStockDataAsynchronously(List<StockDto> stockDtoListToSave){
        Observable.just(stockDtoListToSave)
                .timeout(3000, TimeUnit.MILLISECONDS)
                .blockingSubscribe(
                        stockDtoList -> {
                            for (StockDto stockDto : stockDtoList) {
                                stockRoomDb.stockDao().insertStocks(stockDto);
                            }
                        },
                        throwable -> {throwable.printStackTrace(); Assert.fail("failed to save stocks");}
                );
    }

    private boolean databaseContainsGivenStock(String tickerToCheck){
        Observable.just(tickerToCheck)
                .timeout(3000, TimeUnit.MILLISECONDS)
                .blockingSubscribe(
                        ticker -> {
                           StockDto returnedStock = stockRoomDb.stockDao().searchForASingleStock(ticker);
                           if(returnedStock!=null && tickerToCheck.equals(returnedStock.getTicker())){
                               checkedResult = true;
                           }
                           else {
                               checkedResult = false;
                           }
                        },
                        throwable -> {throwable.printStackTrace(); Assert.fail("failed to save stocks");}
                );
        return checkedResult;
    }


}
