package com.goqing.timetracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface TimeRecordDao {
    @Query("SELECT * FROM timerecord")
    List<TimeRecord> getAll();

    @Query("SELECT * FROM timerecord WHERE timestamp >= :start_time and timestamp <= :end_time")
    List<TimeRecord> loadBetween(long start_time, long end_time);

    @Insert
    void insert(TimeRecord timeRecord);

    @Delete
    void delete(TimeRecord timeRecord);
}

