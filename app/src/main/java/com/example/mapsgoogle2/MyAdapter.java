package com.example.mapsgoogle2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter {
    ArrayList<Place> places;
    TextView name;

    public MyAdapter(@NonNull Context context, ArrayList<Place> places) {

        super(context,R.layout.item_layout);
        this.places = places;
    }

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);

        name = view.findViewById(R.id.text_item);
        name.setText(places.get(position).getName());
        return view;
    }

    @Override
    public int getCount() {
        return places.size();
    }
}
