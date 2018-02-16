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

import com.developer.android.quickveggis.R;
import com.developer.android.quickveggis.model.Manufacturer;
import com.developer.android.quickveggis.ui.activity.ProductsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.developer.android.quickveggis.App.getContext;

/**
 * Created by applepc on 15/02/2018.
 */

public class brandslistAdapter extends RecyclerView.Adapter<brandslistAdapter.Holder> {

    Context context;
    ArrayList<Manufacturer> data;
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


    public brandslistAdapter(Context context, ArrayList<Manufacturer> data, ArrayList<Integer> images){
        this.context = context;
        this.data = new ArrayList<>();
        this.images = new ArrayList<>();
        this.data.addAll(data);
        this.images.addAll(images);

    }


    public void setData(ArrayList<Manufacturer> products){
        this.data = products;
    }

    @Override
    public brandslistAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType){
        return new brandslistAdapter.Holder(LayoutInflater.from(getContext()).inflate(R.layout.reatilerslistlayout, parent, false));
    }


    @Override
    public void onBindViewHolder(brandslistAdapter.Holder holder, int position){
        String product = data.get(position).getName();

        if(position == ProductsActivity.selectedPositionBrands){
            Drawable d = context.getResources().getDrawable(R.drawable.ic_white_circle_green);
            holder.back_lay_lv.setBackgroundDrawable(d);

        }else{
            Drawable d = context.getResources().getDrawable(R.drawable.ic_white_circle);
            holder.back_lay_lv.setBackgroundDrawable(d);
        }

        holder.retailers_name.setText(product);
        //Picasso.with(context).load(data.get(position).getImage()).into(holder.image_icon);
        try {
            Integer image = images.get(position);
            holder.image_icon.setImageResource(image);
        }catch (Exception e){

        }
    }

    public Context getContext(){
        return this.context;
    }

    public int getItemCount() {
        return this.data.size();
    }



}
