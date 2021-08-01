package com.example.instagramclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class commentpopup extends DialogFragment {
    List commentby;
    String postid;
    ListView comments;
    ArrayAdapter<comment> adapter;
    commentpopup(String postid,List commentby){
        this.postid=postid;
        this.commentby=commentby;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.comment_popup,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         comments=view.findViewById(R.id.comments);
        ArrayList<comment> display_comment=new ArrayList<comment>();
            for(int i=0;i<commentby.size();i++){
                String[] user_comment=commentby.get(i).toString().split("-");
                String user=user_comment[0];
                String com=user_comment[1];
                display_comment.add(new comment(user,com));

            }
        adapter = new customCommentView(view.getContext(), display_comment);
        comments.setAdapter(adapter);

        Button postcomment=view.findViewById(R.id.postcomment);
        EditText enter_comment=view.findViewById(R.id.entercomment);
        postcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=enter_comment.getText().toString();

                display_comment.add(new comment(ParseUser.getCurrentUser().getUsername(),comment));
                if(commentby.size()==0){
                    adapter=new customCommentView(view.getContext(),display_comment);
                    comments.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
                String parse_comment= ParseUser.getCurrentUser().getUsername()+"-"+comment;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
                query.whereEqualTo("objectId", postid);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() == 1) {
                                ParseObject object = objects.get(0);
                                object.add("comments", parse_comment);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        /*commentpopup cp=new commentpopup(postid,commentby);
                                        cp.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "this");*/
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }
}
