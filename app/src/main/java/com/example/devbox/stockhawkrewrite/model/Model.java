package com.example.devbox.stockhawkrewrite.model;

import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Flowable;

/**
 * Created by devbox on 3/13/18.
 */

public class Model implements IModel {

    private static Model sModelInstance;
    private static StockRoomDb sStockRoomDb;
    private static ExecutorService sExecutor;


    private Model() {
        if (sModelInstance!=null){
            throw new RuntimeException("Illegal operation: instantiation should be done by getInstance()");
        }
    }




    @Override
    public IModel getInstance() {
        if(sModelInstance==null){
            sModelInstance = new Model();
            sExecutor = Executors.newSingleThreadExecutor();
        }
        return sModelInstance;
    }

    @Override
    public void fetchStocksAndStoreInDatabase() {

    }

    @Override
    public Flowable<List<StockDto>> getFlowableStockList() {
        return sStockRoomDb.stockDao().getAllStocksFlowable();
    }

    @Override
    public void clearStockDatabase() {

    }

    @Override
    public void addASingleStock(String stockToAdd) {

    }

    @Override
    public void deleteASingleStock(String ticker) {

    }

    @Override
    public synchronized void refreshStockData() {

        //TODO get a list of stocks to refresh
        //TODO download stocks data
        //TODO check if data for all stocks has been downloaded
        //TODO save stock data to the database
        //TODO notify user of errors
    }

    @Override
    public void bindPresenter(IStockListPresenter presenter) {

    }

    @Override
    public void unbindPresenter() {

    }


    @Override
    public synchronized String[] getAllStockTickers() {
        sExecutor.
        return sStockRoomDb.stockDao().getAllStockTickers();
    }
}
