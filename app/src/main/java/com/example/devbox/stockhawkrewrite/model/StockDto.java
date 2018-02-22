package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yahoofinance.Stock;

/**
 * Stock Data Transfer Object
 */

@Entity(tableName = "stocks")
public class StockDto implements IStockDto {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "ticker")
    private String mTicker;
    @ColumnInfo(name = "bid")
    private float mBid;
    @ColumnInfo(name = "ask")
    private float mAsk;
    @TypeConverters(HistoricalStockDataTypeConverter.class)
    @ColumnInfo(name = "history")
    private List<Entry> mHistory;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "change_cur")
    private float mChangeCurrency;
    @ColumnInfo(name = "change_per")
    private float mChangePercent;
    @ColumnInfo(name = "year_high")
    private float mYearHigh;
    @ColumnInfo(name = "year_low")
    private float mYearLow;

    @Ignore
    public StockDto(Stock stock) {
        if (stock != null) {
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
    public int getId() {
        return this.mId;
    }

    @Override
    public void setId(int mId) {
        this.mId = mId;
    }

    @Override
    public String getTicker() {
        return mTicker;
    }

    @Override
    public void setTicker(String mTicker) {
        this.mTicker = mTicker;
    }

    @Override
    public float getBid() {
        return mBid;
    }

    @Override
    public void setBid(float mBid) {
        this.mBid = mBid;
    }

    @Override
    public float getAsk() {
        return this.mAsk;
    }

    @Override
    public void setAsk(float mAsk) {
        this.mAsk = mAsk;
    }

    @Override
    public List<Entry> getHistory() {
        return mHistory;
    }

    @Override
    public void setHistory(List<Entry> mHistory) {
        this.mHistory = mHistory;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String mName) {
        this.mName = mName;
    }

    @Override
    public float getChangeCurrency() {
        return mChangeCurrency;
    }

    @Override
    public void setChangeCurrency(float mChangeCurrency) {
        this.mChangeCurrency = mChangeCurrency;
    }

    @Override
    public float getChangePercent() {
        return mChangePercent;
    }

    @Override
    public void setChangePercent(float mChangePercent) {
        this.mChangePercent = mChangePercent;
    }

    @Override
    public float getYearHigh() {
        return mYearHigh;
    }

    @Override
    public void setYearHigh(float mYearHigh) {
        this.mYearHigh = mYearHigh;
    }

    @Override
    public float getYearLow() {
        return mYearLow;
    }

    @Override
    public void setYearLow(float mYearLow) {
        this.mYearLow = mYearLow;
    }
}