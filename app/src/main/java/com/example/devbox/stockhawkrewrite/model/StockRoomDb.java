package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by devbox on 2/21/18.
 */

@Database(entities = StockDto.class, version = 1)
public abstract class StockRoomDb extends RoomDatabase {
    public abstract IStockDao stockDao();
}
