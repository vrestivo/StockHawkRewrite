package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.devbox.stockhawkrewrite.Util.Util;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yahoofinance.Stock;

/**
 * Stock Data Transfer Object
 */

@Entity(tableName = "stocks", indices = {@Index(value = {"ticker"}, unique = true)})
public class StockDto implements IStockDto, Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "ticker")
    private String mTicker;
    @ColumnInfo(name = "reg_price")
    private float mRegPrice;
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
    private final String STRING_DEFAULT = "";

    @Ignore
    public StockDto(Stock stock) {
        if (stock != null) {
            Util.normalizeStockDtoInitialization(this, stock);
            try {
                this.mHistory = Util.historicalStockHistoricalQuoteListtoEntryList(stock.getHistory());
            } catch (IOException e) {
                e.printStackTrace();
                this.mHistory = new ArrayList<Entry>();
            }
        } else {
            initializeMembersWithNormalizedValues();
        }
    }


    public StockDto() {
        initializeMembersWithNormalizedValues();
    }

    private void initializeMembersWithNormalizedValues() {
        this.mId = 0;
        this.mTicker = "";
        this.mRegPrice = 0;
        this.mBid = 0;
        this.mAsk = 0;
        this.mHistory = new ArrayList<>();
        this.mName = "";
        this.mChangeCurrency = 0;
        this.mChangePercent = 0;
        this.mYearHigh = 0;
        this.mYearLow = 0;
    }


    //Parcelable Implementation
    private StockDto(Parcel source) {
        setId(source.readInt());
        setTicker(source.readString());
        setRegPrice(source.readFloat());
        setBid(source.readFloat());
        setAsk(source.readFloat());
        setHistory(Util.historicalStockQuotesCSVtoEntryList(source.readString()));
        setName(source.readString());
        setChangeCurrency(source.readFloat());
        setChangePercent(source.readFloat());
        setYearHigh(source.readFloat());
        setYearLow(source.readFloat());
    }


    public static final Parcelable.Creator<StockDto> CREATOR = new Parcelable.Creator<StockDto>() {
        @Override
        public StockDto createFromParcel(Parcel source) {
            return new StockDto(source);
        }

        @Override
        public StockDto[] newArray(int size) {
            return new StockDto[0];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTicker);
        dest.writeFloat(mRegPrice);
        dest.writeFloat(mBid);
        dest.writeFloat(mAsk);
        dest.writeString(Util.historicalStockQuoteEntryListToCSVString(mHistory));
        dest.writeString(mName);
        dest.writeFloat(mChangeCurrency);
        dest.writeFloat(mChangePercent);
        dest.writeFloat(mYearHigh);
        dest.writeFloat(mYearLow);
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
        if(mTicker==null){
            this.mTicker = STRING_DEFAULT;
            return;
        }
        this.mTicker = mTicker;
    }

    @Override
    public float getRegPrice() {
        return mRegPrice;
    }

    @Override
    public void setRegPrice(float mRegPrice) {
        this.mRegPrice = mRegPrice;
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
        if (mHistory == null) {
            this.mHistory = new ArrayList<>();
            return;
        }
        this.mHistory = mHistory;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String mName) {
        if(mName == null){
            this.mName = STRING_DEFAULT;
        }
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