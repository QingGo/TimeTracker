package com.goqing.timetracker;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(indices = {@Index(value = {"timestamp"})})
public class TimeRecord {

    public TimeRecord(long timestamp, long time_spend, String desc) {
        this.timestamp = timestamp;
        this.time_spend = time_spend;
        this.desc = desc;
    }

    @PrimaryKey(autoGenerate = true) public int id;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "time_spend")
    public long time_spend;

    @ColumnInfo(name = "desc")
    public String desc;
}
