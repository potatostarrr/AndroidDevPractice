package com.example.tengzhongwei.memlocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

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

        //initialize Data structure
        savedAddresses = new ArrayList<>();
        savedLocation = new ArrayList<>();
        savedAddresses.add("Press to Add new Location");
        savedLocation.add(new LatLng(0,0));
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedAddresses);

        //Find Component
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
