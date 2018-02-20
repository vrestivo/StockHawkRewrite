package com.example.devbox.stockhawkrewrite.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by devbox on 2/20/18.
 */

public class StockDto implements IStockDto {

    private int mId;
    private String mTicker;
    private long mBid;
    private long mAsk;
    private List<Entry> mHistory;
    private String Name;
    private long mChangeCurrency;
    private long mChangePercent;
    private long mYearHigh;
    private long mYearLow;

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
    public long getmBid() {
        return mBid;
    }

    @Override
    public void setmBid(long mBid) {
        this.mBid = mBid;
    }

    @Override
    public long getmAsk() {
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
        return Name;
    }

    @Override
    public void setName(String name) {
        Name = name;
    }

    @Override
    public long getmChangeCurrency() {
        return mChangeCurrency;
    }

    @Override
    public void setmChangeCurrency(long mChangeCurrency) {
        this.mChangeCurrency = mChangeCurrency;
    }

    @Override
    public long getmChangePercent() {
        return mChangePercent;
    }

    @Override
    public void setmChangePercent(long mChangePercent) {
        this.mChangePercent = mChangePercent;
    }

    @Override
    public long getmYearHigh() {
        return mYearHigh;
    }

    @Override
    public void setmYearHigh(long mYearHigh) {
        this.mYearHigh = mYearHigh;
    }

    @Override
    public long getmYearLow() {
        return mYearLow;
    }

    @Override
    public void setmYearLow(long mYearLow) {
        this.mYearLow = mYearLow;
    }
}
