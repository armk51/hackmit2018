package com.example.test;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    double lat;
    double lon;
    private GoogleMap map;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, null, false);
        setLat(42.36);
        setLon(-71.10);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        this.map.addMarker(new MarkerOptions()
                .position(new LatLng(this.lat, this.lon))
                .title("Current Location"));
        this.map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(this.lat, this.lon)));
        this.map.moveCamera(CameraUpdateFactory.zoomTo(10));
    }

    public void setLat(double la) {
        this.lat = la;
    }

    public void setLon(double lo) {
        this.lon = lo;
    }
}
