package com.example.instagramclone;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment  extends Fragment  {
    @Nullable
    ListView listview;
    ArrayList<userpost> arrayList;
    customUserPostView adapter;
    Button like;

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home,container,false);
    }
    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview=view.findViewById(R.id.listView);
        arrayList = new ArrayList<userpost>();
        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Image");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    for (ParseObject object : objects) {
                        ParseFile file = (ParseFile) object.get("image");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    //Log.i("user", object.getString("username"));
                                    List<String> likedby=object.getList("Likedby");
                                    //Log.i("likes",likedby.toString());
                                    //int likes=object.getInt("likes");
                                    //Log.i("postid",object.getObjectId());
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    arrayList.add(new userpost(object.getString("username"), bitmap,likedby,object.getObjectId()));
                                    adapter = new customUserPostView(view.getContext(), arrayList);
                                    listview.setAdapter(adapter);
                                }

                            }
                        });
                    }

                    //Log.i("list",arrayList.toString());

                }
            }
        });


    }

}