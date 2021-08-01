package com.example.instagramclone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;



public class customUserPostView extends ArrayAdapter<userpost>  {
    int likes;
    List<String> likedby;
    public customUserPostView(Context context, ArrayList<userpost> arrayList){
        super(context,0,arrayList);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View  currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }
        TextView textView2=currentItemView.findViewById(R.id.likesView);

        // get the position of the view from the ArrayAdapter
        userpost currentNumberPosition = getItem(position);
        // then according to the position of the view assign the desired image for the same
        ImageView postimage = currentItemView.findViewById(R.id.imageView);

        assert currentNumberPosition != null;
        //postimage.setImageResource(currentNumberPosition.getImage());
        postimage.setImageBitmap(currentNumberPosition.getImage());
        //postimage.setImageResource(R.drawable.instalogo);
        TextView textView1 = currentItemView.findViewById(R.id.textView);
        textView1.setText(currentNumberPosition.getU_name());
        List<String> no_likes=currentNumberPosition.getLikes();
        ImageView comment=currentItemView.findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentpopup cp=new commentpopup(currentNumberPosition.getPostid(),currentNumberPosition.getCommentby());
                cp.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "this");
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 likedby_popup cp=new likedby_popup(currentNumberPosition.getPostid(),currentNumberPosition.getLikes());
                cp.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "this");
            }
        });


        textView2.setText(no_likes.size()+" people liked this post");
        ImageView button=currentItemView.findViewById(R.id.like);

        if(no_likes.contains(ParseUser.getCurrentUser().getUsername())){
            button.setImageResource(R.drawable.readheart);
        }
        else{
            button.setImageResource(R.drawable.heart);
        }

        button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //likes = currentNumberPosition.getLikes();
                                    likedby = currentNumberPosition.getLikes();
                                    //likes=likedby.size();
                                    //textView2.setText(likes + " liked this post");
                                    if(no_likes.contains(ParseUser.getCurrentUser().getUsername())){
                                        no_likes.remove(ParseUser.getCurrentUser().getUsername());
                                        button.setImageResource(R.drawable.heart);
                                    }
                                    else{
                                        no_likes.add(ParseUser.getCurrentUser().getUsername());
                                        button.setImageResource(R.drawable.readheart);
                                    }
                                    textView2.setText(no_likes.size() + " people liked this post");
                                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Image");
                                    query1.whereEqualTo("objectId", currentNumberPosition.getPostid());
                                    query1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e == null) {
                                                if (objects.size() == 1) {
                                                    ParseObject object = objects.get(0);
                                                        if (object.getList("Likedby").contains(ParseUser.getCurrentUser().getUsername())) {
                                                            /*object.getList("Likedby").remove(ParseUser.getCurrentUser().getUsername());
                                                            List templikes = object.getList("Likedby");*/
                                                            object.remove("Likedby");
                                                            object.put("Likedby",no_likes);
                                                            //button.setImageResource(R.drawable.heart);
                                                        } else {
                                                            object.add("Likedby", ParseUser.getCurrentUser().getUsername());
                                                           //button.setImageResource(R.drawable.readheart);
                                                        }
                                                        //textView2.setText(object.getList("Likedby").size() + " people liked this post");
                                                        object.saveInBackground();
                                                        Log.i(object.getString("username"), object.getList("Likedby").toString());
                                                }
                                            }
                                        }
                                    });

                }
            });

            return currentItemView;
    }
}



