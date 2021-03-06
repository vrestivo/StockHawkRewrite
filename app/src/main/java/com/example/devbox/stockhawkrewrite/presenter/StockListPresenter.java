package com.example.devbox.stockhawkrewrite.presenter;

import android.content.Context;

import com.example.devbox.stockhawkrewrite.model.IModel;
import com.example.devbox.stockhawkrewrite.model.Model;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.view.IStockListView;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Stock list presenter
 */

public class StockListPresenter implements IStockListPresenter {

    private IStockListView mView;
    private IModel mModel;
    private Flowable<List<StockDto>> mStockListFlowable;
    private Disposable mStockDataDisposable;

    public StockListPresenter(IStockListView view, Context context) {
        mView = view;
        mModel = Model.getInstance(context, this);
        loadStocks();
    }

    @Override
    public IModel getModel() {
        return mModel;
    }

    @Override
    public void loadStocks() {
        if (mStockListFlowable == null) {
            mStockListFlowable = mModel.getFlowableStockList();
            mStockDataDisposable = mStockListFlowable
                    .onBackpressureLatest()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            stockDtoList -> {
                                if (stockDtoList != null) {
                                    mView.onStockListLoaded(stockDtoList);
                                }
                            }
                    );
        }
    }

    @Override
    public void addAStock(String stockTicker) {
        if (mModel != null) {
            mModel.fetchASingleStockAndStoreInDatabase(stockTicker);
        }
    }

    @Override
    public void deleteAStock(String stockTickerToDelete) {
        if (mModel != null) {
            mModel.deleteASingleStock(stockTickerToDelete);
        }
    }

    @Override
    public void refreshStockData() {
        if (mModel != null) {
            mModel.refreshStockData();
        }
    }

    @Override
    public void sendMessageToUI(String errorMessage) {
        if (mView != null) {
            mView.displayMessage(errorMessage);
        }
    }

    @Override
    public void notifyDatabaseEmpty() {
        if (mView != null) {
            mView.showEmptyList();
        }
    }

    @Override
    public void cleanup() {
        if (mStockDataDisposable != null && !mStockDataDisposable.isDisposed()) {
            mStockDataDisposable.dispose();
        }
        if (mModel != null) {
            mModel.unbindStockListPresenter();
            mModel = null;
        }
    }
}
