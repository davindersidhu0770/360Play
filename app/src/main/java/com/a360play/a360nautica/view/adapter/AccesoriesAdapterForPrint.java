package com.a360play.a360nautica.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a360play.a360nautica.R;
import com.a360play.a360nautica.data.booking.AccessoryDataResponse;
import com.a360play.a360nautica.data.booking.AccessoryResponse;
import com.a360play.a360nautica.data.booking.GameAccessoriesItem;
import com.a360play.a360nautica.data.booking.PrintAccessory;

import java.util.ArrayList;

public class AccesoriesAdapterForPrint extends RecyclerView.Adapter<AccesoriesAdapterForPrint.MyViewHolder> {

    ArrayList<PrintAccessory> mList;
    Context context;
    String currency;

    public AccesoriesAdapterForPrint(Context context, ArrayList<PrintAccessory> accorieslist, String currency) {
        this.context = context;
        this.mList = accorieslist;
        this.currency = currency;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_accessories_print, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int i) {

        String accessname = mList.get(viewHolder.getAdapterPosition()).getAccessory();
        String quantity = String.valueOf(mList.get(viewHolder.getAdapterPosition()).getQuantity());
        String totalPrice = String.valueOf(mList.get(viewHolder.getAdapterPosition()).getTotalPrice());

        Log.d("Issuee>>", "quantity : " + quantity + "totalPrice : " + totalPrice);

        viewHolder.tv_accesoriesname.setText(accessname + " " +"\n"+ "(" + quantity + ")");
        viewHolder.tvprice.setText(currency + " " + totalPrice);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_accesoriesname, tv_count, tvprice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_accesoriesname = itemView.findViewById(R.id.tv_accesoriesname);
            tvprice = itemView.findViewById(R.id.tvprice);
            tv_count = itemView.findViewById(R.id.tv_count);

        }
    }


}