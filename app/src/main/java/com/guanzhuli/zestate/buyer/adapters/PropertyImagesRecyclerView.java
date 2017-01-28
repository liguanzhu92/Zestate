package com.guanzhuli.zestate.buyer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guanzhuli.zestate.R;
import com.squareup.picasso.Picasso;

/**
 * Created by shashank reddy on 1/24/2017.
 */
public class PropertyImagesRecyclerView extends RecyclerView.Adapter<PropertyImagesRecyclerView.MyViewHolder> {
    Context mContext;
    String[] mString;
    int count;

    public PropertyImagesRecyclerView(Context context, String[] imageUrls, int count){
        mContext = context;
        mString = imageUrls;
        this.count = count;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_layout_images,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView textCount = holder.textCount;
        ImageView propertyImageView = holder.propertyImageView;
        textCount.setText("");
        if(count<=1 && mString[0].equalsIgnoreCase("no image"))
            propertyImageView.setImageResource(R.drawable.home_placeholder);
        else
            Picasso.with(mContext).load("http://".concat(mString[position])).error(R.drawable.home_default).into(propertyImageView);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView propertyImageView;
        TextView textCount;
        public MyViewHolder(View itemView) {
            super(itemView);
            propertyImageView = (ImageView) itemView.findViewById(R.id.property_view_flipper_image_view);
            textCount = (TextView) itemView.findViewById(R.id.view_flipper_count_text);
        }
    }
}
