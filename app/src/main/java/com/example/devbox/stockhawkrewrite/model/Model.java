package com.example.devbox.stockhawkrewrite.model;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.devbox.stockhawkrewrite.TwoThreadTaskExecutor;
import com.example.devbox.stockhawkrewrite.exceptions.EmptyStockListException;
import com.example.devbox.stockhawkrewrite.exceptions.StockHawkException;
import com.example.devbox.stockhawkrewrite.presenter.IStockDetailsPresenter;
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
    private static IStockListPresenter sStockListPresenter;
    private static IStockDetailsPresenter sStockDetailsPresenter;
    private static IYFNetDao sYFNetDao;


    private Model() {
        if (sModelInstance!=null){
            throw new RuntimeException("Illegal operation: instantiation should be done by getInstance()");
        }
    }

    public static IModel getInstance(Context context, IStockListPresenter stockListPresenter) {
        initModel(context);
        sStockListPresenter = stockListPresenter;
        return sModelInstance;
    }

    public static IModel getInstance(Context context, IStockDetailsPresenter stockDetailsPresenter) {
        initModel(context);

        sStockDetailsPresenter = stockDetailsPresenter;
        return sModelInstance;
    }

    private static void initModel(Context context){
        if(sModelInstance==null){
            sModelInstance = new Model();
            sYFNetDao = new YFNetDao();
            sStockRoomDb = StockRoomDb.getsDatabaseInstance(context);
        }

        if(sExecutor == null){
            sExecutor = new TwoThreadTaskExecutor();
        }

    }

        @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public StockRoomDb getStockRoomDb(){
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
                catch (EmptyStockListException emptyListException){
                    sendMessageToUIOnMainThread(emptyListException.getMessage());
                }
                catch (StockHawkException exception){
                    sendMessageToUIOnMainThread(exception.getMessage());
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
                        sendMessageToUIOnMainThread(stockToAdd + " Added");
                    }
                    catch (StockHawkException exception){
                        exception.printStackTrace();
                        System.out.println("DEBUG: _inside catch");
                        sendMessageToUIOnMainThread(exception.getMessage());
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
    public Flowable<StockDto> getASingleStockFlowable(String stockToGet) {
        //Room library will handle this asynchronously
        return sStockRoomDb.stockDao().getASingleStockFlowable(stockToGet);
    }


    @Override
    public void deleteASingleStock(String ticker) {
        Runnable deleteStockTask = new Runnable() {
            @Override
            public void run() {
                sStockRoomDb.stockDao().deleteASingleStock(ticker);
                sendMessageToUIOnMainThread(ticker + " deleted");
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

    private void sendMessageToUIOnMainThread(String message){
        sExecutor.mainThreadExecutor().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        sendMessageToUI(message);
                    }
                }
        );
    }

    @Override
    public void sendMessageToUI(String message) {
        if(sStockListPresenter !=null) {
            sStockListPresenter.sendMessageToUI(message);
        }
    }


    @Override
    public void unbindStockListPresenter() {
        if(sStockListPresenter !=null){
            sStockListPresenter = null;
        }
    }

    @Override
    public void unbindStockDetailPresenter() {
        if(sStockDetailsPresenter != null){
            sStockDetailsPresenter = null;
        }
    }

    @Override
    public void onDataNotAvailable() {
        if(sStockListPresenter !=null){
            sStockListPresenter.notifyDatabaseEmpty();
        }
    }


    @Override
    public void onDataError(String errorMessage) {
        sendMessageToUI(errorMessage);
    }
}
