package com.example.hinadadebank.verif;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hinadadebank.MainActivity;
import com.example.hinadadebank.R;
import com.example.hinadadebank.adapter.TransAdapter;
import com.example.hinadadebank.api.Client;
import com.example.hinadadebank.api.Interface;
import com.example.hinadadebank.model.MutasiData;
import com.example.hinadadebank.model.MutasiResponse;
import com.example.hinadadebank.model.TransData;
import com.example.hinadadebank.model.TransResponse;
import com.example.hinadadebank.tools.CurrencyRupiah;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MutasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MutasiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String stUserId;
    Interface mApiInterface;
    RelativeLayout lyLoading;
    View ly;
    TextView txSaldo;
    RecyclerView rcTrans;
    private TransAdapter adapter;
    private ArrayList<MutasiData> transModelArrayList;
    public MutasiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MutasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MutasiFragment newInstance(String param1, String param2) {
        MutasiFragment fragment = new MutasiFragment();
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
        ly = inflater.inflate(R.layout.fragment_mutasi, container, false);
        initial();
        shared();
        getSaldo();
        return ly;
    }
    void initial(){
        txSaldo = ly.findViewById(R.id.tx_saldo);
        rcTrans = ly.findViewById(R.id.rc_trans);
        lyLoading = ly.findViewById(R.id.ly_loading);
    }
    void shared(){
        SharedPreferences sgSharedPref = getActivity().getSharedPreferences("sg_shared_pref", getActivity().MODE_PRIVATE);
        stUserId= String.valueOf(sgSharedPref.getInt("id",1));
    }
    void getSaldo(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<TransResponse> saldoCheck = mApiInterface.checkSaldo(stUserId);
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
                        txSaldo.setText(rp.rupiah(saldoCon));
                    }else{
                        Toast.makeText(getActivity() ,"Tidak ada data",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                }
                getMutasi();
            }

            @Override
            public void onFailure(Call<TransResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    void getMutasi(){
        lyLoading.setVisibility(View.VISIBLE);
        transModelArrayList = new ArrayList<MutasiData>();
        mApiInterface = Client.getClient().create(Interface.class);
        Call<MutasiResponse> mutasi = mApiInterface.mutasi(stUserId,"default","","");
        mutasi.enqueue(new Callback<MutasiResponse>() {
            @Override
            public void onResponse(Call<MutasiResponse> call, Response<MutasiResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    Log.d("MutasiFragment", "onResponse: "+response.body().getData());
                    if(response.body().getStatus().equals("success")){

                        /*Log.v("log softgain : ", String.valueOf(response.body().getSuccess().getToken()));*/
                        // below line we are creating a new array list
                        ArrayList<MutasiData> mm = response.body().getData();
                        transModelArrayList=mm;
                        // initializing our adapter class.
                        adapter = new TransAdapter(transModelArrayList, getActivity());

                        // adding layout manager to our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        rcTrans.setHasFixedSize(true);

                        // setting layout manager
                        // to our recycler view.
                        rcTrans.setLayoutManager(manager);

                        // setting adapter to
                        // our recycler view.
                        rcTrans.setAdapter(adapter);

                    }else{
                        Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MutasiResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}