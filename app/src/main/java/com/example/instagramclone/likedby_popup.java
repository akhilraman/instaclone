package com.example.instagramclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class likedby_popup extends DialogFragment {
    String userid;
    List likedby;
    likedby_popup(String user,List likedby){
        this.userid=user;
        this.likedby=likedby;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.likedby_popup,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView likes=view.findViewById(R.id.likedlist);
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,likedby);
        likes.setAdapter(adapter);

    }
}
