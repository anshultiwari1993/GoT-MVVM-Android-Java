package com.anshultiwari.androidassignment.Database;

import android.content.Context;

import com.anshultiwari.androidassignment.Model.Celebrity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Celebrity.class, version = 1)
public abstract class CelebDatabase extends RoomDatabase {

    private static CelebDatabase instance;
    public abstract CelebrityDao celebrityDao();

    public static synchronized CelebDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CelebDatabase.class, "celeb_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
