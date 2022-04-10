// Author Euan Millar, S1820947
package com.example.millar_euan_s1820947;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;





public class detailActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private String[] incident_points;
    private incidentData incident_selected;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        incident_selected = getIntent().getParcelableExtra("selected_incident");
        TextView incident_title = (TextView) findViewById(R.id.incident);
        incident_title.setText(incident_selected.getTitle());
        TextView incident_published = (TextView) findViewById(R.id.published);
        incident_published.setText(incident_selected.getPubDate());
        incident_points = incident_selected.getPoint().split(" ");
        String[] incident_description = incident_selected.getDescription().split("<br /");
        String start_date = incident_description[0].split(": ")[1];
        String end_date = incident_description[1].split(": ")[1];
        String delay = "";
        try {
             delay = incident_description[2].split(": ")[1];
        }
        catch (Exception e){
             delay = "No delay data available.";
        }
        TextView start = (TextView) findViewById(R.id.start);
        TextView end = (TextView) findViewById(R.id.end);
        TextView delaytime = (TextView) findViewById(R.id.delay);
        start.setText(start_date);
        end.setText(end_date);
        delaytime.setText(delay);

        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(detailActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                }
        );

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        googleMap.setMinZoomPreference(15);
        LatLng incident = new LatLng(Double.parseDouble(incident_points[0]), Double.parseDouble(incident_points[1]));
        mMap.addMarker(new MarkerOptions()
                .position(incident)
                .title(incident_selected.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(incident));
    }
}
