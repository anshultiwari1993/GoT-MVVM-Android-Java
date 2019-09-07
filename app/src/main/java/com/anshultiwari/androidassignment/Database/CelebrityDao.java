package com.anshultiwari.androidassignment.Database;

import com.anshultiwari.androidassignment.Model.Celebrity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CelebrityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCelebs(List<Celebrity> celebs);

    @Query("SELECT * FROM Celebrity")
    LiveData<List<Celebrity>> getAllCelebs();
}
