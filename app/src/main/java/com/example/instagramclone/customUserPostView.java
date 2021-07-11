package com.example.instagramclone;

import android.content.Context;
import android.content.Intent;
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
    public customUserPostView(Context context, ArrayList<userpost> arrayList){
        super(context,0,arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }
        TextView textView2=currentItemView.findViewById(R.id.likesView);
        // get the position of the view from the ArrayAdapter
        userpost currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        ImageView postimage = currentItemView.findViewById(R.id.imageView);
        /*postimage.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));*/
        assert currentNumberPosition != null;
        //postimage.setImageResource(currentNumberPosition.getImage());
        postimage.setImageBitmap(currentNumberPosition.getImage());
        //postimage.setImageResource(R.drawable.instalogo);
        TextView textView1 = currentItemView.findViewById(R.id.textView);
        textView1.setText(currentNumberPosition.getU_name());
        textView2.setText(currentNumberPosition.getLikes()+" people liked this post");
        Button button=currentItemView.findViewById(R.id.like);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), currentNumberPosition.getPostid(), Toast.LENGTH_SHORT).show();
                likes = currentNumberPosition.getLikes();
                /*if(button.getTag().equals("liked")){
                    //already liked!
                    likes=likes-1;
                    button.setTag("unlike");
                }else{*/
                likes = likes + 1;
                //button.setTag("liked");

                textView2.setText(likes + " liked this post");

                ParseQuery<ParseObject> query1=ParseQuery.getQuery("Image");
                query1.whereEqualTo("objectId",currentNumberPosition.getPostid());
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            if(objects.size()>0){
                                for(ParseObject object:objects){
                                    String value=object.getString("likedby");
                                    Log.i("this",value);
                                    String newvalue=" ";
                                    if(object.getString("likedby").isEmpty()){
                                         newvalue=ParseUser.getCurrentUser().getUsername();
                                    }
                                    else{
                                         newvalue=","+ ParseUser.getCurrentUser().getUsername();
                                         Log.i("thi",newvalue);
                                    }
                                    value=value+newvalue;
                                    String[] arr=value.split(",");
                                    likes=arr.length;
                                    Toast.makeText(getContext(),value, Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    }
                });

                /*ParseQuery<ParseObject> query=ParseQuery.getQuery("Image");
                query.whereEqualTo("objectId",currentNumberPosition.getPostid());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            if(objects.size()>0){
                                for(ParseObject object:objects){
                                    object.put("likes",likes);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                            //textView2.setText(object.getInt("likes")+" people liked this post");

                                        }
                                    });
                                }
                            }
                        }
                    }

                });*/

            }
        });

        return currentItemView;
            }
        }


