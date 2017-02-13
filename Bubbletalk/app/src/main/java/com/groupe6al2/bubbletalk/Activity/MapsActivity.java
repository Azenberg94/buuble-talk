package com.groupe6al2.bubbletalk.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.groupe6al2.bubbletalk.Class.Bubble;
import com.groupe6al2.bubbletalk.Class.BubbleTalkSQLite;
import com.groupe6al2.bubbletalk.R;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    MapFragment mMapFragment;
    FragmentTransaction fragmentTransaction;
    BubbleTalkSQLite bubbleTalkSQLite;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        userid = getIntent().getStringExtra("id");

        bubbleTalkSQLite = new BubbleTalkSQLite(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       /* mMapFragment = MapFragment.newInstance();
        fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
*/

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        Location location = new Location("");
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f));
        ArrayList<Bubble> bubbles = bubbleTalkSQLite.getAllActiveBubbles();
        addBubbles(bubbles);
    }
    public void addBubbles(ArrayList<Bubble> bubbles){
        int strokeColor;
        LatLng bubbleLocation;
        for (Bubble bubble : bubbles)
        {
            bubbleLocation = new LatLng(Double.parseDouble(bubble.getLatitude()), Double.parseDouble(bubble.getLongitude()));
            if(bubble.getProprio()==userid){
                strokeColor = Color.BLUE;
            }else strokeColor = Color.GRAY;
            mMap.addCircle(new CircleOptions()
                    .center(bubbleLocation)
                    .radius(50)
                    .strokeColor(strokeColor)
                    .fillColor(Color.TRANSPARENT)
                    );
        }
    }


}
