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

    float getmBid();

    void setmBid(long mBid);

    float getmAsk();

    void setmAsk(long mAsk);

    List<Entry> getmHistory();

    void setmHistory(List<Entry> mHistory);

    String getName();

    void setName(String name);

    float getmChangeCurrency();

    void setmChangeCurrency(long mChangeCurrency);

    float getmChangePercent();

    void setmChangePercent(long mChangePercent);

    float getmYearHigh();

    void setmYearHigh(long mYearHigh);

    float getmYearLow();

    void setmYearLow(long mYearLow);
}
