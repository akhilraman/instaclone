package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

public class Main_Page extends AppCompatActivity {
private DrawerLayout drawerLayout;
private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        NavigationView sidebar=findViewById(R.id.sidebar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sidebar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Logout:
                        //Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                        ParseUser.logOut();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;

                    default:
                        return false;
                }
            }
        });

        BottomNavigationView bottomNav=findViewById(R.id.button_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navlister);
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new HomeFragment()).commit();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private BottomNavigationView.OnNavigationItemSelectedListener navlister=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment=null;

                    switch(item.getItemId()){

                        case R.id.home:
                            selectedFragment=new HomeFragment();
                            break;
                        case R.id.search:
                            selectedFragment=new ExploreFragment();
                            break;
                        case R.id.profile:
                            selectedFragment=new ProfileFragment(ParseUser.getCurrentUser().getUsername());
                            /*Bundle bundle = new Bundle();
                            bundle.putString("profile",ParseUser.getCurrentUser().getUsername());
                            selectedFragment.setArguments(bundle);*/
                            break;
                        case R.id.upload:
                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            } else {
                                selectedFragment=new UploadFragment();
                            }
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,selectedFragment).commit();
                    return true;
                }
            };

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        super.onOptionsItemSelected(item);

        return false;
        }



}
