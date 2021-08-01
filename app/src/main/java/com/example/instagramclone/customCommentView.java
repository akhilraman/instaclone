package com.example.instagramclone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class customCommentView extends ArrayAdapter<comment> {
    int likes;
    List<String> likedby;
    public customCommentView(Context context, ArrayList<comment> arrayList){
        super(context,0,arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View  currentItemView = convertView;
        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.comment_layout, parent, false);
        }
        TextView textView2=currentItemView.findViewById(R.id.likesView);
        // get the position of the view from the ArrayAdapter
        comment currentNumberPosition = getItem(position);
        // then according to the position of the view assign the desired image for the same
        //ImageView postimage = currentItemView.findViewById(R.id.imageView);
        assert currentNumberPosition != null;
        //postimage.setImageResource(currentNumberPosition.getImage());
       // postimage.setImageBitmap(currentNumberPosition.getImage());
        //postimage.setImageResource(R.drawable.instalogo);
        TextView username=currentItemView.findViewById(R.id.username);
        TextView commeny_byuser=currentItemView.findViewById(R.id.comment_byuser);
        username.setText(currentNumberPosition.getUsername());
        commeny_byuser.setText(currentNumberPosition.getComment());

        return currentItemView;
    }
}
