package com.example.tengzhongwei.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView lonTextView;
    TextView latTextView;
    TextView attTextView;
    TextView addressTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startListening();
    }

    //Start Listening
    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //initialize location Service
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateLocation(location);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
        }
    }

    //Update Location
    private void updateLocation(Location location){
        lonTextView.setText("Longtitute:"+String.valueOf(location.getLongitude()));
        latTextView.setText("Latititute:"+String.valueOf(location.getLatitude()));
        attTextView.setText("Altitude:"+String.valueOf(location.getAltitude()));
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String addressMessage = "Address: \n";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            if(addresses!=null && addresses.size()>0){
                Address address = addresses.get(0);
                addressMessage += address.getSubThoroughfare() +"\n";
                addressMessage += address.getThoroughfare() +"\n";
                addressMessage += address.getPostalCode() +"\n";
                addressMessage += address.getCountryName() +"\n";
            } else{
                addressMessage += "Can't find Address Information";
            }
        } catch (IOException e) {
            addressMessage += "Error!";
        }
        addressTextView.setText(addressMessage);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign View
        lonTextView = findViewById(R.id.lonTextView);
        latTextView = findViewById(R.id.latTextView);
        attTextView = findViewById(R.id.attTextView);
        addressTextView = findViewById(R.id.addressTextView);



        //Check Permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        } else {
            startListening();
        }

    }
}
