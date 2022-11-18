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
import com.example.hinadadebank.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView btnLogin;
    Button btnRegister;
    EditText txName, txNik, txPhone, txCard, txEmail, txUsername, txPass;
    Interface mApiInterface;
    RelativeLayout lyLoading;
    ProgressBar pgCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hideTaskbar();
        //initial view
        initialView();
        mApiInterface = Client.getClient().create(Interface.class);

    }

    public void hideTaskbar(){
        getSupportActionBar().hide();
    }
    void initialView(){
        txName = findViewById(R.id.tx_name);
        txNik = findViewById(R.id.tx_nik);
        txPhone = findViewById(R.id.tx_phone);
        txCard = findViewById(R.id.tx_card);
        txEmail = findViewById(R.id.tx_email);
        txUsername = findViewById(R.id.tx_username);
        txPass = findViewById(R.id.tx_password);
        lyLoading = findViewById(R.id.ly_loading);
        pgCircle  = findViewById(R.id.simpleProgressBar);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyLoading.setVisibility(View.VISIBLE);
                String name = txEmail.getText().toString();
                String nik = txNik.getText().toString();
                String tel = txPhone.getText().toString();
                String card = txCard.getText().toString();
                String email = txEmail.getText().toString();
                String username = txUsername.getText().toString();
                String pass = txPass.getText().toString();
                String status = "unverif";
                if(registerChecker(name, nik, tel, card, email, username, pass)){
                    Call<UserResponse> postRegisterExe = mApiInterface.postRegister(name, nik, tel,
                            card,email, username,pass, status);
                    postRegisterExe.enqueue(new Callback<UserResponse>() {
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
                                    editor.apply();
                                    Intent nn = new Intent(RegisterActivity.this, MainActivity.class);
                                    nn.putExtra("name", nama);
                                    finish();
                                    startActivity(nn);
                                }else{
                                    Toast.makeText(getApplicationContext() ,response.body().getMessages(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                lyLoading.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext() ,"Register gagal",Toast.LENGTH_SHORT).show();
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
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nn = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(nn);
            }
        });
    }
    boolean registerChecker(String name, String nik, String phone, String card, String username,
                            String email, String pass){
        Boolean valid = false;
        if(email.equals("") && pass.equals("") && name.equals("")&& nik.equals("")
                && phone.equals("")&& card.equals("")&& username.equals("")){
            Toast.makeText(RegisterActivity.this, "Semuanya tidak boleh kosong",Toast.LENGTH_LONG).show();
            lyLoading.setVisibility(View.GONE);
        }else{
            valid = true;
        }
        return valid;
    }
}