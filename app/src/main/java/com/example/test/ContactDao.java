package com.example.test;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT COUNT(id) FROM contact")
    int count_contacts();

    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM contact WHERE address LIKE :address")
    List<Contact> getByAddress(String address);

    @Query("SELECT * FROM contact WHERE name LIKE :name")
    List<Contact> getByName(String name);

    @Query("SELECT * FROM contact WHERE phn LIKE :phn")
    List<Contact> getByPhn(String phn);

    @Query("SELECT * FROM contact WHERE x LIKE :x AND y LIKE :y")
    List<Contact> getByCoords(Double x, Double y);

    @Insert
    void insertAll(Contact... contact);

    @Delete
    void delete(Contact contact);
}
