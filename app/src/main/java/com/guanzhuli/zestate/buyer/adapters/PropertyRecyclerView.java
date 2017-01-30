package com.guanzhuli.zestate.buyer.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.buyer.fragments.PropertyViewFragment;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.Property;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shashank reddy on 1/22/2017.
 */
public class PropertyRecyclerView extends RecyclerView.Adapter<PropertyRecyclerView.PropertyViewHolder> {
    ArrayList<Property> mPropertyList;
    FragmentActivity mContext;
    OnClickCard mOnclickCard;
    public PropertyRecyclerView(Context context,OnClickCard mOnclickCard){
        mPropertyList= VolleyController.getInstance().getmProperty().getPropertyList();
        this.mContext = (FragmentActivity) context;
        this.mOnclickCard = mOnclickCard;
    }
    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proprty_intial_view_layout,parent,false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, final int position) {
        TextView propertyName = holder.propertyName,propertyType = holder.propertyType,
                propertyCost = holder.propertyCost
                ,propertySize= holder.propertySize;
        ImageView propertyImageView = holder.propertyImageView;
        CardView propeCardView = holder.propertyCardView;
        Property property = mPropertyList.get(position);
        propertyName.setText(property.getName());
        propertyType.setText(property.getType());
        propertyCost.setText("$"+property.getCost());
        propertySize.setText(property.getCost()+"Sq.ft");
        if(property.getImage1().equals(""))
            propertyImageView.setImageResource(R.drawable.home_placeholder);
        else {
            Picasso.with(mContext).load(property.getImage1())
                    .error(R.drawable.home_default)
                    .placeholder(R.drawable.home_placeholder)
                    .into(propertyImageView);
        }

        propeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnclickCard.onClickOfCardItem();
                Log.d(PropertyRecyclerView.class.getSimpleName(),"item clicked on"+position);
                FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_replaceble, PropertyViewFragment.newInstance(mPropertyList.get(position).getId()))
                        .addToBackStack(null).commit();
                Log.d(PropertyRecyclerView.class.getSimpleName(),mPropertyList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView propertyName,propertyType,propertyCost,propertySize;
        ImageView propertyImageView;
        CardView propertyCardView;
        public PropertyViewHolder(View itemView) {
            super(itemView);
            propertyCardView = (CardView) itemView.findViewById(R.id.property_intial_card_view);
            propertyName = (TextView) itemView.findViewById(R.id.property_name);
            propertyType= (TextView) itemView.findViewById(R.id.property_type);
            propertyCost= (TextView) itemView.findViewById(R.id.property_price);
            propertySize= (TextView) itemView.findViewById(R.id.property_size);
            propertyImageView = (ImageView) itemView.findViewById(R.id.property_Image1_view);
        }
    }

    public void notifyOnDataChange(){
        mPropertyList.clear();
        mPropertyList = VolleyController.getInstance().getmProperty().getPropertyList();
        this.notifyDataSetChanged();
    }

    public interface OnClickCard{
         void onClickOfCardItem();
    }
}
