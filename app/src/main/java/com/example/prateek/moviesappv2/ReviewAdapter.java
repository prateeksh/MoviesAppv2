package com.example.prateek.moviesappv2;

/**
 * Created by Prateek on 29-07-2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<MovieReview> {
    /**
     * This is my own custom constructor
     * The context (current context) is used to inflate the layout file, and the list (MovieReviews)
     * is the list data to populate into the listview
     */

    public ReviewAdapter(Activity context, List<MovieReview> result) {
        super(context,0,result);
    }

    /**
     * Provides a view for an AdapterView (ListView in this case)
     * position: The AdapterView position that is requesting a view
     * convertView: The recycled view to populate.
     * parent: The parent ViewGroup that is used for inflation
     * return: The View for the position in the AdapterView
     */

    @Override

    public View getView(int position, View convertView, ViewGroup parent){

        MovieReview result = getItem(position);

        //2. Adapters recycle views to AdapterViews. If this is a new View Object, then inflate the layout.
        //If not, this view already has the layout inflate from previous call to getView, we just modify the view widgets.

        if (convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item,parent,false);

        }

        TextView AuthorName = (TextView)convertView.findViewById(R.id.review_item_author);
        AuthorName.setText(result.author);

        TextView ContentTxt = (TextView)convertView.findViewById(R.id.review_item_content);
        ContentTxt.setText(result.content);

        return convertView;

    }

}