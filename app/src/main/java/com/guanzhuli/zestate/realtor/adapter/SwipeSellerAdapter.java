package com.guanzhuli.zestate.realtor.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.model.PostPropertyList;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.realtor.fragment.NewPropertyFragment;
import com.guanzhuli.zestate.realtor.fragment.PropertyDetailFragment;
import com.guanzhuli.zestate.realtor.fragment.SellerHomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Guanzhu Li on 1/21/2017.
 */
public class SwipeSellerAdapter extends RecyclerSwipeAdapter<SwipeSellerHolder>{
    private Context mContext;
    private ArrayList<Property> mList;

    public SwipeSellerAdapter(Context context, ArrayList<Property> obj) {
        mList = obj;
        mContext = context;
    }

    @Override
    public SwipeSellerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_seller, parent, false);
        SwipeSellerHolder swipeSellerHolder = new SwipeSellerHolder(view);
        return swipeSellerHolder;
    }

    @Override
    public void onBindViewHolder(SwipeSellerHolder viewHolder, final int position) {
        viewHolder.mTextName.setText(mList.get(position).getName());
        viewHolder.mTextCost.setText(mList.get(position).getCost());
        viewHolder.mTextCategory.setText(String.valueOf(mList.get(position).getCategory()));
        viewHolder.mTextAddress.setText(mList.get(position).getAddress1()
                + " " +mList.get(position).getAddress2());
        viewHolder.mSwipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "nav to detail", Toast.LENGTH_SHORT).show();
                PropertyDetailFragment propertyDetailFragment = new PropertyDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("DetailPosition", position);
                propertyDetailFragment.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.seller_activity_container, propertyDetailFragment)
                        .addToBackStack(SellerHomeFragment.class.getName())
                        .commit();
            }
        });
        String headURL = "https://www.shareicon.net/data/256x256/2015/09/15/101491_house_512x512.png";
        if (!mList.get(position).getImage1().equals("")) {
            headURL = mList.get(position).getImage1();
        } else if (!mList.get(position).getImage2().equals("")) {
            headURL = mList.get(position).getImage2();
        } else if (!mList.get(position).getImage3().equals("")) {
            headURL = mList.get(position).getImage3();
        }
        if (headURL != null) {
                Picasso.with(mContext)
                        .load(headURL)
                        .into(viewHolder.mImageHead);
        } else {
            viewHolder.mImageHead.setImageResource(R.mipmap.house_sample);
        }
        viewHolder.mImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPropertyFragment newPropertyFragment = new NewPropertyFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("EditPosition", position);
                newPropertyFragment.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.seller_activity_container, newPropertyFragment)
                        .addToBackStack(PropertyDetailFragment.class.getName())
                        .commit();
            }
        });
        viewHolder.mImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostPropertyList.getInstance().deleteData(mList.get(position).getId());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipelist_property_seller;
    }

}


class SwipeSellerHolder extends RecyclerView.ViewHolder {
    SwipeLayout mSwipeLayout;
    TextView mTextName, mTextCost, mTextCategory, mTextAddress;
    ImageView mImageEdit, mImageDelete, mImageHead;
    public SwipeSellerHolder(View itemView) {
        super(itemView);
        mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipelist_property_seller);
        mTextName = (TextView) itemView.findViewById(R.id.swipe_name);
        mTextCost = (TextView) itemView.findViewById(R.id.swipe_cost);
        mTextCategory = (TextView) itemView.findViewById(R.id.swipe_cate);
        mTextAddress = (TextView) itemView.findViewById(R.id.swipe_addr);
        mImageEdit = (ImageView) itemView.findViewById(R.id.swipe_edit_property);
        mImageDelete = (ImageView) itemView.findViewById(R.id.swipe_delete_property);
        mImageHead = (ImageView) itemView.findViewById(R.id.swipe_image);
    }
}
