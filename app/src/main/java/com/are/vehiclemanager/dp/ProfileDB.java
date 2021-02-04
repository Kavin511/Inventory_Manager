package com.are.vehiclemanager.dp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ProfileDB")
public class ProfileDB {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private final long timestamp;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private final byte[] image;
    private final String userData;

    public ProfileDB(byte[] image, String userData, long timestamp) {
        this.timestamp = timestamp;
        this.image = image;
        this.userData = userData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getImage() {
        return image;
    }

    public String getUserData() {
        return userData;
    }
}

