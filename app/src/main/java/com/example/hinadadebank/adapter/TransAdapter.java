package com.example.hinadadebank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hinadadebank.R;
import com.example.hinadadebank.model.MutasiData;
import com.example.hinadadebank.model.TransData;
import com.example.hinadadebank.tools.CurrencyRupiah;

import java.util.ArrayList;
import java.util.Currency;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<MutasiData> transModelArrayList;
    private Context context;

    // creating a constructor for our variables.
    public TransAdapter(ArrayList<MutasiData> courseModelArrayList, Context context) {
        this.transModelArrayList = courseModelArrayList;
        this.context = context;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<MutasiData> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        transModelArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        MutasiData model = transModelArrayList.get(position);
        String desc = model.getDesc()+"\n"+model.getDest();
        holder.tvDate.setText(model.getSaldo());
        holder.tvDesc.setText(desc);
        CurrencyRupiah rp = new CurrencyRupiah();
        String debit = "+ "+rp.rupiah(model.getDebit());
        String credit = "- "+rp.rupiah(model.getCredit());
        if(model.getDebit()>0){
            holder.tvAmount.setText(debit);
        }else{
            holder.tvAmount.setText(credit);
        }
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return transModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView tvDate, tvDesc,tvAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }
}