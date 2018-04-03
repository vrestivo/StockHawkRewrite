package com.example.devbox.stockhawkrewrite.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.presenter.IStockDetailsPresenter;
import com.example.devbox.stockhawkrewrite.presenter.StockDetailsPresenter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class StockDetailActivity extends AppCompatActivity implements IStockDetailsView {

    public static final String EXTRA_TICKER = "EXTRA_TICKER";
    private StockDto mStockToShow;
    private IStockDetailsPresenter mDetailsPresenter;
    private String mTickerString;

    private TextView mTicker;
    private TextView mName;
    private TextView mPrice;
    private TextView mBid;
    private TextView mAsk;
    private TextView mCurrencyChange;
    private TextView mPercentChange;
    private TextView mYearHigh;
    private TextView mYearLow;
    private LineChart mPriceChart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);


        mDetailsPresenter = new StockDetailsPresenter(this, getApplicationContext());
        bindViews();

        if(savedInstanceState!=null){
            //TODO restore mStockToShow from parcelable
        }
        else {
            showEmpty();
        }

    }

    private void bindViews(){
        mTicker = findViewById(R.id.detail_ticker);
        mName = findViewById(R.id.detail_company_name);
        mPrice = findViewById(R.id.detail_price);
        mBid = findViewById(R.id.detail_bid);
        mAsk = findViewById(R.id.detail_ask);
        mCurrencyChange = findViewById(R.id.detail_currency_change);
        mPercentChange = findViewById(R.id.detail_percent_change);
        mYearHigh = findViewById(R.id.detail_year_high);
        mYearLow = findViewById(R.id.detail_year_low);
        mPriceChart = findViewById(R.id.detail_price_chart);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cleanup();
        //TODO save Stock to parcelable
    }

    @Override
    public void getStockData(String stockTicker) {
        if(mDetailsPresenter!=null){
            mDetailsPresenter.getStockByTicker(stockTicker);
        }
    }

    @Override
    public void onDataLoaded(StockDto stockDto) {
        if(stockDto!=null){
            mStockToShow = stockDto;
            applyStockData(stockDto);
            return;
        }
        showEmpty();
    }

    private void applyStockData(StockDto stockDto){
        mName.setText(getString(R.string.detail_name, stockDto.getName()));
        mTicker.setText(getString(R.string.detail_ticker, stockDto.getTicker()));
        mPrice.setText(getString(R.string.detail_price, stockDto.getRegPrice()));
        mAsk.setText(getString(R.string.detail_ask, stockDto.getAsk()));
        mBid.setText(getString(R.string.detail_bid, stockDto.getBid()));
        mCurrencyChange.setText(getString(R.string.detail_currency_change, stockDto.getChangeCurrency()));
        mPercentChange.setText(getString(R.string.detail_percent_change, stockDto.getChangePercent()));
        mYearHigh.setText(getString(R.string.detail_year_high, stockDto.getYearHigh()));
        mYearLow.setText(getString(R.string.detail_year_low, stockDto.getYearLow()));
        loadChartData(stockDto.getHistory());
    }

    private void loadChartData(List<Entry> entryList){
        if(entryList!=null && entryList.size()>0){
            LineDataSet dataSet = new LineDataSet(entryList, getString(R.string.detail_chart_label));
            mPriceChart.setData(new LineData(dataSet));
            mPriceChart.invalidate();
        }
    }

    @Override
    public void showEmpty() {
        applyStockData(new StockDto());
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
