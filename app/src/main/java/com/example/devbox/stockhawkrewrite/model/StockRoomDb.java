package com.example.devbox.stockhawkrewrite.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by devbox on 2/21/18.
 */

@Database(entities = StockDto.class, version = 1)
public abstract class StockRoomDb extends RoomDatabase {
    public static final String DATABASE_NAME = "stock.db";
    private static StockRoomDb sDatabaseInstance;

    public abstract IStockDao stockDao();

    private static final Object sLock = new Object();

    public static StockRoomDb getsDatabaseInstance(Context context) {
        synchronized (sLock) {
            if (sDatabaseInstance == null) {
                sDatabaseInstance = Room
                        .databaseBuilder(
                                context,
                                StockRoomDb.class,
                                DATABASE_NAME
                        ).build();
            }
            return sDatabaseInstance;
        }
    }

}
