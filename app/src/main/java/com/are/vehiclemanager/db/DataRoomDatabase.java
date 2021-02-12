package com.are.vehiclemanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DataDB.class}, version = 1, exportSchema = false)
public abstract class DataRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile DataRoomDatabase INSTANCE;

    static DataRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            if (INSTANCE == null)
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        DataRoomDatabase.class, DataRoomDatabase.class.getName())
                        .build();
        }
        return INSTANCE;
    }

    public abstract DataDBDao dataDBDao();
}