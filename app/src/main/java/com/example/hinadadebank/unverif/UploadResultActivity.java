package com.example.hinadadebank.unverif;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hinadadebank.LoginActivity;
import com.example.hinadadebank.R;
import com.example.hinadadebank.RegisterActivity;
import com.example.hinadadebank.api.Interface;
import com.example.hinadadebank.model.CheckResponse;
import com.example.hinadadebank.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadResultActivity extends AppCompatActivity {
    TextView txuserId;
    String usersId;
    RelativeLayout lyLoading;
    Interface mApiInterface;
    Boolean hasUpload=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_result);
        hideTaskbar();
        shared();
        initial();

    }
    public void hideTaskbar(){
        getSupportActionBar().hide();
    }
    void initial(){
        txuserId = findViewById(R.id.tx_user_id);
        txuserId.setText("ID : "+usersId);
    }
    void shared(){
        SharedPreferences sgSharedPref = getApplicationContext().getSharedPreferences("sg_shared_pref", getApplicationContext().MODE_PRIVATE);
        usersId= String.valueOf(sgSharedPref.getInt("id",1));
    }
}