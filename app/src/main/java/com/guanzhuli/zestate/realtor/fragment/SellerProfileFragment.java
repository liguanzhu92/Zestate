package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerProfileFragment extends Fragment {


    public SellerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_profile, container, false);
    }

}
