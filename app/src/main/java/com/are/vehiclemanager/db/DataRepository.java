package com.are.vehiclemanager.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DataRepository {
    private static final Object LOCK = new Object();
    @NonNull
    private static DataDBDao dataDBDao;
    private static DataRoomDatabase dataRoomDatabase;
    public LiveData<List<DataDB>> mFilteredData;
    String TAG = "DataRepository";
    private LiveData<List<DataDB>> mOldData;

    public synchronized static DataRoomDatabase getDataRoomDatabase(Context context) {
        {
            dataRoomDatabase = DataRoomDatabase.getDatabase(context);
            return dataRoomDatabase;
        }
    }

    public DataDBDao getDataDAO(Context context) {
        return getDataRoomDatabase(context).dataDBDao();
    }

    public List<DataDB> getData(Context c) {
        return getDataDAO(c).getData();
    }

    public LiveData<List<DataDB>> getRecentData(Context context, String type) {
        return getDataDAO(context).getRecentData(type);
    }

    public LiveData<List<DataDB>> getPrice(Context context, String type) {
        return getDataDAO(context).getPrice(type);
    }

    public LiveData<List<DataDB>> getOldData(Context context, String type) {
        return getDataDAO(context).getOldData(type);
    }

    public LiveData<List<DataDB>> getFilterData(Context context, String filter, String type) {
        return getDataDAO(context).getFilteredData(filter, type);
    }

    void insert(DataDB dataDB, Context context) {
        new insertAsyncTask(getDataDAO(context)).execute(dataDB);
    }

    void update(DataDB dataDB, Context context) {
        new insertAsyncTask(getDataDAO(context)).execute(dataDB);
    }

    void deleteAll(Context context) {
        DataRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                getDataDAO(context).deleteAll();
            }
        });

    }

    private static class insertAsyncTask extends AsyncTask<DataDB, Void, Void> {
        private final DataDBDao mAsyncDataDBDao;

        insertAsyncTask(DataDBDao dataDBDao) {
            mAsyncDataDBDao = dataDBDao;
        }

        @Override
        protected Void doInBackground(DataDB... dataDBS) {
            mAsyncDataDBDao.insert(dataDBS[0]);
            return null;
        }
    }

}
