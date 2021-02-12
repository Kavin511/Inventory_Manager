
package com.are.vehiclemanager.db;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DataDBViewModel extends AndroidViewModel {
    private final DataRepository dataRepository;

    //    private final LiveData<List<DataDB>> getRecentData;
//    private final LiveData<List<DataDB>> getOldData;
//    private final LiveData<List<DataDB>> getFilterData;
    public DataDBViewModel(@NonNull Application application) {

        super(application);
        dataRepository = new DataRepository();
//        getRecentData=dataRepository.getRecentData();
//        getOldData=dataRepository.getOldData();
//        getFilterData=dataRepository.getFilterData("serial");
    }

    public LiveData<List<DataDB>> getPrice(Context context, String type) {
        return dataRepository.getPrice(context, type);
    }

    public LiveData<List<DataDB>> getGetOldData(Context context, String type) {
        return dataRepository.getOldData(context, type);
    }

    public List<DataDB> getData(Context context) {
        return dataRepository.getData(context);
    }

    public LiveData<List<DataDB>> getGetRecentData(Context context, String type) {
        return dataRepository.getRecentData(context, type);
    }

    public LiveData<List<DataDB>> getFilteredData(Context context, String filter, String type) {
        return dataRepository.getFilterData(context, filter, type);
    }

    public void delete(Context context) {
        dataRepository.deleteAll(context);
    }

    public void insert(DataDB dataDB, Context context) {
        dataRepository.insert(dataDB, context);
    }

    public void update(DataDB dataDB, Context context) {
        dataRepository.update(dataDB, context);
    }
}
