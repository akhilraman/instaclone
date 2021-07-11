package com.example.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

public class userpost {

    String u_name;
    Bitmap image;
    int likes;
    String postid;
    public userpost(String u,Bitmap i,int l,String p){
        u_name=u;
        image=i;
        likes=l;
        postid=p;
    }
    public Bitmap getImage(){
        return image;
    }

    public int getLikes() {
        return likes;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
