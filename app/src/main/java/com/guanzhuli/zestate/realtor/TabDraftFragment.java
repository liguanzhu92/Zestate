package com.guanzhuli.zestate.realtor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.util.Attributes;
import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabDraftFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeSellerAdapter mAdapter;

    public TabDraftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_draft, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_tab_draft);
        mAdapter = new SwipeSellerAdapter(getContext());
        ((SwipeSellerAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

}
