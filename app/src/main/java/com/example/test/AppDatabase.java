package com.example.test;

import android.arch.persistence.room.*;

@Database(entities = {Contact.class, Shelter.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract ShelterDao shelterDao();
}