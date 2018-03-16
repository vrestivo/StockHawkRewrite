package com.example.devbox.stockhawkrewrite.presenter;

import android.support.annotation.VisibleForTesting;

import com.example.devbox.stockhawkrewrite.model.IModel;
import com.example.devbox.stockhawkrewrite.model.StockDto;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Presenter for the StockListView
 */

public interface IStockListPresenter {

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    IModel getModel();


    void loadStocks();
    void addAStock(String stockTicker);
    void deleteAStock(String stockTickerToDelete);
    void refreshStockData();
    void notifyError(String errorMessage);
    void notifyDatabaseEmpty();
    void cleanup();
}
