package com.guanzhuli.zestate.realtor.adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.realtor.SellerActivity;
import com.guanzhuli.zestate.realtor.fragment.PropertyDetailFragment;
import com.guanzhuli.zestate.realtor.fragment.SellerHomeFragment;

/**
 * Created by Guanzhu Li on 1/21/2017.
 */
public class SwipeSellerAdapter extends RecyclerSwipeAdapter<SwipeSellerHolder>{

    private Context mContext;

    public SwipeSellerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SwipeSellerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_seller, parent, false);
        SwipeSellerHolder swipeSellerHolder = new SwipeSellerHolder(view);
        return swipeSellerHolder;
    }

    @Override
    public void onBindViewHolder(SwipeSellerHolder viewHolder, int position) {
        viewHolder.mTextName.isInEditMode();
        viewHolder.mTextName.getEditableText();
        viewHolder.mSwipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
                PropertyDetailFragment propertyDetailFragment = new PropertyDetailFragment();
                ((SellerActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.seller_activity_container, propertyDetailFragment)
                        .addToBackStack(SellerHomeFragment.class.getName())
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipelist_property_seller;
    }

}


class SwipeSellerHolder extends RecyclerView.ViewHolder {
    SwipeLayout mSwipeLayout;
    TextView mTextName, mTextType, mTextCategory, mTextAddress;
    ImageView mImageEdit, mImageDelete;
    public SwipeSellerHolder(View itemView) {
        super(itemView);
        mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipelist_property_seller);
        mTextName = (TextView) itemView.findViewById(R.id.swipe_name);
        mTextType = (TextView) itemView.findViewById(R.id.swipe_type);
        mTextCategory = (TextView) itemView.findViewById(R.id.swipe_cate);
        mTextAddress = (TextView) itemView.findViewById(R.id.swipe_addr);
        mImageEdit = (ImageView) itemView.findViewById(R.id.swipe_edit_property);
        mImageDelete = (ImageView) itemView.findViewById(R.id.swipe_delete_property);
    }
}
