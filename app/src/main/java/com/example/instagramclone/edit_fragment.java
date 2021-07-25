package com.example.instagramclone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class edit_fragment  extends Fragment {
    EditText user_name;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_profile,container,false);

    }
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

                if(ParseUser.getCurrentUser().get("profilepicture")!=null){
                    ParseUser.getCurrentUser().remove("profilepicture");
                }
                ParseUser.getCurrentUser().put("profilepicture",file);

               ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
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

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        user_name=view.findViewById(R.id.u_name);
        super.onViewCreated(view, savedInstanceState);
        String u_name=ParseUser.getCurrentUser().getUsername();
        user_name.setText(u_name);
        ImageView profilepicture= view.findViewById(R.id.profilepicture);
        EditText pass=view.findViewById(R.id.password);
        EditText repass=view.findViewById(R.id.repass);
        EditText bio=view.findViewById(R.id.profile_bio);
        String user_bio;
        if(ParseUser.getCurrentUser().get("bio")!=null){
             user_bio=ParseUser.getCurrentUser().get("bio").toString();
        }
        else{
            user_bio=" ";
        }
        bio.setText(user_bio);
        if(ParseUser.getCurrentUser().get("profilepicture")==null){
            profilepicture.setImageResource(R.drawable.instalogo);
        }
        else {

            ParseFile file = (ParseFile) ParseUser.getCurrentUser().get("profilepicture");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null && data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        profilepicture.setImageBitmap(bitmap);
                    }
                }
            });
        }

        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });

    }
}
