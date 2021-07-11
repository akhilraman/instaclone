package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    EditText name;
    EditText password;
    TextView login_signup;
    Button button;
    Boolean signup=true;


    public void transfer_SecondActivity(){
        Intent intent = new Intent(MainActivity.this,Main_Page.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_signup){
            String value=button.getText().toString();
            if(value.equals("signup")){
                signup=false;
                button.setText("login");
                login_signup.setText("or Signup");
            }
            else{
                signup=true;
                button.setText("signup");
                login_signup.setText("or Login");
            }
        }else if(v.getId()==R.id.instalogo||v.getId()==R.id.backgroundLayout){
            InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            log_sign(v);
        }
        return false;
    }

    public void login(){
        ParseUser.logInInBackground(name.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){

                    Toast.makeText(MainActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                    transfer_SecondActivity();

                }
                else{

                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signup(){
                    ParseUser user = new ParseUser();
                    user.setUsername(name.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
                                transfer_SecondActivity();
                            }
                            else{
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


    public void log_sign(View view) {


        if (name.getText().toString().trim().length() == 0 || password.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Enter details!", Toast.LENGTH_SHORT).show();
        }

        if (name.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
            if (signup == true) {
                signup();
            } else {
                login();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

         name= (EditText)findViewById(R.id.name);
         password=(EditText)findViewById(R.id.password);
         password.setOnKeyListener(this);
         ImageView instalogo=findViewById(R.id.instalogo);
         ConstraintLayout backgroundLayout=findViewById(R.id.backgroundLayout);
         instalogo.setOnClickListener(this);

         backgroundLayout.setOnClickListener(this);
         button=findViewById(R.id.button);
         login_signup=(TextView)findViewById(R.id.login_signup);
         login_signup.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            transfer_SecondActivity();
        }
        else{
            Log.i("status","user is logged out");
        }







    }
}