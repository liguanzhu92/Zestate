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
import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyDetailFragment extends Fragment {
    private View mView;
    private ViewPager mPager;
    private DetailPagerAdapter mDetailPagerAdapter;
    private Button mButtonEdit, mButtonDelete;
    private int[] imageSrc = {R.drawable.house_sample1, R.drawable.house_sample2, R.drawable.house_sample3};

    public PropertyDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_property_detail, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mDetailPagerAdapter = new DetailPagerAdapter(getContext());
        mPager = (ViewPager) mView.findViewById(R.id.property_detail_pager);
        mPager.setAdapter(mDetailPagerAdapter);
        mButtonEdit = (Button) mView.findViewById(R.id.detail_edit);
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPropertyFragment newPropertyFragment = new NewPropertyFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.seller_activity_container, newPropertyFragment)
                        .addToBackStack(PropertyDetailFragment.class.getName())
                        .commit();
            }
        });
        mButtonDelete = (Button) mView.findViewById(R.id.detail_delete);
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
