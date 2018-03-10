package com.example.devbox.stockhawkrewrite.model;

import com.example.devbox.stockhawkrewrite.presenter.IStockPresenter;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by devbox on 3/9/18.
 */

interface IModel {

    void fetchStocksAndStoreInDatabase();

    Flowable<List<StockDto>> getFlowableStockList();

    void clearStockDatabase();

    void addASingleStock(String stockToAdd); //TODO add throws clause

    void deleteASingleStock(String ticker);

    void refreshStockData();

    void bindPresenter(IStockPresenter presenter);

    void unbindPresenter();

}
