package com.example.hinadadebank;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hinadadebank.verif.HomeFragment;
import com.example.hinadadebank.verif.MutasiFragment;
import com.example.hinadadebank.verif.TransactionFragment;
import com.example.hinadadebank.verif.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hinadadebank.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        hideTaskbar();

    }
    HomeFragment homeFragment = new HomeFragment();
    TransactionFragment transactionFragment = new TransactionFragment();
    MutasiFragment mutasiFragment = new MutasiFragment();
    UserFragment userFragment = new UserFragment();
    public void hideTaskbar(){
        getSupportActionBar().hide();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;

            case R.id.nav_trans:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, transactionFragment).commit();
                return true;

            case R.id.nav_mutasi:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mutasiFragment).commit();
                return true;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, userFragment).commit();
                return true;
            case R.id.nav_logout:
                removedata();
                move();
                return true;
        }
        return false;
    }
    void removedata(){
        SharedPreferences sgSharedPref = getApplicationContext().getSharedPreferences("sg_shared_pref", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sgSharedPref.edit();
        editor.putBoolean("login", false);
        editor.apply();
    }
    void move(){
        Intent nn = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(nn);
    }
}