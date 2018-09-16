package com.example.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    static AppDatabase db;

    public MainActivity() {

    }

    public AppDatabase getDb() {
        return this.db;
    }

    private static class LoadContactsTask extends AsyncTask<Void, Void, List<Contact>> {
        @Override
        protected List<Contact> doInBackground(Void... params) {
            ContactDao contactDao = db.contactDao();
            return contactDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    private static class SaveContactTask extends AsyncTask<Contact, Void, Contact> {
        @Override
        protected Contact doInBackground(Contact... params) {
            Contact contact = params[0];
            ContactDao contactDao = db.contactDao();
            if (contactDao.getByEverything(contact.getName(), contact.getPhn(), contact.getAddress()).size() == 0) {
                contactDao.insertAll(contact);
            }
            contactDao.updateContacts(contact);
            return params[0];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Contact c1 = new Contact("John Doe", "230-345-3456", "5912 Chatsworth Lane, Bethesda, MD");
        Contact c2 = new Contact("Jane Doe", "808-546-3567", "Ashford, CT");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(c1); contacts.add(c2);

        try {
            DistanceHelpers.updateDistContacts(42, -71, contacts);
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            DistanceHelpers.getNClosestContacts(contacts, 1);
        } catch (Exception e) {
            System.out.println(e);
        }

        this.db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-database").build();

        new SaveContactTask().execute(c1);
        new SaveContactTask().execute(c2);

        System.out.println(new LoadContactsTask().execute());

        String locationProvider = LocationManager.GPS_PROVIDER;

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        double x = 0;
        double y = 0;

        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
        try {
            locationManager.requestLocationUpdates(locationProvider, 0, 0, new MyLocationListener());

            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            x=lastKnownLocation.getLatitude();
            y=lastKnownLocation.getLongitude();
        } catch (SecurityException e) {
            System.out.println(e);
        }

        String alertDescription = "";
        try {
            URL url = new URL("https://api.weather.com/v3/alerts/headlines?geocode=" + x + "%2C" + y + "&format=json&language=en-US&apiKey=da328055e2e940d8b28055e2e9e0d851");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 204) {
                alertDescription = "No Alerts";
            } else {
                // read from the URL
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                alertDescription = json.getJSONArray("alerts").getJSONObject(0).getString("headlineText");
            }
        }
        catch (Exception e){
            alertDescription = e.toString();
        }

        LayoutInflater factory = getLayoutInflater();
        View v = factory.inflate(R.layout.home_fragment, null);
        TextView alert_message = (TextView) v.findViewById(R.id.alert_card_message);
        alert_message.setText(alertDescription);

        System.out.println(alertDescription);

        bottomNavigation = findViewById(R.id.navigationView);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = HomeFragment.newInstance();
                        item.setChecked(true);
                        break;
                    case R.id.navigation_alerts:
                        selectedFragment = AlertsFragment.newInstance();
                        item.setChecked(true);
                        break;
                    case R.id.navigation_contacts:
                        selectedFragment = ContactsFragment.newInstance();
                        item.setChecked(true);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();

                return false;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();
    }

    public void openMap(android.view.View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MapFragment.newInstance());
        transaction.commit();
    }
}
