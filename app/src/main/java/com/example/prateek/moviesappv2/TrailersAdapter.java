package com.example.prateek.moviesappv2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prateek on 28-07-2016.
 */
public class TrailersAdapter extends ArrayAdapter<MovieTrailer> {

    public TrailersAdapter(Activity context, List<MovieTrailer> result){
        super(context, 0 , result);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        MovieTrailer result = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item,parent,false);
            TextView trailerName = (TextView)convertView.findViewById(R.id.trailer_item_textTitle);
            trailerName.setText(result.name);
        }
        return convertView;
    }
}
