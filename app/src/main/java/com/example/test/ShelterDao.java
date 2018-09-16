package com.example.test;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface ShelterDao {
    @Query("SELECT COUNT(id) FROM shelter")
    int count_shelters();

    @Query("SELECT * FROM shelter")
    List<Shelter> getAll();

    @Update
    public void updateShelters(Shelter... shelters);

    @Query("SELECT * FROM contact WHERE name LIKE :name AND phn LIKE :phn AND address LIKE :address")
    List<Contact> getByEverything(String name, String phn, String address);

    @Query("SELECT * FROM shelter WHERE name LIKE :name")
    List<Shelter> getByName(String name);

    @Query("SELECT * FROM shelter WHERE x LIKE :x AND y LIKE :y")
    List<Shelter> getByCoords(Double x, Double y);

    @Insert
    void insertAll(Shelter... shelter);

    @Delete
    void delete(Shelter shelter);
}
