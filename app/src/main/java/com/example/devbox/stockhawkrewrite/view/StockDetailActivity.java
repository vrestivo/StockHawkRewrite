package com.example.devbox.stockhawkrewrite.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.presenter.IStockDetailsPresenter;

public class StockDetailActivity extends AppCompatActivity implements IStockDetailsView {

    private StockDto mStockToShow;
    private IStockDetailsPresenter mDetailsPresenter;

    private TextView mTicker;
    private TextView mName;
    private TextView mPrice;
    private TextView mBid;
    private TextView mAsk;
    private TextView mCurrencyChange;
    private TextView mPercentChange;
    private TextView mYearHigh;
    private TextView mYearLow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            //TODO restore mStockToShow from parcelable


        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        cleanup();
    }

    @Override
    public void getStockData(String stockTicker) {

    }

    @Override
    public void onDataLoaded(StockDto stockDto) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void cleanup() {
        if(mDetailsPresenter!=null){
            mDetailsPresenter.cleanup();
        }

    }

    @Override
    public void displayError(String ErrorMessage) {

    }
}
