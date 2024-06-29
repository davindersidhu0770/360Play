package com.a360play.a360nautica.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a360play.a360nautica.R;
import com.a360play.a360nautica.data.booking.AccessoryDataResponse;
import com.a360play.a360nautica.data.booking.AccessoryResponse;
import com.a360play.a360nautica.extension.MyCustomObjectListener;

import java.util.ArrayList;
import java.util.List;

public class AccesoriesAdapter extends RecyclerView.Adapter<AccesoriesAdapter.MyViewHolder> {

    List<AccessoryResponse> mList;
    Context context;
    String currency;

    public AccesoriesAdapter(Context context, List<AccessoryResponse> accorieslist, String currency) {
        this.context = context;
        this.mList = accorieslist;
        this.currency = currency;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_accessories_show, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int i) {
        AccessoryResponse item = mList.get(i);

        if (item.getQuantity() > 0) {
            viewHolder.itemView.setVisibility(View.VISIBLE);
            // Bind data to the view
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.setMargins(0, 0, 0, 0);
            viewHolder.itemView.setLayoutParams(params);
        } else {
            viewHolder.itemView.setVisibility(View.GONE);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.height = 0;
            params.width = 0;
            params.setMargins(0, 0, 0, 0);
            viewHolder.itemView.setLayoutParams(params);
        }

        String accessname = mList.get(viewHolder.getAdapterPosition()).getAccessory();
        String price = String.valueOf(mList.get(viewHolder.getAdapterPosition()).getPrice());
        viewHolder.tv_accesoriesname.setText(accessname + " (" + currency + " " + price + ")");
        viewHolder.tv_count.setText(" x " + String.valueOf(mList.get(viewHolder.getAdapterPosition()).getQuantity()) + " = ");
        viewHolder.tvprice.setText(String.valueOf(mList.get(viewHolder.getAdapterPosition()).getPrice() *
                mList.get(viewHolder.getAdapterPosition()).getQuantity()));

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