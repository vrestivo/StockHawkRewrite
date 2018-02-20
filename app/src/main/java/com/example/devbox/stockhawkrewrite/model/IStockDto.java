package com.example.devbox.stockhawkrewrite.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Interface for stock data transfer object
 */

interface IStockDto {


    int getmId();

    void setmId(int mId);

    String getmTicker();

    void setmTicker(String mTicker);

    long getmBid();

    void setmBid(long mBid);

    long getmAsk();

    void setmAsk(long mAsk);

    List<Entry> getmHistory();

    void setmHistory(List<Entry> mHistory);

    String getName();

    void setName(String name);

    long getmChangeCurrency();

    void setmChangeCurrency(long mChangeCurrency);

    long getmChangePercent();

    void setmChangePercent(long mChangePercent);

    long getmYearHigh();

    void setmYearHigh(long mYearHigh);

    long getmYearLow();

    void setmYearLow(long mYearLow);
}
