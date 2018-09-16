import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import org.json.*;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id=0;

    @Ignore
    private String APIKey = "AIzaSyB6Wq1m5PB27emo2reEUzRuY4B0kdT-jFU"; //value does not change

    private String name;
    private String phn;
    private String address;
    private double x;
    private double y;

    public Contact(String name, String phn, String address) {
        this.name = name;
        this.phn = phn;
        this.address = address;

        try {
            double[] coords = get_coords(address);
            this.x = coords[0];
            this.y = coords[1];
        }
        catch (Exception e){
            this.x = 0;
            this.y = 0;
        }
    }

    private double[] get_coords(String address) throws IOException, JSONException {
        URL obj = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+address.replace(' ', '+')+"&key="+ this.APIKey);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        // read from the URL
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // build a JSON object
        JSONObject json = new JSONObject(response.toString());
        if (! json.getString("status").equals("OK"))
            throw new IOException("Bad Address");

        // get the first result
        JSONObject res = json.getJSONArray("results").getJSONObject(0);
        System.out.println(res.getString("formatted_address"));
        JSONObject loc =
                res.getJSONObject("geometry").getJSONObject("location");
        return new double[] {loc.getDouble("lat"), loc.getDouble("lng")};
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

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
