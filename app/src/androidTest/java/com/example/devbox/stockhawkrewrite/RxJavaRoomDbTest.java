package com.example.devbox.stockhawkrewrite;
import android.arch.persistence.room.Room;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.StockRoomDb;
import com.example.devbox.stockhawkrewrite.model.Util;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * TODO validate flowable update
 */


@RunWith(AndroidJUnit4.class)
public class RxJavaRoomDbTest {

    private String[] validStockTickers = {"TSLA", "BA", "LMT"};
    private String[] newValidStockTickers = {"TSLA", "BA", "LMT", "NOC"};
    private String testTicker1 = "TEST1";
    private String testTicker2 = "TEST2";
    private StockRoomDb stockRoomDb;
    private YFNetDao yfNetDao = new YFNetDao();
    private List<StockDto> downloadedStockData;
    private List<StockDto> databaseStockData;
    private Flowable<List<StockDto>> databaseStockFlowableData;
    private boolean checkedResult;
    private Disposable disposable;


    @Before
    public void setupInMemoryDb() {
        stockRoomDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                StockRoomDb.class
        ).build();

        checkedResult = false;
        downloadedStockData = new ArrayList<>();
        databaseStockData = new ArrayList<>();
        databaseStockFlowableData = null;
    }


    @After
    public void tearDownInMemoryDb() {
        //clean up database
        if (stockRoomDb != null && stockRoomDb.isOpen()) {
            stockRoomDb.close();
        }
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
        databaseStockData = null;
        downloadedStockData = null;

    }


    @Test
    public void downloadAndStoreStocksReactivelyTest(){
        givenDownloadedStockDataStoredInRoomDatabase();
        whileDatabaseIsUpatedAsyncrhonously();
        changesAreReflectedOntheMainThread();
    }

    private void givenDownloadedStockDataStoredInRoomDatabase() {
        downloadStockDataAsyncronously(validStockTickers);
        Assert.assertNotNull("downloaded sock data is null",downloadedStockData);
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



    @Test
    public void databaseFlowableUpdateTest(){
        givenInitializedDatabaseFlowableAndDisposable();
        whenAStockIsAddedToTheDatabase();
        //give enough time for emissions to fire
        SystemClock.sleep(1000);
        flowableEmitsCorrectUpdates();
    }

    public void givenInitializedDatabaseFlowableAndDisposable(){
        initializeInMemoryTestDatabase();
        StockDto initialStock = new StockDto();
        initialStock.setTicker(testTicker1);
        insertASingleStockReactively(initialStock);

    }

    public void whenAStockIsAddedToTheDatabase(){
        StockDto stockToInsert = new StockDto();
        stockToInsert.setTicker(testTicker2);
        insertASingleStockReactively(stockToInsert);
    }

    private void flowableEmitsCorrectUpdates() {
        Assert.assertTrue("_stock list has and invalid size of " + databaseStockData.size(), databaseStockData.size() == 2);
        StockDto retrievedStock = searchStockDtoListForStock(testTicker2, databaseStockData);
        Assert.assertNotNull(retrievedStock);
        Assert.assertEquals("_failed to find added stock", testTicker2, retrievedStock.getTicker());
    }

    public void initializeInMemoryTestDatabase(){
        databaseStockFlowableData = stockRoomDb.stockDao().getAllStocksFlowable();
        disposable = databaseStockFlowableData
                .onBackpressureLatest()
                .subscribe(
                emitter -> {
                    Util.copyStockDtoList(emitter, databaseStockData);
                    System.out.println("_debug: updated stock list size " + emitter.size() + Thread.currentThread().getName());
                    for (StockDto stock : emitter) {
                        System.out.println("_debug: ticker " + stock.getTicker());
                    }
                },
                throwable -> {throwable.printStackTrace(); Assert.fail("_failed to insert initial stock data");}
        );
    }

    private StockDto searchStockDtoListForStock(String tickerToSearch, List<StockDto> stockDtoList){
        if(stockDtoList!=null && stockDtoList.size() > 0 && tickerToSearch!=null && tickerToSearch.length()>0){
            for(StockDto stock : stockDtoList){
                if(stock.getTicker().equals(tickerToSearch)){
                    return stock;
                }
            }
        }
        return null;
    }

    public void insertASingleStockReactively(StockDto stockToInsert){
        Observable.just(stockToInsert)
                .timeout(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(
                        stock -> {
                            stockRoomDb.stockDao().insertStocks(stock);
                        },
                        throwable -> {
                          throwable.printStackTrace(); Assert.fail();
                        }
                );
    }

    public void searchForStockReactively(String tickerToSearch){
        Observable.just(tickerToSearch)
                .timeout(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(
                        emitter -> {stockToTestAgainst = stockRoomDb.stockDao().searchForASingleStock(emitter);},
                        throwable -> {throwable.printStackTrace(); Assert.fail("cant find " + tickerToSearch);}
                );
    }

    public void deleteReactively(String tickerToDelete){
        Observable.just(tickerToDelete)
                .timeout(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(
                        stock -> {
                            stockRoomDb.stockDao().deleteASingleStock(stock);
                        },
                        throwable -> {
                            throwable.printStackTrace(); Assert.fail();
                        }
                );
    }



    @Test
    public void databaseFlowableDeleteTest(){
        givenInitializedDatabaseWithTwoTestStockDtos();
        whenTestStockOneIsDeleted();
        //give time for events to finish emitting
        SystemClock.sleep(1000);
        testStockTwoRemainsInTheStockDtoList();
    }

    public void givenInitializedDatabaseWithTwoTestStockDtos(){
        initializeInMemoryTestDatabase();
        StockDto stockDto1 = new StockDto();
        stockDto1.setTicker(testTicker1);
        StockDto stockDto2 = new StockDto();
        stockDto2.setTicker(testTicker2);
        insertASingleStockReactively(stockDto1);
        insertASingleStockReactively(stockDto2);
    }

    public void whenTestStockOneIsDeleted(){
        deleteReactively(testTicker1);
    }

    public void testStockTwoRemainsInTheStockDtoList(){
        System.out.println();
        Assert.assertTrue(databaseStockData.size() == 1);
        Assert.assertEquals(testTicker2, databaseStockData.get(0).getTicker());
        disposable.dispose();
        Assert.assertTrue("failed to dispose of disposable", disposable.isDisposed());
    }

}


