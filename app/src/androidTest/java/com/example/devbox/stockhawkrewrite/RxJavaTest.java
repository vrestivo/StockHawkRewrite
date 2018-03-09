package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava Instrumented Tests
 */

public class RxJavaTest {
    private String[] validStocks = {"IBM", "BA", "TSLA"};
    private YFNetDao yfNetDao;
    private List<StockDto> stocks;
    Observable<List<StockDto>> yahooObservable;


    @After
    public void cleanup(){

    }



    @Test
    public void rxJavaStockFetchingTest(){
        givenInitializedYFNetDaoAndObservable();
        whenFetchingStocksAsynchronously();
        validResultsAreAvailableOnTheMainThread();
    }


    public void givenInitializedYFNetDaoAndObservable(){
        yfNetDao = new YFNetDao();
        yahooObservable = Observable.fromCallable(() -> yfNetDao.fetchStocks(validStocks));
    }


    public void whenFetchingStocksAsynchronously(){
        yahooObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(7000, TimeUnit.MILLISECONDS)
                .blockingSubscribe(stockDtoList -> {
                    System.out.println("_in emitter");
                    Assert.assertNotNull(stockDtoList);
                    Assert.assertEquals("wrong stock list size", validStocks.length, stockDtoList.size());
                    stocks = stockDtoList;
                }, e -> {e.printStackTrace(); Assert.fail();
                });
    }


    public void validResultsAreAvailableOnTheMainThread(){
        List<String> tickerValidationList = Arrays.asList(validStocks);
        for(StockDto stockDto : stocks){
            Assert.assertTrue("Invalid Stock Ticker", tickerValidationList.contains(stockDto.getTicker()));
        }
    }


}
