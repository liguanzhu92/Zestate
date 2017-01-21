package com.guanzhuli.zestate.realtor;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerHomeFragment extends Fragment {
    private ViewPager mViewPager;
    private SellerPagerAdapter mSellerPagerAdapter;


    public SellerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mSellerPagerAdapter = new SellerPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.seller_home_container);
        mViewPager.setAdapter(mSellerPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.seller_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    public class SellerPagerAdapter extends FragmentPagerAdapter{

        public SellerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0 :
                    TabPostFragment tab1 = new TabPostFragment();
                    return tab1;
                case 1:
                    TabDraftFragment tab2 = new TabDraftFragment();
                    return tab2;
                case 2:
                    TabAllFragment tab3 = new TabAllFragment();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Post";
                case 1:
                    return "Draft";
                case 2:
                    return "All";
            }
            return null;
        }
    }
}
