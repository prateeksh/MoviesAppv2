package com.example.prateek.moviesappv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
       if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT))
       {
           String display = intent.getStringExtra(Intent.EXTRA_TEXT);
           String display2 = intent.getStringExtra(Intent.EXTRA_REFERRER_NAME);
           String Image = intent.getStringExtra("IMGID");
           String synp = intent.getStringExtra("SYNOPSIS");
           String rat = intent.getStringExtra("RATING");

           ImageView imgView = (ImageView) rootView.findViewById(R.id.Movie_Image);
           Picasso.with(getContext()).load(Image).into(imgView);

           TextView textView = (TextView) rootView.findViewById(R.id.Original_Title);
           textView.setText(display);

           TextView textView1 = (TextView) rootView.findViewById(R.id.release);
           textView1.setText(display2);

           TextView textView2 = (TextView) rootView.findViewById(R.id.Plot);
           textView2.setText(synp);

           TextView textView3 = (TextView) rootView.findViewById(R.id.rating);
           textView3.setText(rat);

       }
        return rootView;
    }
}
