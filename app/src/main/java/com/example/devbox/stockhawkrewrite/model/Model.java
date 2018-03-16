package com.example.devbox.stockhawkrewrite.model;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.devbox.stockhawkrewrite.TwoThreadTaskExecutor;
import com.example.devbox.stockhawkrewrite.exceptions.StockHawkException;
import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

import java.util.List;
import io.reactivex.Flowable;

/**
 * Model Implementation
 */

public class Model implements IModel, IModel.DataLoaderCallbacks {

    private static IModel sModelInstance;
    private static StockRoomDb sStockRoomDb;
    private static TwoThreadTaskExecutor sExecutor;
    private static IStockListPresenter sPresenter;
    private static IYFNetDao sYFNetDao;


    private Model() {
        if (sModelInstance!=null){
            throw new RuntimeException("Illegal operation: instantiation should be done by getInstance()");
        }
    }

    public static IModel getInstance(Context context, IStockListPresenter stockListPresenter) {
        if(sModelInstance==null){
            sModelInstance = new Model();
            sYFNetDao = new YFNetDao();
           sStockRoomDb = StockRoomDb.getsDatabaseInstance(context);
        }

        if(sExecutor == null){
            sExecutor = new TwoThreadTaskExecutor();
        }

        sPresenter = stockListPresenter;
        return sModelInstance;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public StockRoomDb getsStockRoomDb(){
        return sStockRoomDb;
    }



    @Override
    public void refreshStockData() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    String[] stockTickersToFetch = sStockRoomDb.stockDao().getAllStockTickers();
                    List<StockDto> fetchedStocks = sYFNetDao.fetchStocks(stockTickersToFetch);
                    for (StockDto stock: fetchedStocks) {
                        sStockRoomDb.stockDao().insertStocks(stock);
                    }
                }
                catch (StockHawkException exception){
                    sExecutor.mainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            notifyError(exception.getMessage());
                        }
                    });
                }
            }
        };
        sExecutor.iOExecutor().execute(task);
    }


    @Override
    public void fetchASingleStockAndStoreInDatabase(String stockToAdd) {
        if(stockToAdd!=null) {
            Runnable fetchASingleSTockTask = new Runnable() {
                @Override
                public void run() {
                    try{
                        StockDto fetchedStock = sYFNetDao.fetchASingleStock(stockToAdd);
                        sStockRoomDb.stockDao().insertStocks(fetchedStock);
                    }
                    catch (StockHawkException exception){
                        exception.printStackTrace();
                        System.out.println("DEBUG: _inside catch");
                        sExecutor.mainThreadExecutor().execute(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyError(exception.getMessage());
                                    }
                                }
                        );
                    }
                }
            };
            sExecutor.iOExecutor().execute(fetchASingleSTockTask);
        }
    }


    @Override
    public Flowable<List<StockDto>> getFlowableStockList() {
        //Room library will handle this asynchronously
        return sStockRoomDb.stockDao().getAllStocksFlowable();
    }


    @Override
    public void deleteASingleStock(String ticker) {
        Runnable deleteStockTask = new Runnable() {
            @Override
            public void run() {
                sStockRoomDb.stockDao().deleteASingleStock(ticker);
            }
        };

        sExecutor.iOExecutor().execute(deleteStockTask);
    }


    @Override
    public void clearStockDatabase() {
        Runnable clearAllTask = new Runnable() {
            @Override
            public void run() {
                sStockRoomDb.stockDao().deleteAllStocks();
            }
        };
        sExecutor.iOExecutor().execute(clearAllTask);
    }


    @Override
    public void notifyError(String errorMessage) {
        if(sPresenter!=null) {
            sPresenter.notifyError(errorMessage);
        }
    }


    @Override
    public void unbindPresenter() {
        if(sPresenter!=null){
            sPresenter = null;
        }
    }


    @Override
    public void onDataNotAvailable() {
        if(sPresenter!=null){
            sPresenter.notifyDatabaseEmpty();
        }
    }


    @Override
    public void onDataError(String errorMessage) {
        notifyError(errorMessage);
    }
}
