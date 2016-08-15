package com.example.prateek.moviesappv2;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by Prateek on 21-04-2016.
 */
public class CustomAdapter extends CursorAdapter {

    private final String baseUrl = "http://image.tmdb.org/t/p/w185/";

    public CustomAdapter(Context context, Cursor c, int flags )
    {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
     int layoutId = R.layout.movies_main;
       View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imgView = (ImageView) view.findViewById(R.id.image_view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String poster_path = cursor.getString(MainActivityFragment.COL_MOVIE_POSTER);
        int width = MainActivityFragment.getWidth();
        int height = MainActivityFragment.getHeight();
        width = (MainActivity.getPaneMode())? width : (width/2);
        height = (context.getResources().getConfiguration().orientation == 1) ? (height/2) : height;
        Picasso.with(context).load(baseUrl + poster_path).resize(width,height).into(viewHolder.imgView);
    }

    public static class ViewHolder{
        public ImageView imgView;

//        public ViewHolder(View view){
//            imgView = (ImageView) view.findViewById(R.id.image_view);
//        }
    }
}