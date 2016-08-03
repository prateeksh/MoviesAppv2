package com.example.prateek.moviesappv2;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.prateek.moviesappv2.data.MovieContract;
import com.squareup.picasso.Picasso;


/**
 * Created by Prateek on 21-04-2016.
 */
public class CustomAdapter extends CursorAdapter {

    private static int sLoaderID;
    private Context mContext;

    public CustomAdapter(Context context, Cursor c, int flags , int loaderID)
    {
        super(context, c, flags);
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
     int layoutId = R.layout.movies_main;
        //MovieMain movieMain = getItem(position);
       View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
//        if(convertView == null){
//          convertView = LayoutInflater.from(getContext()).inflate(R.layout.movies_main, parent, false);
//      }
//        ImageView imgView = (ImageView) convertView.findViewById(R.id.image_view);
//
//       Picasso.with(getContext()).load(movieMain.img_url).into(imgView);
//    return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){

        ViewHolder viewHolder = (ViewHolder) view.getTag();

//        int viresionIndex = cursor.getColumnIndex(MovieContract.MovieEntry.TABLE_NAME);
//        final String versionName = cursor.getString(viresionIndex);
//        viewHolder.
        int imgIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMG);
        final String image = cursor.getString(imgIndex);
        Picasso.with(mContext).load(image).into(viewHolder.imgView);
    }

    public static class ViewHolder{
        public final ImageView imgView;

        public ViewHolder(View view){
            imgView = (ImageView) view.findViewById(R.id.image_view);
        }
    }
}