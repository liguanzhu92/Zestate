package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.model.PostPropertyList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerMapFragment extends Fragment {
    private View mView;
    private PostPropertyList mPropertyList = PostPropertyList.getInstance();

    public SellerMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_seller_map, container, false);
        return mView;
    }

}
