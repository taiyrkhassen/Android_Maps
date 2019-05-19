package com.example.mapsgoogle2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.AppComponentFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

     ArrayList<Place> places;
     GoogleMap mMap;
     ListView lv;
     RelativeLayout rl1;
     RelativeLayout rl2;
     RadioGroup rg;
     MyAdapter adapter;
     DataBaseHelper db;
     EditText country;
     EditText descr;
     Animation animation;
    int i = 0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (i==0){

            rg.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            lv.startAnimation(animation);
            rl2.setVisibility(View.GONE);
            i++;
        }
        else{
            rl2.startAnimation(animation);
            rg.setVisibility(View.VISIBLE);
            rg.startAnimation(animation);


            lv.setVisibility(View.GONE);
            rl2.setVisibility(View.VISIBLE);
            i--;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        //assert mapFragment != null;
        mapFragment.getMapAsync(this);


        init();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lv.setVisibility(View.GONE);
                rl2.setVisibility(View.VISIBLE);
                rg.setVisibility(View.VISIBLE);
                i--;
                mMap.addMarker(new MarkerOptions().position(places.get(position).getLatlng()).title(places.get(position).getName())
                        .snippet(places.get(position).getDescr()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(places.get(position).getLatlng(),mMap.getMinZoomLevel()));
            }
        });



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

        for(int i =0; i<places.size(); i++){
            mMap.addMarker(new MarkerOptions().position(places.get(i).getLatlng()).
                    title(places.get(i).getName()).snippet(places.get(i).getDescr()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(places.get(i).getLatlng()));
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                @SuppressLint("ViewHolder") View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.blank,null);
                Button bty = view1.findViewById(R.id.yes);
                Button btn = view1.findViewById(R.id.no);
                final EditText name1 = view1.findViewById(R.id.country);
                 final EditText name2 = view1.findViewById(R.id.description);
                mBuilder.setView(view1);
                 final AlertDialog dialog = mBuilder.create();
                dialog.show();

                final Place pl = new Place();
                bty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pl.name = name1.getText().toString();
                        pl.descr = name2.getText().toString();
                        pl.latlng = latLng;
                        places.add(pl);

                        mMap.addMarker(new MarkerOptions().position(latLng).title(pl.getName()).snippet(pl.getDescr()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        adapter.notifyDataSetChanged();

                        System.out.println(places.size());
                        System.out.println(adapter.getCount());

                        //DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                        //db.drop();
                        db.insertNote(pl);
                        dialog.cancel();

                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    String city = addresses.get(0).getLocality();
                    Log.d("City", city);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void radioClicked(View v){
        switch (v.getId()){
            case R.id.standard:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
            case R.id.satellite:

                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }

    }
    public void init(){
        db = new DataBaseHelper(this);
        country = findViewById(R.id.country);
        descr = findViewById(R.id.description);
        lv = findViewById(R.id.lv);
        rl1 = findViewById(R.id.first);
        rl2 = findViewById(R.id.second);
        rg = findViewById(R.id.rg);

        places = new ArrayList<>();
        places = db.getNotes();

        animation = AnimationUtils.loadAnimation(this, R.xml.fade);

        adapter = new MyAdapter(this, places);
        lv.setAdapter(adapter);

        lv.setVisibility(View.GONE);
    }
}
