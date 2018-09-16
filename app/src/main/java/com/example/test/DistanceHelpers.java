package com.example.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DistanceHelpers {

    static void updateDistContacts(double userX, double userY, List<Contact> contacts) throws IOException, JSONException {
        String APIKey = "AIzaSyB6Wq1m5PB27emo2reEUzRuY4B0kdT-jFU";

        StringBuilder request = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=");
        request.append(userX);
        request.append(",");
        request.append(userY);
        request.append("&destinations=");
        for (int i=0;i<contacts.size()-1;i++) {
            Contact contact = contacts.get(i);
            request.append(contact.getX());
            request.append(",");
            request.append(contact.getY());
            request.append("|");
        }
        request.append(contacts.get(contacts.size()-1).getX());
        request.append(",");
        request.append(contacts.get(contacts.size()-1).getY());
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

        // get the first result
        JSONObject res = json.getJSONArray("rows").getJSONObject(0);
        JSONArray dists = res.getJSONArray("elements");
        for (int i=0;i<contacts.size();i++) {
            Contact contact = contacts.get(i);
            JSONObject info = dists.getJSONObject(i);
            contact.setTimeToUser(info.getJSONObject("duration").getInt("value"));
            contact.setDistToUser(info.getJSONObject("distance").getInt("value"));
        }
    }

    static void updateDistShelters(double userX, double userY, List<Shelter> shelters) throws IOException, JSONException {
        String APIKey = "AIzaSyB6Wq1m5PB27emo2reEUzRuY4B0kdT-jFU";

        StringBuilder request = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=");
        request.append(userX);
        request.append(",");
        request.append(userY);
        request.append("&destinations=");
        for (int i=0;i<shelters.size()-1;i++) {
            Shelter shelter = shelters.get(i);
            request.append(shelter.getX());
            request.append(",");
            request.append(shelter.getY());
            request.append("|");
        }
        request.append(shelters.get(shelters.size()-1).getX());
        request.append(",");
        request.append(shelters.get(shelters.size()-1).getY());
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

        // get the first result
        JSONObject res = json.getJSONArray("rows").getJSONObject(0);
        JSONArray dists = res.getJSONArray("elements");
        for (int i=0;i<shelters.size();i++) {
            Shelter shelter = shelters.get(i);
            JSONObject info = dists.getJSONObject(i);
            shelter.setTimeToUser(info.getJSONObject("duration").getInt("value"));
            shelter.setDistToUser(info.getJSONObject("distance").getInt("value"));
        }
    }

    static List<Contact> getNClosestContacts(List<Contact> contacts, int num) throws IOException, JSONException {
        String APIKey = "da328055e2e940d8b28055e2e9e0d851";

        Collections.sort(contacts);
        int i = 0;
        List<Contact> results = new ArrayList<Contact>();
        while (contacts.size() > i && results.size() < num) {
            Contact contact = contacts.get(i);
            // Check if in disaster area.
            URL url = new URL("https://api.weather.com/v3/alerts/headlines?geocode="+contact.getX()+"%2C"+contact.getY()+"&format=json&language=en-US&apiKey="+APIKey);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 204) {
                results.add(contact);
                i++;
                continue;
            }

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

            // get the first result
            boolean add = true;
            JSONArray alerts = json.getJSONArray("alerts");
            for (int j=0; j<alerts.length();j++) {
                int severity = alerts.getJSONObject(j).getInt("severityCode");
                if (severity < 3) {
                    add = false;
                    break;
                }
            }
            if (add) {
                results.add(contact);
            }
            i++;
        }

        return results;
    }

    static List<Shelter> getNClosestShelters(List<Shelter> shelters, int num) throws IOException, JSONException {
        String APIKey = "da328055e2e940d8b28055e2e9e0d851";

        Collections.sort(shelters);
        int i = 0;
        List<Shelter> results = new ArrayList<Shelter>();
        while (shelters.size() > i && results.size() < num) {
            Shelter shelter = shelters.get(i);
            // Check if in disaster area.
            URL url = new URL("https://api.weather.com/v3/alerts/headlines?geocode="+shelter.getX()+"%2C"+shelter.getY()+"&format=json&language=en-US&apiKey="+APIKey);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 204) {
                results.add(shelter);
                i++;
                continue;
            }

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

            // get the first result
            boolean add = true;
            JSONArray alerts = json.getJSONArray("alerts");
            for (int j=0; j<alerts.length();j++) {
                int severity = alerts.getJSONObject(j).getInt("severityCode");
                if (severity < 3) {
                    add = false;
                    break;
                }
            }
            if (add) {
                results.add(shelter);
            }
            i++;
        }

        return results;
    }
}
