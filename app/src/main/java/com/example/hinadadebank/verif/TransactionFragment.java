package com.example.hinadadebank.verif;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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

import com.example.hinadadebank.MainActivity;
import com.example.hinadadebank.R;
import com.example.hinadadebank.api.Client;
import com.example.hinadadebank.api.Interface;
import com.example.hinadadebank.model.TransResponse;
import com.example.hinadadebank.model.UserResponse;
import com.example.hinadadebank.tools.CurrencyRupiah;
import com.example.hinadadebank.tools.DateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText etCard,etAmount, etDesc;
    TextView tvName;
    Button btnCek, btnAction;
    View ly;
    RelativeLayout lyLoading;
    String stUserId, stCard, stAmount, stDest, stDesc;
    Interface mApiInterface;
    Boolean penerima = false, check = false;
    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
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
        ly = inflater.inflate(R.layout.fragment_transaction, container, false);
        initial();
        shared();
        return ly;
    }
    void initial(){
        etCard = ly.findViewById(R.id.tx_card);
        etAmount = ly.findViewById(R.id.tx_amount);
        etDesc = ly.findViewById(R.id.tx_desc);
        tvName = ly.findViewById(R.id.tx_name);
        btnAction = ly.findViewById(R.id.btn_action);
        btnCek = ly.findViewById(R.id.btn_cek);
        lyLoading = ly.findViewById(R.id.ly_loading);

        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stDest = etCard.getText().toString();
                getData();
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stDest = etCard.getText().toString();
                stAmount = etAmount.getText().toString();
                stDesc = etDesc.getText().toString();
                check = checkForm(stDest, stAmount,stDesc);
                if(check){
                    postData();
                }
            }
        });
    }
    void shared(){
        SharedPreferences sgSharedPref = getActivity().getSharedPreferences("sg_shared_pref", getActivity().MODE_PRIVATE);
        stUserId= String.valueOf(sgSharedPref.getInt("id",1));
        stCard = sgSharedPref.getString("card", "0000000");
    }
    Boolean checkForm(String card, String amount, String desc){
        Boolean hasil = false;
        if (!card.equals("") && !amount.equals("") && !desc.equals("") && penerima){
            hasil=true;
        }
        return hasil;
    }
    void getData(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<UserResponse> cardCheck = mApiInterface.checkCard(stDest);
        cardCheck.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        tvName.setText(response.body().getData().getName());
                        penerima=true;
                    }else{
                        Toast.makeText(getActivity() ,"Tidak ada data",Toast.LENGTH_SHORT).show();
                        penerima=false;
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                    penerima=false;
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
                penerima=false;
            }
        });
    }
    void postData(){
        lyLoading.setVisibility(View.VISIBLE);
        mApiInterface = Client.getClient().create(Interface.class);
        Call<TransResponse> postTrans = mApiInterface.postTrans(stUserId,"0",stAmount,"0",stCard,stDest,stDesc);
        postTrans.enqueue(new Callback<TransResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<TransResponse> call, Response<TransResponse> response) {
                if(response.isSuccessful()){
                    lyLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: "+response.body().getStatus());
                    if(response.body().getStatus().equals("success")){
                        Intent nn = new Intent(getActivity(), TransReportActivity.class);
                        DateFormat dtf = new DateFormat();
                        nn.putExtra("no_trans","HBB"+dtf.tanggal_no()+response.body().getData().getUsersId());
                        nn.putExtra("tanggal", dtf.tanggal_trans());
                        nn.putExtra("from",stCard);
                        nn.putExtra("dest", stDest);
                        nn.putExtra("desc",stDesc);
                        nn.putExtra("amount",stAmount);
                        getActivity().startActivity(nn);
                        getActivity().finish();
                    }else{
                        Toast.makeText(getActivity() ,response.body().getMessages(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity() ,"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                    penerima=false;
                }
            }

            @Override
            public void onFailure(Call<TransResponse> call, Throwable t) {
                /*Log.v("log softgain : ", String.valueOf(t));*/
                lyLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+t.toString(), Toast.LENGTH_LONG).show();
                penerima=false;
            }
        });
    }
}