package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.PostPropertyList;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.realtor.SellerActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerHomeFragment extends Fragment {
    private ViewPager mViewPager;
    private SellerPagerAdapter mSellerPagerAdapter;
    private String userId;
    private static final String basicURL = "http://www.rjtmobile.com/realestate/getproperty.php?all&userid=";
    private static String TAG = SellerActivity.class.getSimpleName();
    private PostPropertyList mProperties = PostPropertyList.getInstance();

    public SellerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);
        userId = "17";
        mProperties.updateData(userId);
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
