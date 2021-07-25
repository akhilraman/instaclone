package com.example.instagramclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class liked_popup extends DialogFragment {
    @Nullable
    String key;
    List following;
    liked_popup(String key){
        this.key=key;
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
         return inflater.inflate(R.layout.likedby_popup,container,false);

    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("key",key);
        ListView followinglist=view.findViewById(R.id.likedlist);
        if(key.equals(ParseUser.getCurrentUser().getUsername())){
            following=ParseUser.getCurrentUser().getList("Following");
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, following);
            followinglist.setAdapter(adapter);
        }
        else{
            ParseQuery<ParseUser> query5=ParseUser.getQuery();

            query5.whereEqualTo("username",key);
            query5.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null){
                        List following_profile=new ArrayList();
                        for(ParseUser user:objects){
                           following_profile=user.getList("Following");
                           Log.i("list",following_profile.toString());
                        }
                        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,following_profile);
                        followinglist.setAdapter(adapter);
                    }
                }
            });
        }



        followinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), following.get(position).toString(), Toast.LENGTH_SHORT).show();
                Fragment selectedFragment=new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("profile",following.get(position).toString());
                selectedFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment_container,selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
