package com.example.tengzhongwei.memlocation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Geocoder geocoder;


    public void startListening(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                startListening();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // If We have the permission
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startListening();
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        //If We are not adding new address,  then retrieve previous address
        final Intent intent = getIntent();
        if(!intent.getStringExtra("address").equals("Press to Add new Location") &&
                intent.getIntExtra("index", 0)>0){
            LatLng latLng = MainActivity.savedLocation.get(intent.getIntExtra("index", 0));
            String address = MainActivity.savedAddresses.get(intent.getIntExtra("index", 0));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        } else{
            // We are adding new address, get last saved location
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastLocation!=null){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()),10));
                }
            }

        }




    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        String address = "";
        //Get Address
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
            if(addresses!=null && addresses.size()>0){
                if(addresses.get(0).getSubThoroughfare()!=null){
                    address += addresses.get(0).getSubThoroughfare()+",";
                }
                if(addresses.get(0).getThoroughfare()!=null){
                    address += addresses.get(0).getThoroughfare()+",";
                }
                if(addresses.get(0).getPostalCode()!=null){
                    address += addresses.get(0).getPostalCode()+",";
                }
                if(addresses.get(0).getCountryName()!=null){
                    address += addresses.get(0).getCountryName();
                }
            } else{
                //Using time to replace address
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
                address = sdf.format(new Date());
            }

        } catch (IOException e) {
            address = "Error Happened";
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        MainActivity.savedLocation.add(latLng);
        MainActivity.savedAddresses.add(address);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Add Address Succeessfully", Toast.LENGTH_LONG).show();
    }
}
