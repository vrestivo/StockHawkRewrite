package com.example.devbox.stockhawkrewrite.model;

import com.example.devbox.stockhawkrewrite.presenter.IStockPresenter;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by devbox on 3/9/18.
 */

public class OverallModel implements IModel {

    @Override
    public void fetchStocksAndStoreInDatabase() {

    }

    @Override
    public Flowable<List<StockDto>> getFlowableStockList() {
        return null;
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
    public void refreshStockData() {

    }

    @Override
    public void bindPresenter(IStockPresenter presenter) {

    }

    @Override
    public void unbindPresenter() {

    }
}
