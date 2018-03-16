package com.example.devbox.stockhawkrewrite.presenter;

import com.example.devbox.stockhawkrewrite.model.StockDto;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Presenter for the StockListView
 */

public interface IStockListPresenter {
    void init();
    void addStock(String stockTickerToAdd);
    void loadStocks();
    void notifyStockListUpdated(Flowable<List<StockDto>> newStockListFlowable);
    void notifyError(String errorMessage);
    void addAStock(String stockTicker);
    void deleteAStock();
    void refreshStockData();
    void notifyDatabaseEmpty();
    void cleanup();
}
