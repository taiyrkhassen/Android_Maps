package com.example.mapsgoogle2;

import com.google.android.gms.maps.model.LatLng;
public class Place {
    String name;
    String descr;
    LatLng latlng;

    public LatLng getLatlng() {
        return latlng;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }
}
