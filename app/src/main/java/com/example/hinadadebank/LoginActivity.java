package com.example.hinadadebank;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hinadadebank.api.Client;
import com.example.hinadadebank.api.Interface;
import com.example.hinadadebank.model.UserData;
import com.example.hinadadebank.model.UserResponse;
import com.example.hinadadebank.unverif.UploadActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txEmail, txPass;
    TextView btnRegister;
    Interface mApiInterface;
    RelativeLayout lyLoading;
    ProgressBar pgCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //hide action bar
        hideTaskbar();
        haslogin();
        //initial view
        initialView();
        mApiInterface = Client.getClient().create(Interface.class);
    }

    public void hideTaskbar(){
        getSupportActionBar().hide();
    }
    void haslogin(){
        SharedPreferences sgSharedPref = getApplicationContext().getSharedPreferences("sg_shared_pref", getApplicationContext().MODE_PRIVATE);
        Boolean haslogin = sgSharedPref.getBoolean("login",false);
        String status = sgSharedPref.getString("status","unverif");
        if(haslogin){
            checkStatus(status);
        }
    }
    void checkStatus(String status){
        if(status.equals("unverif")){
            Intent nn = new Intent(LoginActivity.this, UploadActivity.class);
            startActivity(nn);
            finish();
        }else{
            Intent nn = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(nn);
            finish();
        }
    }
    void initialView(){
        txEmail = findViewById(R.id.tx_email);
        txPass = findViewById(R.id.tx_password);
        lyLoading = findViewById(R.id.ly_loading);
        pgCircle  = findViewById(R.id.simpleProgressBar);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nn = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(nn);
            }
        });
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyLoading.setVisibility(View.VISIBLE);
                if(loginChecker(txEmail.getText().toString(),txPass.getText().toString())){
                    Call<UserResponse> postLoginExe = mApiInterface.postLogin(txEmail.getText().toString(), txPass.getText().toString());
                    postLoginExe.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if(response.isSuccessful()){
                                lyLoading.setVisibility(View.GONE);
                                Log.d(TAG, "onResponse: "+response.body().getStatus());
                                if(response.body().getStatus().equals("success")){
                                    /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                                    Toast.makeText(getApplicationContext(),
                                            "Login berhasil",Toast.LENGTH_SHORT).show();
                                    SharedPreferences sgSharedPref = getApplicationContext().getSharedPreferences("sg_shared_pref", getApplicationContext().MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sgSharedPref.edit();
                                    String nama = String.valueOf(response.body().getData().getName());
                                    Log.d(TAG, "onResponse: "+nama);
                                    editor.putBoolean("login", true);
                                    editor.putInt("id",response.body().getData().getId());
                                    editor.putString("name",response.body().getData().getName());
                                    editor.putString("email",response.body().getData().getEmail());
                                    editor.putString("status",response.body().getData().getStatus());
                                    editor.apply();
                                    checkStatus(response.body().getData().getStatus());
                                }else{
                                    Toast.makeText(getApplicationContext() ,response.body().getMessages(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                lyLoading.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext() ,"Login gagal",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            /*Log.v("log softgain : ", String.valueOf(t));*/
                            lyLoading.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                }
        });
    }
    boolean loginChecker(String email, String pass){
        Boolean valid = false;
        if(email.equals("") && pass.equals("")){
            Toast.makeText(LoginActivity.this, "Email atau password tidak boleh kosong",Toast.LENGTH_LONG).show();
            lyLoading.setVisibility(View.GONE);
        }else{
            valid = true;
        }
        return valid;
    }
}