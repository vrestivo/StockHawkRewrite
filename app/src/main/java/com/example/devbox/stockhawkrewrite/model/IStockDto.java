package com.example.devbox.stockhawkrewrite.model;

import android.os.Parcelable;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Interface for stock data transfer object
 */

interface IStockDto extends Parcelable {


    int getId();

    void setId(int mId);

    String getTicker();

    void setTicker(String mTicker);

    float getRegPrice();

    void setRegPrice(float mRegPrice);

    float getBid();

    void setBid(float mBid);

    float getAsk();

    void setAsk(float mAsk);

    List<Entry> getHistory();

    void setHistory(List<Entry> mHistory);

    String getName();

    void setName(String mName);

    float getChangeCurrency();

    void setChangeCurrency(float mChangeCurrency);

    float getChangePercent();

    void setChangePercent(float mChangePercent);

    float getYearHigh();

    void setYearHigh(float mYearHigh);

    float getYearLow();

    void setYearLow(float mYearLow);
}
