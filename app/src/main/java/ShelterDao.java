import android.arch.persistence.room.*;

import java.util.List;


public interface ShelterDao {
    @Query("SELECT COUNT(id) FROM shelter")
    int count_shelters();

    @Query("SELECT * FROM shelter")
    List<Shelter> getAll();

    @Query("SELECT * FROM shelter WHERE name LIKE :name")
    List<Shelter> getByName(String name);

    @Query("SELECT * FROM shelter WHERE x LIKE :x AND y LIKE :y")
    List<Shelter> getByCoords(Double x, Double y);

    @Insert
    void insertAll(Shelter... shelter);

    @Delete
    void delete(Shelter shelter);
}
