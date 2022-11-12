package com.example.hinadadebank.verif;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hinadadebank.LoginActivity;
import com.example.hinadadebank.R;
import com.example.hinadadebank.api.Client;
import com.example.hinadadebank.api.Interface;
import com.example.hinadadebank.model.TransResponse;
import com.example.hinadadebank.model.UserResponse;
import com.example.hinadadebank.tools.CurrencyRupiah;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View ly;
    EditText tvName, tvNik, tvTelp, tvEmail, tvUsername, tvPassword;
    Interface mApiInterface;
    RelativeLayout lyLoading;
    String usersId,name, card;
    String nama, nik, telp, email, username, password;
    Boolean editable=false;
    Button btnEdit, btnSimpan, btnDelete;
    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ly = inflater.inflate(R.layout.fragment_user, container, false);
        initial();
        shared();
        getData();
        disabled();
        click();
        return ly;
    }
    void initial(){
        tvName = ly.findViewById(R.id.tx_name);
        tvNik = ly.findViewById(R.id.tx_nik);
        tvTelp = ly.findViewById(R.id.tx_phone);
        tvEmail = ly.findViewById(R.id.tx_email);
        tvUsername = ly.findViewById(R.id.tx_username);
        tvPassword = ly.findViewById(R.id.tx_password);
        lyLoading = ly.findViewById(R.id.ly_loading);
        btnEdit = ly.findViewById(R.id.btn_ubah);
        btnSimpan = ly.findViewById(R.id.btn_simpan);
        btnDelete = ly.findViewById(R.id.btn_delete);
    }
    void shared(){
        SharedPreferences sgSharedPref = getActivity().getSharedPreferences("sg_shared_pref", getActivity().MODE_PRIVATE);
        usersId= String.valueOf(sgSharedPref.getInt("id",1));
        name = sgSharedPref.getString("name", "tidak ada nama");
        card = sgSharedPref.getString("card", "00");
    }
    void disabled(){
        tvName.setEnabled(false);
        tvNik.setEnabled(false);
        tvTelp.setEnabled(false);
        tvEmail.setEnabled(false);
        tvUsername.setEnabled(false);
        tvPassword.setEnabled(false);
    }
    void enabled(){
        tvName.setEnabled(true);
        tvNik.setEnabled(true);
        tvTelp.setEnabled(true);
        tvEmail.setEnabled(true);
        tvUsername.setEnabled(true);
        tvPassword.setEnabled(true);
    }
    void click(){
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editable){
                    disabled();
                    editable=false;
                }else{
                    enabled();
                    editable=true;
                }
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Boolean ok = check();
               if(ok){
                   postData();
               }else{
                   Toast.makeText(getActivity(), "Tidak boleh Kosong",Toast.LENGTH_SHORT).show();
               }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }
    boolean check(){
        Boolean verif = false;
        nama = tvName.getText().toString();
        nik = tvNik.getText().toString();
        telp = tvTelp.getText().toString();
        email = tvEmail.getText().toString();
        username = tvUsername.getText().toString();
        password = tvPassword.getText().toString();
        if(!nama.equals("")&&!nik.equals("")&&!telp.equals("")&&!email.equals("")&&!username.equals("")&&!password.equals("")){
            verif=true;
        }
        return verif;
    }
    void getData(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<UserResponse> getUsers = mApiInterface.getUsers(usersId);
        getUsers.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                        tvName.setText(response.body().getData().getName());
                        tvNik.setText(String.valueOf(response.body().getData().getNik()));
                        tvTelp.setText(String.valueOf(response.body().getData().getTel()));
                        tvEmail.setText(response.body().getData().getEmail());
                        tvUsername.setText(response.body().getData().getUsername());
                        tvPassword.setText("");
                    }else{
                        Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    void deleteData(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<UserResponse> getUsers = mApiInterface.deleteUsers(usersId);
        getUsers.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                        Toast.makeText(getActivity() ,"Berhasil menghapus akun",Toast.LENGTH_SHORT).show();
                        Intent nn = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(nn);
                        getActivity().finish();
                    }else{
                        Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    void postData(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<UserResponse> getUsers = mApiInterface.postUsers(usersId,nama,nik,telp,email,username,password);
        getUsers.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                        tvName.setText(response.body().getData().getName());
                        tvNik.setText(String.valueOf(response.body().getData().getNik()));
                        tvTelp.setText(String.valueOf(response.body().getData().getTel()));
                        tvEmail.setText(response.body().getData().getEmail());
                        tvUsername.setText(response.body().getData().getUsername());
                        tvPassword.setText("");
                    }else{
                        Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}