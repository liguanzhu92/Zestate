package com.guanzhuli.zestate.realtor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.guanzhuli.zestate.R;

/**
 * Created by Guanzhu Li on 1/21/2017.
 */
public class SwipeSellerAdapter extends RecyclerSwipeAdapter<SwipeSellerHolder> implements View.OnClickListener {

    private Context mContext;

    public SwipeSellerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SwipeSellerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_seller, parent, false);
        return new SwipeSellerHolder(view);
    }

    @Override
    public void onBindViewHolder(SwipeSellerHolder viewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipelist_property_seller;
    }

    @Override
    public void onClick(View view) {

    }
}
class SwipeSellerHolder extends RecyclerView.ViewHolder {
    SwipeLayout mSwipeLayout;
    TextView mTextName, mTextType, mTextCategory, mTextAddress;
    ImageView mImageEdit, mImageDelete;
    public SwipeSellerHolder(View itemView) {
        super(itemView);
        mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipelist_property_seller);

    }
}
