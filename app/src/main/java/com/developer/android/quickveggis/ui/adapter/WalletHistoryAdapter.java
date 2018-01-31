package com.developer.android.quickveggis.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.android.quickveggis.R;
import com.developer.android.quickveggis.model.Product;
import com.developer.android.quickveggis.model.WalletHistory;
import com.developer.android.quickveggis.ui.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mian Azhar on 1/29/2018.
 */

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.Holder> {
    Context context;
    ArrayList<WalletHistory> data;

    public class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.history_arrow_icon)
        ImageView history_arrow_icon;
        @Bind(R.id.history_user_tv)
        TextView history_user_tv;
        @Bind(R.id.history_price_tv)
        TextView history_price_tv;
        @Bind(R.id.history_activity_tv)
        TextView history_activity_tv;
        @Bind(R.id.history_date_tv)
        TextView history_date_tv;
        @Bind(R.id.main_layout)
        LinearLayout main_layout;
        @Bind(R.id.date_section)
        TextView date_section;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public WalletHistoryAdapter(Context context, ArrayList<WalletHistory> data) {
        this.context = context;
        this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    public void setData(ArrayList<WalletHistory> products) {
        this.data = products;
    }

    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.history_list_item, parent, false));
    }

    public void onBindViewHolder(Holder holder, int position) {
        WalletHistory product = data.get(position);

        if(product.isDateSection()){
            holder.main_layout.setVisibility(View.GONE);
            holder.date_section.setVisibility(View.VISIBLE);

            holder.date_section.setText(product.getDate());
        } else {
            holder.main_layout.setVisibility(View.VISIBLE);
            holder.date_section.setVisibility(View.GONE);

            holder.history_activity_tv.setText(product.getActivity());
            holder.history_date_tv.setText(product.getDate());
            holder.history_price_tv.setText(product.getPrice());
            holder.history_user_tv.setText(product.getUser());

            if(product.isSent())
                holder.history_arrow_icon.setImageResource(R.drawable.red_arrow);
            else
                holder.history_arrow_icon.setImageResource(R.drawable.green_arrow);
        }

    }

    public Context getContext() {
        return this.context;
    }

    public int getItemCount() {
        return this.data.size();
    }
}
