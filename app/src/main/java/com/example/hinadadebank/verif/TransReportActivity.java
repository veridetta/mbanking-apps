package com.example.hinadadebank.verif;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hinadadebank.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class TransReportActivity extends AppCompatActivity {

    String stUsersId, stNoTrans, stTanggal, stFrom, stDest, stDesc, stAmount;
    TextView tvNo, tvTanggal, tvFrom, tvTo, tvAmount, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_report);

        hideTaskbar();
        shared();
        getintent();
        initial();
        setvalue();
    }
    public void hideTaskbar(){
        getSupportActionBar().hide();
    }
    void initial(){
        tvNo = findViewById(R.id.tv_no_trans);
        tvTanggal = findViewById(R.id.tv_tanggal);
        tvFrom = findViewById(R.id.tx_from);
        tvTo = findViewById(R.id.tx_to);
        tvAmount = findViewById(R.id.tx_amount);
        tvDesc = findViewById(R.id.tx_desc);
    }
    void getintent(){
        Intent intent = getIntent();
        stNoTrans = intent.getStringExtra("no_trans");
        stTanggal = intent.getStringExtra("tanggal");
        stFrom = intent.getStringExtra("from");
        stDest = intent.getStringExtra("dest");
        stDesc = intent.getStringExtra("desc");
        stAmount = intent.getStringExtra("amount");

    }
    void setvalue(){
        tvNo.setText(stNoTrans);
        tvTanggal.setText(stTanggal);
        tvFrom.setText(stFrom);
        tvTo.setText(stDest);
        tvAmount.setText(stAmount);
        tvDesc.setText(stDesc);
    }
    void shared(){
        SharedPreferences sgSharedPref = getApplicationContext().getSharedPreferences("sg_shared_pref", getApplicationContext().MODE_PRIVATE);
        stUsersId= String.valueOf(sgSharedPref.getInt("id",1));
    }
}