package com.example.instagramclone;


import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    ArrayAdapter<String>useradapter;
    ListView userlist;
    @Nullable

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore,container,false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userlist=view.findViewById(R.id.userlist);
        ArrayList<String>users=new ArrayList<>();
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), users.get(position), Toast.LENGTH_SHORT).show();
                 Fragment selectedFragment=new ProfileFragment(users.get(position));
                /*Bundle bundle = new Bundle();
                bundle.putString("profile",users.get(position));
                selectedFragment.setArguments(bundle);*/
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment_container,selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null &&objects.size()>0){
                    int i=0;
                    for(ParseUser user:objects){
                        Log.i("this",user.getUsername());
                        users.add(user.getUsername());
                        useradapter=new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,users);
                        userlist.setAdapter(useradapter);
                    }

                }
            }
        });

    }
}
