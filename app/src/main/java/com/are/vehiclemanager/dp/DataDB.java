package com.are.vehiclemanager.dp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DataDB")
public class DataDB {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "timeStamp")
    private final long timeStamp;
    @ColumnInfo(name = "type")
    private final String type;
    private final String price;
    @ColumnInfo(name = "mData")
    public String mData;
    String part_name_;
    String partnum_;
    String time;

    public DataDB(String mData, @NonNull long timeStamp, String type, String price, String time) {
        this.mData = mData;
        this.timeStamp = timeStamp;
        this.type = type;
        this.price = price;
        this.time = time;
    }

    public String getData() {
        return this.mData;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}
