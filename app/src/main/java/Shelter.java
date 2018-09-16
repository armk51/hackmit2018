import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Shelter implements Comparable {
    @PrimaryKey(autoGenerate = true)
    private int id=0;

    private String name;
    private double x;
    private double y;

    @Ignore
    private int timeToUser;
    @Ignore
    private int distToUser;

    public Shelter(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Shelter) obj).getTimeToUser() == (this.getTimeToUser());
    }

    @Override
    public int compareTo(Object o) {
        Shelter s = (Shelter) o;
        return (int) (this.getTimeToUser() - s.getTimeToUser());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getTimeToUser() {
        return timeToUser;
    }

    public void setTimeToUser(int timeToUser) {
        this.timeToUser = timeToUser;
    }

    public int getDistToUser() {
        return distToUser;
    }

    public void setDistToUser(int distToUser) {
        this.distToUser = distToUser;
    }
}
