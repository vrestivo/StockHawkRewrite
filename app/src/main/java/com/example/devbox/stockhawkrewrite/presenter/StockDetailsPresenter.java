package com.example.devbox.stockhawkrewrite.presenter;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.devbox.stockhawkrewrite.model.IModel;
import com.example.devbox.stockhawkrewrite.model.Model;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.view.IStockDetailsView;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class StockDetailsPresenter implements IStockDetailsPresenter {

    private IModel mModel;
    private IStockDetailsView mView;
    private Flowable <StockDto> mStockFlowable;
    private Disposable mStockDisposable;

    public StockDetailsPresenter(IStockDetailsView detailsView, Context context) {
        mView = detailsView;
        initModel(context);
    }

    private void initModel(Context context){
        if(mModel == null){
            mModel = Model.getInstance(context, this);
        }
    }

    @Override
    public void getStockByTicker(String tickerToGet) {
        if(tickerToGet!=null) {
            mStockFlowable = mModel.getASingleStockFlowable(tickerToGet);
            if(mStockFlowable!=null){
                mStockDisposable = mStockFlowable
                        .onBackpressureLatest()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stockDto -> onDataLoaded(stockDto));
            }
        }
    }


    @Override
    public void onDataLoaded(StockDto stockDto) {
        if(stockDto!=null){
            mView.onDataLoaded(stockDto);
            return;
        }
        mView.showEmpty();
    }


    @Override
    public void sendMessageToUI(String message) {
        if(mView!=null) {
            mView.displayError(message);
        }
    }


    @Override
    public void cleanup() {
        if(mStockDisposable!=null && !mStockDisposable.isDisposed()){
            mStockDisposable.dispose();
        }
        if(mModel!=null){
            mModel.unbindStockDetailPresenter();
            mModel = null;
        }
    }


    @VisibleForTesting
    public IModel getModel(){
        return mModel;
    }
}
