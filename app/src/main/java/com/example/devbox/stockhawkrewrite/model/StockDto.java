package com.example.devbox.stockhawkrewrite.model;

import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yahoofinance.Stock;

/**
 * Stock Data Transfer Object
 */


public class StockDto implements IStockDto {

    private int mId;
    private String mTicker;
    private float mBid;
    private float mAsk;
    private List<Entry> mHistory;
    private String mName;
    private float mChangeCurrency;
    private float mChangePercent;
    private float mYearHigh;
    private float mYearLow;

    public StockDto(Stock stock) {
        if(stock!=null) {
            this.mTicker = stock.getSymbol();
            this.mBid = stock.getQuote().getBid().floatValue();
            this.mAsk = stock.getQuote().getAsk().floatValue();
            mName = stock.getName();
            this.mChangeCurrency = stock.getQuote().getChange().floatValue();
            this.mChangePercent = stock.getQuote().getChangeInPercent().floatValue();
            this.mYearHigh = stock.getQuote().getYearHigh().floatValue();
            this.mYearLow = stock.getQuote().getYearLow().floatValue();

            try {
                this.mHistory = Util.historicalStockHistoricalQuoteListtoEntryList(stock.getHistory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public StockDto() {
        this.mId = 0;
        this.mTicker = "";
        this.mBid = 0;
        this.mAsk = 0;
        this.mHistory = new ArrayList<>();
        this.mName = "";
        this.mChangeCurrency = 0;
        this.mChangePercent = 0;
        this.mYearHigh = 0;
        this.mYearLow = 0;
    }

    @Override
    public int getmId() {
        return mId;
    }

    @Override
    public void setmId(int mId) {
        this.mId = mId;
    }

    @Override
    public String getmTicker() {
        return mTicker;
    }

    @Override
    public void setmTicker(String mTicker) {
        this.mTicker = mTicker;
    }

    @Override
    public float getmBid() {
        return mBid;
    }

    @Override
    public void setmBid(long mBid) {
        this.mBid = mBid;
    }

    @Override
    public float getmAsk() {
        return mAsk;
    }

    @Override
    public void setmAsk(long mAsk) {
        this.mAsk = mAsk;
    }

    @Override
    public List<Entry> getmHistory() {
        return mHistory;
    }

    @Override
    public void setmHistory(List<Entry> mHistory) {
        this.mHistory = mHistory;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public float getmChangeCurrency() {
        return mChangeCurrency;
    }

    @Override
    public void setmChangeCurrency(long mChangeCurrency) {
        this.mChangeCurrency = mChangeCurrency;
    }

    @Override
    public float getmChangePercent() {
        return mChangePercent;
    }

    @Override
    public void setmChangePercent(long mChangePercent) {
        this.mChangePercent = mChangePercent;
    }

    @Override
    public float getmYearHigh() {
        return mYearHigh;
    }

    @Override
    public void setmYearHigh(long mYearHigh) {
        this.mYearHigh = mYearHigh;
    }

    @Override
    public float getmYearLow() {
        return mYearLow;
    }

    @Override
    public void setmYearLow(long mYearLow) {
        this.mYearLow = mYearLow;
    }
}
