package com.example.devbox.stockhawkrewrite.model;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Flowable;

/**
 * Model Implementation
 */

public class Model implements IModel {

    private static IModel sModelInstance;
    private static StockRoomDb sStockRoomDb;
    private static ExecutorService sExecutor;
    private static IStockListPresenter sPresenter;
    private static IYFNetDao sYFNetDao;




    private Model() {
        if (sModelInstance!=null){
            throw new RuntimeException("Illegal operation: instantiation should be done by getInstance()");
        }
    }

    public static IModel getInstance(Context context) {
        if(sModelInstance==null){
            sModelInstance = new Model();
            sExecutor = Executors.newSingleThreadExecutor();
            sYFNetDao = new YFNetDao();
           sStockRoomDb = StockRoomDb.getsDatabaseInstance(context);
        }
        return sModelInstance;
    }

    //TODO delete after testing
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public synchronized StockRoomDb getsStockRoomDb(){
        return sStockRoomDb;
    }

    @Override
    public synchronized void fetchStocksAndStoreInDatabase() {

    }

    @Override
    public Flowable<List<StockDto>> getFlowableStockList() {
        return sStockRoomDb.stockDao().getAllStocksFlowable();
    }

    @Override
    public void clearStockDatabase() {

    }

    @Override
    public void fetchASingleStockAndStoreInDatabase(String stockToAdd) {

    }

    @Override
    public void deleteASingleStock(String ticker) {

    }

    @Override
    public synchronized void refreshStockData() {

        //TODO get a list of stocks to refresh
        String[] tickerToFetch = getAllStockTickers();
        List<StockDto> fetchedStocks = sYFNetDao.fetchStocks(tickerToFetch);
        //TODO download stocks data
        //TODO check if data for all stocks has been downloaded
        //TODO save stock data to the database
        //TODO notify user of errors
    }

    @Override
    public StockDto getStockFromDbByTicker(String tickerToGet) {
        return null;
    }

    @Override
    public void bindPresenter(IStockListPresenter presenter) {

    }

    @Override
    public void unbindPresenter() {
        if(!sExecutor.isShutdown()){
            sExecutor.shutdownNow();
        }
        sPresenter = null;
    }


    @Override
    public synchronized String[] getAllStockTickers() {
        return sStockRoomDb.stockDao().getAllStockTickers();
    }
}
