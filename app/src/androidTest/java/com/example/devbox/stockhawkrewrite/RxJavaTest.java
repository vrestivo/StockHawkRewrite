package com.example.devbox.stockhawkrewrite;

import android.os.SystemClock;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava Instrumented Tests
 */

public class RxJavaTest {
    private String[] validStocks = {"IBM", "BA", "TSLA"};
    private YFNetDao yfNetDao;
    private List<StockDto> stocks;
    Observable<List<StockDto>> yahooObservable;
    private Disposable disposable;


    @After
    public void cleanup(){
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }



    @Test
    public void rxJavaStockFetchingTest(){
        givenInitializedYFNetDaoAndObservable();
        whenFetchingStocksAsynchronously();
        SystemClock.sleep(50000);
        validResultsAreAvailableOnTheMainThread();
    }


    public void givenInitializedYFNetDaoAndObservable(){
        yfNetDao = new YFNetDao();
        yahooObservable = Observable.fromCallable(() -> yfNetDao.fetchStocks(validStocks));
    }


    public void whenFetchingStocksAsynchronously(){
        disposable = yahooObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockDtoList -> {
                    System.out.println("_in emitter");
                    Assert.assertNotNull(stockDtoList);
                    Assert.assertEquals("wrong stock list size", validStocks.length, stockDtoList.size());
                    stocks = stockDtoList;
                    }, e -> {e.printStackTrace(); Assert.fail();
                });
    }

    public void validResultsAreAvailableOnTheMainThread(){
        int counter = 0;
        List<String> tickerValiationList = Arrays.asList(validStocks);
        for(StockDto stockDto : stocks){
            Assert.assertTrue("Invalid Stock Ticker", tickerValiationList.contains(stockDto.getTicker()));
        }
    }


}
