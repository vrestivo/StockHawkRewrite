package com.example.devbox.stockhawkrewrite.model;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by devbox on 3/9/18.
 */

public interface IModel {

    void fetchStocksAndStoreInDatabase();

    Flowable<List<StockDto>> getFlowableStockList();

    void clearStockDatabase();

    void fetchASingleStockAndStoreInDatabase(String stockToAdd); //TODO add throws clause

    void deleteASingleStock(String ticker);

    void refreshStockData();

    void bindPresenter(IStockListPresenter presenter);

    void unbindPresenter();

    StockDto getStockFromDbByTicker(String tickerToGet);

    String[] getAllStockTickers();

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    StockRoomDb getsStockRoomDb();

}
