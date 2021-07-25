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
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment  extends Fragment {
    ListView listview;
    customUserPostView adapter;
    ArrayList<userpost> arrayList;
    TextView userid;
    String profile;
    Button follow_edit ;
    TextView following_count,follower_count;
    @Nullable

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        profile = bundle.getString("profile", "ok");
        return inflater.inflate(R.layout.fragment_profile,container,false);


        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview=view.findViewById(R.id.listview2);
        TextView profile_bio=view.findViewById(R.id.profile_bio);
        follow_edit=view.findViewById(R.id.followedit);
        userid=view.findViewById(R.id.textView3);
        following_count=view.findViewById(R.id.followingcount);
        follower_count=view.findViewById(R.id.followercount);
        userid.setText(profile);
        ImageView profilepic=view.findViewById(R.id.profilepic);
        arrayList = new ArrayList<userpost>();

        //to show dialogue box of following
        following_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liked_popup lp=new liked_popup(profile);
                lp.show(requireActivity().getSupportFragmentManager(), "this");
                Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
            }
        });



        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Image");
//update profile picture
        if(profile.equals(ParseUser.getCurrentUser().getUsername())){
            profile_bio.setText(ParseUser.getCurrentUser().getString("bio"));
            int following=ParseUser.getCurrentUser().getList("Following").size();
            following_count.setText(following+"");
            //update dp
            if(ParseUser.getCurrentUser().get("profilepicture")==null){
                profilepic.setImageResource(R.drawable.instalogo);
            }
            else {
                //to get bio information
                ParseFile file = (ParseFile) ParseUser.getCurrentUser().get("profilepicture");
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null && data != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            profilepic.setImageBitmap(bitmap);
                        }
                    }
                });
            }
             /*ParseQuery<ParseUser> query4=ParseUser.getQuery();
             query4.findInBackground(new FindCallback<ParseUser>() {
                 @Override
                 public void done(List<ParseUser> objects, ParseException e) {
                     if(e==null &&objects.size()>0){
                         //ParseUser.getCurrentUser().getList("Followers").clear();
                         for(ParseUser user:objects){
                             if(user.getList("Following").contains(ParseUser.getCurrentUser().getUsername())){
                                 //ParseUser.getCurrentUser().add("Followers",user.getUsername());
                             }
                         }
                     }
                     Log.i(ParseUser.getCurrentUser().getUsername()+"followers are",ParseUser.getCurrentUser().getList("Followers").toString());
                     ParseUser.getCurrentUser().saveInBackground();
                 }
             });*/
            //int followers=ParseUser.getCurrentUser().getList("Followers").size();
           //follower_count.setText(followers+"");
            follow_edit.setText("Edit Profile");
            follow_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.Fragment_container,new edit_fragment());
                    transaction.addToBackStack(null);
                    transaction.commit();*/
                    Fragment selectedFragment=new edit_fragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.Fragment_container,selectedFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
        else {
//other users profile
            ParseQuery<ParseUser> query5=ParseUser.getQuery();
            query5.whereEqualTo("username",profile);
            query5.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null){
                        for(ParseUser user:objects){
                            int following=user.getList("Following").size();
                            following_count.setText(following+"");
                            if(user.get("profilepicture")==null){
                                profilepic.setImageResource(R.drawable.like);
                            }
                            else {
                                profile_bio.setText(user.getString("bio"));
                                ParseFile file = (ParseFile) user.get("profilepicture");
                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            profilepic.setImageBitmap(bitmap);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
            ParseQuery<ParseUser> query2 = ParseUser.getQuery();
            query2.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query2.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        for (ParseUser user : objects) {
                            Log.i("the user is", user.getUsername());
                            List following = user.getList("Following");
                            Log.i("following are",user.getList("Following").toString());
                            if (following.contains(profile)) {
                                follow_edit.setText("Unfollow");
                            } else {
                                follow_edit.setText("Follow");
                            }
                        }
                    } else {
                        follow_edit.setText("Follow");
                    }
                }
            });

            follow_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(follow_edit.getText().equals("Follow")){
                       ParseUser.getCurrentUser().add("Following",profile);
                       follow_edit.setText("Unfollow");
                       //ADDING TO THE FOLLOWERS LIST FOR PROFILE ACCOUNT
                        /*ParseQuery<ParseObject> query3=ParseQuery.getQuery("User");
                        query3.whereEqualTo("username",profile);
                        query3.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null &&objects.size()>0){
                                    int i=0;
                                    for(ParseObject user:objects){
                                        user.add("Followers",ParseUser.getCurrentUser().getUsername());
                                        Log.i(user.getString("username")+"Followers: ",user.getList("Followers").toString());
                                        user.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Toast.makeText(getContext(), "done!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }
                            }
                        });*/

                    }
                    else{
                        ParseUser.getCurrentUser().getList("Following").remove(profile);
                        List tempfollow=ParseUser.getCurrentUser().getList("Following");
                        ParseUser.getCurrentUser().remove("Following");
                        ParseUser.getCurrentUser().put("Following",tempfollow);
                        follow_edit.setText("Follow");
                        //TO UPDATE FOLLOWERS LIST FOR PROFILE ACCOUNT
                       /* ParseQuery<ParseObject> query4=ParseQuery.getQuery("User");

                        query4.whereEqualTo("username",profile);
                        query4.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null &&objects.size()>0){
                                    int i=0;
                                    for(ParseObject user:objects){
                                       user.getList("Followers").remove(ParseUser.getCurrentUser().getUsername());

                                       List tempfollower=user.getList("Followers");
                                       user.remove("Followers");
                                       user.put("Followers",tempfollower);
                                        Log.i(user.getString("username")+"Followers:",user.getList("Followers").toString());
                                        user.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Toast.makeText(getContext(), "done!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });*/
                    }
                    Log.i(ParseUser.getCurrentUser().getUsername(),ParseUser.getCurrentUser().getList("Following").toString());
                    ParseUser.getCurrentUser().saveInBackground();
                }
            });
        }
        query.whereEqualTo("username", profile);
        query.orderByAscending("createdAt");
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
                                    Log.i("user", object.getString("username"));
                                    List<String> likedby=object.getList("Likedby");
                                    Log.i("likes",likedby.toString());
                                    //int likes=object.getInt("likes");
                                    Log.i("postid",object.getObjectId());
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    arrayList.add(new userpost(object.getString("username"), bitmap,likedby,object.getObjectId()));
                                    adapter = new customUserPostView(view.getContext(), arrayList); ;
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
