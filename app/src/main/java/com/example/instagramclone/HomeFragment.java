package com.example.instagramclone;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class HomeFragment  extends Fragment  {
    @Nullable
    ListView listview;
    ArrayList<userpost> arrayList;
    customUserPostView adapter;
    Button like;
    LinearLayout storylist;
    String storyid;
    ArrayList<String> story_ids;
    List list;
    Bitmap other_profile_pic;
    Bitmap present_profile_pic;
    int i;

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        i=0;
        return inflater.inflate(R.layout.fragment_home,container,false);
    }
//CODE TO UPLOAD A PICTURE TO THE PARSE SERVER
    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("image.png", byteArray);
                ParseObject object = new ParseObject("Story");
                object.put("story", file);
                object.put("username", ParseUser.getCurrentUser().getUsername()+"-"+i);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getView().getContext(), "Image uploaded!", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getView().getContext(), "Problem in uploading", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void create_story(List p,String comingfrom,Bitmap profile_pic){
        //ImageView image = new ImageView(getContext());
        de.hdodenhof.circleimageview.CircleImageView image=new CircleImageView(getContext());
        Log.i("size",ParseUser.getCurrentUser().getList("stories")+"");
           Log.i("other users are:",p+"");
           Log.i("is image null ?",profile_pic+"");
           image.setImageBitmap(profile_pic);
        image.setTag(p);
        storylist.addView(image,200,200);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), image.getTag().toString(), Toast.LENGTH_SHORT).show();
                Fragment selectedFragment=new storyfragment(p);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment_container,selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        story_ids=new ArrayList<>();
        storylist=(LinearLayout)view.findViewById(R.id.storylist);
        Button upload_story=view.findViewById(R.id.upload_story);
        ParseQuery<ParseUser> query4=ParseUser.getQuery();
        query4.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null &&objects.size()>0){
                    for(ParseUser user:objects){
                        if(user.getList("stories").size()>0){
                            Log.i("users with stories are",user.getUsername()+"");
                            ParseFile pro_pic=(ParseFile)user.get("profilepicture");
                            pro_pic.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null &&data!=null) {
                                         Bitmap others_profile_pic=BitmapFactory.decodeByteArray(data, 0, data.length);
                                        create_story(user.getList("stories"),"otheruser",others_profile_pic);
                                    }
                                }
                            });
                            Log.i("here is it null",other_profile_pic+"");
                            if(user.getUsername().equals(ParseUser.getCurrentUser().getUsername())){
                                i=user.getList("stories").size()+1;
                            }
                        }
                        //create_story(user.getList("stories"),"otheruser");

                        ParseUser.getCurrentUser().saveInBackground();
                    }
                }
                Log.i(ParseUser.getCurrentUser().getUsername()+"followers are",ParseUser.getCurrentUser().getList("Followers").toString());

            }
        });
        upload_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
                i++;
                Log.i("i value",i+"");
                String story_presentuser=ParseUser.getCurrentUser().getUsername()+"-"+i;
                ParseUser.getCurrentUser().add("stories",story_presentuser);
                Log.i("check list",ParseUser.getCurrentUser().getList("stories")+"");
                ParseUser.getCurrentUser().saveInBackground();
                ParseFile pro_pic=(ParseFile)ParseUser.getCurrentUser().get("profilepicture");
                pro_pic.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if(e==null &&data!=null){
                            present_profile_pic=BitmapFactory.decodeByteArray(data, 0, data.length);
                        }
                    }
                });
                ParseUser.getCurrentUser().saveInBackground();
                create_story(ParseUser.getCurrentUser().getList("stories"),"presentuser",other_profile_pic);
            }
        });
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
                                    List<String>commentby=object.getList("comments");
                                    //Log.i("likes",likedby.toString());
                                    //int likes=object.getInt("likes");
                                    //Log.i("postid",object.getObjectId());
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    arrayList.add(new userpost(object.getString("username"), bitmap,likedby,commentby,object.getObjectId()));
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