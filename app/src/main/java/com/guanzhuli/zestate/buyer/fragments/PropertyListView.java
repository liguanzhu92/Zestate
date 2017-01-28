package com.guanzhuli.zestate.buyer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.buyer.adapters.PropertyRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyListView extends Fragment implements PropertyRecyclerView.OnClickCard {


    public PropertyListView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_property_list_view, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.property_list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(new PropertyRecyclerView(getActivity(),this));
        return view;
    }

    @Override
    public void onClickOfCardItem() {

    }
}
