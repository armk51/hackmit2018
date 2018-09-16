import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.net.ssl.HttpsURLConnection;

public class DistanceHelpers {

    static List<Contact> getDistContacts(double userX, double userY, List<Contact> contacts) throws IOException {
        String APIKey = "AIzaSyB6Wq1m5PB27emo2reEUzRuY4B0kdT-jFU";

        StringBuilder request = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=");
        request.append(userX);
        request.append(",");
        request.append(userY);
        request.append("&");
        for (Contact contact : contacts) {
            request.append(contact.getX());
            request.append(",");
            request.append(contact.getY());
        }
        request.append("&key=");
        request.append(APIKey);

        URL obj = new URL(request.toString());
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

        return new List<Contact>();
    }
}
