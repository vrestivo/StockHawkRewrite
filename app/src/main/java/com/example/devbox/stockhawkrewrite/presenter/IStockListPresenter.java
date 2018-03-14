package com.example.devbox.stockhawkrewrite.presenter;

import com.example.devbox.stockhawkrewrite.model.StockDto;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by devbox on 3/9/18.
 */

public interface IStockListPresenter {
    Flowable<List<StockDto>> loadStocks();
    void addStock(String stockTickerToAdd);
    void notifyStockListUpdated(List<StockDto> newStockList);
    void notifyError(String errorMessage);
}
