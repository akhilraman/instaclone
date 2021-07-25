package com.example.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class userpost {

    String u_name;
    Bitmap image;
    List<String> likedby;
    String postid;
    public userpost(String u,Bitmap i,List<String> l,String p){
        u_name=u;
        image=i;
        likedby=l;
        postid=p;
    }
    public Bitmap getImage(){
        return image;
    }

    public List<String> getLikes() {
        return likedby;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public void setLikes(List<String> likes) {
        this.likedby = likes;
    }

    public String getU_name() {
        return u_name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }
}
