package com.developer.android.quickveggis.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.android.quickveggis.App;
import com.developer.android.quickveggis.R;
import com.developer.android.quickveggis.model.WalletHistory;
import com.developer.android.quickveggis.ui.activity.ProductsActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.developer.android.quickveggis.App.getContext;

/**
 * Created by applepc on 09/02/2018.
 */

public class retailerslistAdapter extends RecyclerView.Adapter<retailerslistAdapter.Holder> {

    Context context;
    ArrayList<String> data;
    ArrayList<Integer> images;


    public class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_icon)
        ImageView image_icon;
        @Bind(R.id.retailers_name)
        TextView retailers_name;
        @Bind(R.id.back_lay)
        LinearLayout back_lay_lv;



        public Holder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public retailerslistAdapter(Context context, ArrayList<String> data, ArrayList<Integer> images){
        this.context = context;
        this.data = new ArrayList<>();
        this.images = new ArrayList<>();
        this.data.addAll(data);
        this.images.addAll(images);

    }

    public void setData(ArrayList<String> products){
        this.data = products;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType){
        return new retailerslistAdapter.Holder(LayoutInflater.from(getContext()).inflate(R.layout.reatilerslistlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position){
        String product = data.get(position);
        Integer image = images.get(position);

        if(position == ProductsActivity.selectedPosition){
            Drawable d = context.getResources().getDrawable(R.drawable.ic_white_circle_green);
            holder.back_lay_lv.setBackgroundDrawable(d);
        }else{
            Drawable d = context.getResources().getDrawable(R.drawable.ic_white_circle);
            holder.back_lay_lv.setBackgroundDrawable(d);
        }

        holder.retailers_name.setText(product);
        holder.image_icon.setImageResource(image);
    }

    public Context getContext(){
        return this.context;
    }

    public int getItemCount() {
        return this.data.size();
    }

}
