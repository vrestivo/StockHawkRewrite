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
        mModel = Model.getInstance(context.getApplicationContext(), this);
        loadStocks();
    }

    @Override
    public IModel getModel() {
        return mModel;
    }

    @Override
    public void loadStocks() {
        if(mStockListFlowable==null){
            mStockListFlowable = mModel.getFlowableStockList();
            mStockDataDisposable = mStockListFlowable
                   .onBackpressureLatest()
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(
                   stockDtoList -> {
                       if(stockDtoList!=null && stockDtoList.size() > 0) {
                           mView.onStockListLoaded(stockDtoList);
                       }
                   }
           );
        }
    }

    @Override
    public void addAStock(String stockTicker) {
        mModel.fetchASingleStockAndStoreInDatabase(stockTicker);
    }

    @Override
    public void deleteAStock(String stockTickerToDelete) {
        mModel.deleteASingleStock(stockTickerToDelete);
    }

    @Override
    public void refreshStockData() {
        mModel.refreshStockData();
    }

    @Override
    public void notifyError(String errorMessage) {
        if(mView!=null){
            mView.displayError(errorMessage);
        }
    }

    @Override
    public void notifyDatabaseEmpty() {
        if(mView!=null){
            mView.showListIsEmpty();
        }
    }

    @Override
    public void cleanup() {
        if(!mStockDataDisposable.isDisposed()){
            mStockDataDisposable.dispose();
        }
        mModel.unbindPresenter();
        mModel = null;
    }
}
