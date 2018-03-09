package com.example.tengzhongwei.memlocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> savedAddresses;
    static ArrayList<LatLng> savedLocation;
    static ArrayAdapter<String> arrayAdapter;
    ListView addressListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<String> lat = new ArrayList<>();
        ArrayList<String> lng = new ArrayList<>();


//        Retrieve Saved Data
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.tengzhongwei.memlocation", Context.MODE_PRIVATE);
        try {
            savedAddresses = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("address", ObjectSerializer.serialize(new ArrayList<>())));
            lat = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lat", ObjectSerializer.serialize(new ArrayList<>())));
            lng = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lng", ObjectSerializer.serialize(new ArrayList<>())));

        } catch (IOException e) {
            Log.i("ERROR",e.getMessage());
        }


        savedLocation = new ArrayList<>();
        if(savedAddresses.size()>0 && lat.size()==savedAddresses.size() && lng.size()==lat.size()){
            for(int i=0; i<lat.size();i++){
                savedLocation.add(new LatLng(Double.valueOf(lat.get(i)), Double.valueOf(lng.get(i))));
            }
        } else {
            savedAddresses = new ArrayList<>();
            savedLocation = new ArrayList<>();
            savedAddresses.add("Press to Add new Location");
            savedLocation.add(new LatLng(0,0));
        }

//        savedAddresses = new ArrayList<>();
//        savedLocation = new ArrayList<>();
//        savedAddresses.add("Press to Add new Location");
//        savedLocation.add(new LatLng(0,0));

        //Find Component
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedAddresses);
        addressListView = findViewById(R.id.addressListView);
        addressListView.setAdapter(arrayAdapter);
        addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("index", i);
                intent.putExtra("address", savedAddresses.get(i));
                startActivity(intent);
            }
        });

    }
}
