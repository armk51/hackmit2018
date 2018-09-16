import android.arch.persistence.room.*;

import java.util.List;


public interface ShelterDao {
    @Query("SELECT COUNT(id) FROM shelter")
    int count_shelters();

    @Query("SELECT * FROM shelter")
    List<Contact> getAll();

    @Query("SELECT * FROM shelter WHERE name LIKE :name")
    List<Contact> getByName(String name);

    @Query("SELECT * FROM shelter WHERE x LIKE :x AND y LIKE :y")
    List<Contact> getByCoords(Double x, Double y);

    @Insert
    void insertAll(Contact... contact);

    @Delete
    void delete(Contact contact);
}
