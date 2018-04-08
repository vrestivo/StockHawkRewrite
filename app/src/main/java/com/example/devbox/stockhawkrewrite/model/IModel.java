package com.example.devbox.stockhawkrewrite.model;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.devbox.stockhawkrewrite.exceptions.StockHawkException;
import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by devbox on 3/9/18.
 */

public interface IModel {

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public StockRoomDb getStockRoomDb();

    void refreshStockData();

    void fetchASingleStockAndStoreInDatabase(String stockToAdd);

    Flowable<List<StockDto>> getFlowableStockList();

    Flowable <StockDto> getASingleStockFlowable(String stockToGet);

    void deleteASingleStock(String ticker);

    void clearStockDatabase();

    void sendMessageToUI(String errorMessage);

    void unbindStockListPresenter();

    void unbindStockDetailPresenter();


    //TODO delete if not needed
    interface DataLoaderCallbacks{
        void onDataNotAvailable();
        void onDataError(String errorMessage);
    }

}
