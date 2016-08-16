package com.example.prateek.moviesappv2;

/**
 * Created by Prateek on 29-07-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReviewAdapter extends CustomAdapter {


    public ReviewAdapter(Activity context, Cursor c, int flags) {
        super(context,c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){

        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.textView = (TextView) view.findViewById(R.id.review_home_page_text);
        view.setTag(viewHolder);
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        String text;
        String authorName;
        authorName = cursor.getString(ReviewTab.COL_AUTHOR);
        if(authorName != null) {
            text = "Review By " + authorName;
        }
        else {
            text = "Review Not Available";
        }
        holder.textView.setText(text);
    }

    public class ViewHolder {
        //public ImageView icon;
        public TextView textView;
    }

}