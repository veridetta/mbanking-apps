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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hinadadebank.R;
import com.example.hinadadebank.api.Client;
import com.example.hinadadebank.api.Interface;
import com.example.hinadadebank.model.CheckResponse;
import com.example.hinadadebank.model.TransResponse;
import com.example.hinadadebank.tools.CurrencyRupiah;
import com.example.hinadadebank.unverif.UploadActivity;
import com.example.hinadadebank.unverif.UploadResultActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View ly;
    TextView tx_saldo, tx_name;
    Interface mApiInterface;
    RelativeLayout lyLoading;
    String usersId,name, rupiah;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        ly = inflater.inflate(R.layout.fragment_home, container, false);
        shared();
        initial();
        getData();
        return ly;
    }

    void initial(){
        tx_saldo = ly.findViewById(R.id.tx_saldo);
        tx_name = ly.findViewById(R.id.tx_name);
        lyLoading = ly.findViewById(R.id.ly_loading);
        tx_name.setText(name);
    }
    void shared(){
        SharedPreferences sgSharedPref = getActivity().getSharedPreferences("sg_shared_pref", getActivity().MODE_PRIVATE);
        usersId= String.valueOf(sgSharedPref.getInt("id",1));
        name = sgSharedPref.getString("name", "tidak ada nama");
    }
    void getData(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<TransResponse> saldoCheck = mApiInterface.checkSaldo(usersId);
        saldoCheck.enqueue(new Callback<TransResponse>() {
            @Override
            public void onResponse(Call<TransResponse> call, Response<TransResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                        String saldo = String.valueOf(response.body().getData().getSaldo());
                        Double saldoCon = Double.valueOf(saldo);
                        CurrencyRupiah rp = new CurrencyRupiah();
                        tx_name.setText(name);
                        tx_saldo.setText(rp.rupiah(saldoCon));
                    }else{
                        Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}