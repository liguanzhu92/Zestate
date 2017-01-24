package com.guanzhuli.zestate.realtor.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.model.PostPropertyList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyDetailFragment extends Fragment {
    private View mView;
    private ViewPager mPager;
    private DetailPagerAdapter mDetailPagerAdapter;
    private Button mButtonEdit, mButtonDelete;
    private ImageView mImageLocation;
    private TextView mTextAddress,mTextName,mTextType, mTextCategory, mTextCost, mTextSize, mTextDescription;
    private int[] imageSrc = {R.drawable.house_sample1, R.drawable.house_sample2, R.drawable.house_sample3};
    private PostPropertyList mProperties = PostPropertyList.getInstance();

    public PropertyDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_property_detail, container, false);
        initView();
        setContent();
        return mView;
    }

    private void setContent() {
        final int position = getArguments().getInt("DetailPosition");
        mTextAddress.setText(mProperties.get(position).getAddress1() + mProperties.get(position).getAddress2() );
        mTextName.setText(mProperties.get(position).getName());
        mTextType.setText(mProperties.get(position).getType());
        mTextCategory.setText(String.valueOf(mProperties.get(position).getmCategory()));
        mTextCost.setText(mProperties.get(position).getCost());
        mTextSize.setText(mProperties.get(position).getSize());
        mTextDescription.setText(mProperties.get(position).getDescription());
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPropertyFragment newPropertyFragment = new NewPropertyFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("EditPosition", position);
                newPropertyFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.seller_activity_container, newPropertyFragment)
                        .addToBackStack(PropertyDetailFragment.class.getName())
                        .commit();
            }
        });
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String propertyId = mProperties.get(position).getId();
                String userId = mProperties.get(position).getUserId();
                PostPropertyList.getInstance().deleteData(propertyId);
                SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.seller_activity_container, sellerHomeFragment)
                        .addToBackStack(PropertyDetailFragment.class.getName())
                        .commit();
            }
        });
        mImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initView() {
        mDetailPagerAdapter = new DetailPagerAdapter(getContext());
        mPager = (ViewPager) mView.findViewById(R.id.property_detail_pager);
        mPager.setAdapter(mDetailPagerAdapter);
        mTextAddress = (TextView) mView.findViewById(R.id.seller_detail_address);
        mTextName = (TextView) mView.findViewById(R.id.seller_detail_name);
        mTextType = (TextView) mView.findViewById(R.id.seller_detail_type);
        mTextCategory = (TextView) mView.findViewById(R.id.seller_detail_category);
        mTextCost = (TextView) mView.findViewById(R.id.seller_detail_cost);
        mTextSize = (TextView) mView.findViewById(R.id.seller_detail_size);
        mTextDescription = (TextView) mView.findViewById(R.id.seller_detail_description);
        mButtonEdit = (Button) mView.findViewById(R.id.seller_detail_button_edit);
        mButtonDelete = (Button) mView.findViewById(R.id.seller_detail_button_delete);
        mImageLocation = (ImageView) mView.findViewById(R.id.seller_detail_location);
    }

    class DetailPagerAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;

        public DetailPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.detail_pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(imageSrc[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
