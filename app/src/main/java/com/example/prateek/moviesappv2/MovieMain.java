package com.example.prateek.moviesappv2;

/**
 * Created by Prateek on 09-05-2016.
 */
public class MovieMain {
    String img_url;
    String O_title;
    String synopsis;
    String movie_release;
    String rating;

  public MovieMain(String img, String title, String syn, String mrelease, String rat){
      this.img_url = img;
      this.O_title = title;
      this.synopsis = syn;
      this.movie_release = mrelease;
      this.rating = rat;
  }
}
