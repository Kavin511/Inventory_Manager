package com.are.vehiclemanager.dp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataDBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DataDB dataDB);

    @Query("DELETE FROM DATADB")
    void deleteAll();

    @Query("SELECT * FROM DATADB WHERE type =:type ORDER BY timeStamp DESC")
    LiveData<List<DataDB>> getRecentData(String type);

    @Query("SELECT * FROM DATADB where  type =:type and (mData LIKE :filter OR part_name_ LIKE :filter OR partnum_ LIKE :filter OR time LIKE :filter OR price LIKE :filter)")
    LiveData<List<DataDB>> getFilteredData(String filter, String type);

    @Query("SELECT * FROM DATADB WHERE type =:type ORDER BY timeStamp ASC")
    LiveData<List<DataDB>> getOldData(String type);

    @Query("SELECT * FROM DATADB ORDER BY timeStamp DESC")
    LiveData<List<DataDB>> getStockData();

    @Query("SELECT * FROM datadb")
    List<DataDB> getData();

    @Query("SELECT * FROM datadb where type =:type_")
    LiveData<List<DataDB>> getPrice(String type_);
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertProfile(ProfileDB profileDB);
//    @Query("SELECT * FROM DATADB where day =str")
//    LiveData<List<ProfileDB>> getProfileData(String type);
}
