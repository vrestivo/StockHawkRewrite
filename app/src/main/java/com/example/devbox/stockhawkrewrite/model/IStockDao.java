package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import yahoofinance.Stock;


/**
 * Created by devbox on 2/21/18.
 */

@Dao
public interface IStockDao {

    @Query("SELECT * FROM stocks;")
    List<StockDto> getAllStocks();


    @Query("SELECT * FROM stocks WHERE ticker == :ticker")
    StockDto searchForASingleStock(String ticker);

    @Query("SELECT * FROM stocks;")
    Flowable<List<StockDto>> getAllStocksFlowable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStocks(StockDto... stocks);

    @Query("DELETE FROM stocks WHERE ticker == :ticker")
    int deleteASingleStock(String ticker);

}
