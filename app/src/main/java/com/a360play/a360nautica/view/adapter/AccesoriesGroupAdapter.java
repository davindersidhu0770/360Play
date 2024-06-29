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
import com.a360play.a360nautica.extension.MyCustomObjectListener;

import java.util.ArrayList;

public class AccesoriesGroupAdapter extends RecyclerView.Adapter<AccesoriesGroupAdapter.MyViewHolder> {

    ArrayList<AccessoryDataResponse> mList;
    Context context;
    MyCustomObjectListener listener;
    String currency;
    //int total_count=0;

    public AccesoriesGroupAdapter(Context context, ArrayList<AccessoryDataResponse> accorieslist,
                                  MyCustomObjectListener listener, String currency) {
        this.context = context;
        this.mList = accorieslist;
        this.listener = listener;
        this.currency = currency;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_accessories, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int i) {
        String accessname = mList.get(viewHolder.getAdapterPosition()).getName();
        String price = String.valueOf(mList.get(viewHolder.getAdapterPosition()).getPrice());
//        viewHolder.tv_accesoriesname.setText(accessname + " ("+currency + " " + price +")");
        viewHolder.tv_accesoriesname.setText(accessname);
        viewHolder.tv_count.setText(String.valueOf(mList.get(viewHolder.getAdapterPosition()).getItemcount()));
        viewHolder.tvprice.setText(price);

        if (mList.get(viewHolder.getAdapterPosition()).isSelected()) {
            viewHolder.cb_accesories.setChecked(true);
        } else {
            viewHolder.cb_accesories.setChecked(false);
        }
        viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(viewHolder.tv_count.getText().toString());
                count++;
                viewHolder.cb_accesories.setChecked(true);
                mList.get(viewHolder.getAdapterPosition()).setItemcount(count);
                mList.get(viewHolder.getAdapterPosition()).setSelected(true);
                viewHolder.tv_count.setText(String.valueOf(count));
            }
        });

        viewHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(viewHolder.tv_count.getText().toString());
                if (count >= 1) {
                    count--;
                    viewHolder.tv_count.setText(String.valueOf(count));
                    if (count == 0) {
                        viewHolder.cb_accesories.setChecked(false);
                        mList.get(viewHolder.getAdapterPosition()).setItemcount(count);
                        mList.get(viewHolder.getAdapterPosition()).setSelected(false);
                    } else {
                        mList.get(viewHolder.getAdapterPosition()).setItemcount(count);
                        mList.get(viewHolder.getAdapterPosition()).setSelected(true);
                    }
                }


            }
        });

        viewHolder.cb_accesories.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int count = Integer.parseInt(viewHolder.tv_count.getText().toString());
                    count++;
                    viewHolder.tv_count.setText(String.valueOf(count));
                    mList.get(viewHolder.getAdapterPosition()).setItemcount(count);
                    mList.get(viewHolder.getAdapterPosition()).setSelected(true);
                } else {
                    viewHolder.tv_count.setText(String.valueOf(0));
                    mList.get(viewHolder.getAdapterPosition()).setItemcount(0);
                    mList.get(viewHolder.getAdapterPosition()).setSelected(false);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout btn_remove, btn_add;
        TextView tv_accesoriesname, tvprice, tv_count;
        CheckBox cb_accesories;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvprice = itemView.findViewById(R.id.tvprice);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            btn_add = itemView.findViewById(R.id.btn_add);
            tv_accesoriesname = itemView.findViewById(R.id.tv_accesoriesname);
            tv_count = itemView.findViewById(R.id.tv_count);
            cb_accesories = itemView.findViewById(R.id.cb_accesories);

        }
    }


}