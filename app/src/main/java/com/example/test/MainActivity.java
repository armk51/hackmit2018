package com.example.test;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

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
